package com.buyexpressly.api;

import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.merchant.CustomerDataResponse;
import com.buyexpressly.api.resource.merchant.EmailAddressRequest;
import com.buyexpressly.api.resource.merchant.EmailStatusListResponse;
import com.buyexpressly.api.resource.merchant.InvoiceListRequest;
import com.buyexpressly.api.resource.merchant.InvoiceListResponse;
import com.buyexpressly.api.resource.merchant.PingRegisteredResponse;
import com.buyexpressly.api.resource.merchant.PingResponse;
import com.buyexpressly.api.resource.server.CartData;
import com.buyexpressly.api.resource.server.Customer;
import com.buyexpressly.api.resource.server.CustomerData;
import com.buyexpressly.api.resource.server.Metadata;
import com.buyexpressly.api.resource.server.MigrationResponse;
import com.buyexpressly.api.resource.server.Tuple;
import com.buyexpressly.api.util.Builders;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MerchantServiceRouter {
    private final String expectedAuthorizationHeader;
    private final InternalMerchantServiceHandler merchantServiceHandler;
    private final ObjectMapper mapper;

    public MerchantServiceRouter(String expresslyApiKey, MerchantServiceProvider merchantServiceProvider, ExpresslyProvider expresslyProvider) {
        Builders.validateApiKey(expresslyApiKey);
        Objects.requireNonNull(merchantServiceProvider, "provider is null");
        this.merchantServiceHandler = new InternalMerchantServiceHandler(merchantServiceProvider, expresslyProvider);
        this.expectedAuthorizationHeader = "Basic " + expresslyApiKey;
        this.mapper = new ObjectMapper();
    }

    public void route(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MerchantServiceRoute route = MerchantServiceRoute.findRoute(
                request.getMethod(),
                request.getRequestURI());

        if (route.isAuthenticated() && !isAuthenticated(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        switch (route) {
            case PING:
                ping(request, response);
                break;
            case PING_REGISTERED:
                pingRegistered(request, response);
                break;
            case DISPLAY_POPUP:
                displayPopup(request, response);
                break;
            case GET_CUSTOMER:
                getCustomer(request, response);
                break;
            case GET_INVOICES:
                getInvoices(request, response);
                break;
            case CHECK_EMAILS:
                checkEmails(request, response);
                break;
            case CONFIRM_MIGRATION:
                confirmMigration(request, response);
                break;
            default:
                throw new IllegalArgumentException(String.format(
                        "invalid expressly merchant route, httpMethod=%s, uri=%s",
                        request.getMethod(),
                        request.getRequestURI()));
        }
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        return expectedAuthorizationHeader.equals(request.getHeader("Authorization"));
    }

    private void getCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = MerchantServiceRoute.GET_CUSTOMER.getUriParameters(request.getRequestURI()).get("email");
        mapper.writeValue(response.getWriter(), merchantServiceHandler.getCustomerData(email));
    }

    private void checkEmails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EmailAddressRequest emailAddressRequest = mapper.readValue(request.getReader(), EmailAddressRequest.class);
        mapper.writeValue(response.getWriter(), merchantServiceHandler.checkEmailAddresses(emailAddressRequest));
    }

    private void ping(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getWriter(), PingResponse.build());
    }

    private void pingRegistered(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getWriter(), PingRegisteredResponse.build());
    }

    private void getInvoices(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InvoiceListRequest providerRequest = mapper.readValue(request.getReader(), InvoiceListRequest.class);
        mapper.writeValue(response.getWriter(), merchantServiceHandler.getInvoices(providerRequest));
    }

    private void displayPopup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.getSession().setAttribute(
                    "popupContent",
                    merchantServiceHandler.getDisplayPopupAction(MerchantServiceRoute.DISPLAY_POPUP.getUriParameters(request.getRequestURI()).get("campaignCustomerUuid")));
            request.getRequestDispatcher(merchantServiceHandler.getPopUpDestination()).forward(request, response);
        } catch (ServletException | ExpresslyException e) {
            merchantServiceHandler.redirectToHomePage(response);
        }
    }

    //INTERNAL routes!!!\\
    private void confirmMigration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String campaignCustomerUuid = MerchantServiceRoute.CONFIRM_MIGRATION.getUriParameters(request.getRequestURI()).get("campaignCustomerUuid");
        MigrationResponse data = merchantServiceHandler.acceptMigration(campaignCustomerUuid);
        String merchantUserReference = merchantServiceHandler.registerCustomer(data.getMigrationData().getData());
        merchantServiceHandler.populateCart(merchantUserReference, data.getCartData());
        merchantServiceHandler.confirmCustomer(campaignCustomerUuid);
        response.sendRedirect(merchantServiceHandler.getMigrationRedirectLocation(merchantUserReference));
    }

    private class InternalMerchantServiceHandler {
        private MerchantServiceProvider merchantServiceProvider;
        private ExpresslyProvider expresslyProvider;

        public InternalMerchantServiceHandler(MerchantServiceProvider merchantServiceProvider, ExpresslyProvider expresslyProvider) {
            this.merchantServiceProvider = merchantServiceProvider;
            this.expresslyProvider = expresslyProvider;
        }

        public String getDisplayPopupAction(String campaignCustomerUuid) throws IOException {
            return expresslyProvider.fetchMigrationConfirmationHtml(campaignCustomerUuid);
        }

        public CustomerDataResponse getCustomerData(String email) {
            Metadata meta = new Metadata(merchantServiceProvider.getShopUrl(), merchantServiceProvider.getLocale(), new ArrayList<Tuple>());
            CustomerData data = merchantServiceProvider.getCustomerData(email);
            Customer customer = new Customer(data, email, merchantServiceProvider.getCustomerReference(email));
            return CustomerDataResponse.builder().withData(customer).withMeta(meta).build();
        }

        public InvoiceListResponse getInvoices(InvoiceListRequest request) {
            return InvoiceListResponse.builder().addAll(merchantServiceProvider.getInvoices(request)).build();
        }

        public EmailStatusListResponse checkEmailAddresses(EmailAddressRequest request) {
            List<String> existing = merchantServiceProvider.getExistingEmails(request.getEmails());
            List<String> deleted = merchantServiceProvider.getDeletedEmails(request.getEmails());
            List<String> pending = merchantServiceProvider.getPendingEmails(request.getEmails());
            return EmailStatusListResponse.builder().addExisting(existing).addDeleted(deleted).addPending(pending).build();
        }


        // INTERNAL \\

        public MigrationResponse acceptMigration(String campaignCustomerUuid) throws IOException {
            MigrationResponse response = expresslyProvider.fetchMigrationCustomerData(campaignCustomerUuid);
            String email = response.getMigrationData().getData().getEmail();
            Builders.requiresNonNull(email, "Customer Email missing, not enough information to migrate customer");
            Builders.pattern(email, "migratedCustomerEmail", "(.+@.+)");
            return response;
        }

        public String registerCustomer(Customer data) {
            String email = data.getEmail();
            Builders.validate(merchantServiceProvider.checkCustomerIsNew(email), String.format("Customer %s has already been migrated", email));
            String merchantUserReference = merchantServiceProvider.registerCustomer(email, data.getCustomerData());
            Builders.validate(!Builders.isNullOrEmpty(merchantUserReference), String.format("Failed to register customer %s with merchant", email));
            Builders.validate(merchantServiceProvider.sendPasswordResetEmail(merchantUserReference), String.format("Failed to send password reset email to %s", email));
            return merchantUserReference;
        }

        public void populateCart(String email, CartData cartData) {
            Builders.validate(merchantServiceProvider.storeCartData(email, cartData), "Failed to add cart to customer id: %s");
        }

        public void confirmCustomer(String campaignCustomerUuid) throws IOException {
            Builders.validate(expresslyProvider.finaliseMigrationOfCustomerData(campaignCustomerUuid), String.format("Couldn't confirm the migration of %s with xly server", campaignCustomerUuid));
        }

        public String getMigrationRedirectLocation(String merchantUserReference) {
            return merchantServiceProvider.loginCustomer(merchantUserReference);
        }


        public String getPopUpDestination() {
            return merchantServiceProvider.getPopupDestination();
        }

        public void redirectToHomePage(HttpServletResponse response) throws IOException {
            response.sendRedirect(merchantServiceProvider.getHomePageLocation());
        }
    }
}