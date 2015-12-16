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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
                ping(response);
                break;
            case PING_REGISTERED:
                pingRegistered(response);
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

    private void ping(HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getWriter(), PingResponse.build());
    }

    private void pingRegistered(HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getWriter(), PingRegisteredResponse.build());
    }

    private void getInvoices(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InvoiceListRequest providerRequest = mapper.readValue(request.getReader(), InvoiceListRequest.class);
        mapper.writeValue(response.getWriter(), merchantServiceHandler.getInvoices(providerRequest));
    }

    private void displayPopup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        merchantServiceHandler.getPopup(request, response);
    }

    private void confirmMigration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        merchantServiceHandler.confirmMigration(request, response);
    }

    private class InternalMerchantServiceHandler {
        private MerchantServiceProvider merchantServiceProvider;
        private ExpresslyProvider expresslyProvider;

        public InternalMerchantServiceHandler(MerchantServiceProvider merchantServiceProvider, ExpresslyProvider expresslyProvider) {
            this.merchantServiceProvider = merchantServiceProvider;
            this.expresslyProvider = expresslyProvider;
        }

        public CustomerDataResponse getCustomerData(String email) {
            Metadata meta = new Metadata(merchantServiceProvider.getShopUrl(), merchantServiceProvider.getLocale(), new ArrayList<Tuple>());
            CustomerData data = merchantServiceProvider.getCustomerData(email);
            Customer customer = Customer.build(data, email);
            return CustomerDataResponse.builder().withData(customer).withMeta(meta).build();
        }

        public InvoiceListResponse getInvoices(InvoiceListRequest request) {
            return InvoiceListResponse.builder().addAll(merchantServiceProvider.getInvoices(request)).build();
        }

        public EmailStatusListResponse checkEmailAddresses(EmailAddressRequest request) {
            return merchantServiceProvider.checkCustomerStatus(request);
        }

        public MigrationResponse acceptMigration(String campaignCustomerUuid) throws IOException {
            MigrationResponse response = expresslyProvider.fetchMigrationCustomerData(campaignCustomerUuid);
            String email = response.getMigrationData().getData().getEmail();
            Builders.requiresNonNull(email, "Customer Email missing, not enough information to migrate customer");
            Builders.pattern(email, "migratedCustomerEmail", "(.+@.+)");
            return response;
        }

        public String registerCustomer(MigrationResponse data) {
            String email = data.getMigrationData().getData().getEmail();
            String merchantUserReference = merchantServiceProvider.registerCustomer(email, data.getMigrationData().getData().getCustomerData());
            Builders.validate(!Builders.isNullOrEmpty(merchantUserReference), String.format("Failed to register customer %s with merchant", email));
            Builders.validate(merchantServiceProvider.sendPasswordResetEmail(merchantUserReference), String.format("Failed to send password reset email to %s", email));
            return merchantUserReference;
        }

        private void confirmMigration(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String campaignCustomerUuid = MerchantServiceRoute.CONFIRM_MIGRATION.getUriParameters(request.getRequestURI()).get("campaignCustomerUuid");
            try {
                MigrationResponse data = acceptMigration(campaignCustomerUuid);
                if (checkCustomerAlreadyExists(data)) {
                    handleCustomerAlreadyExists(data.getMigrationData().getData().getEmail(), request, response);
                } else {
                    String merchantUserReference = registerCustomer(data);
                    populateCart(merchantUserReference, data.getCartData());
                    confirmCustomer(campaignCustomerUuid);
                    loginAndRedirectCustomer(merchantUserReference, request, response);
                }
            } catch (ExpresslyException e) {
                if (e.getMessage().contains("USER_ALREADY_MIGRATED")) {
                    handleCustomerAlreadyExists(null, request, response);
                } else {
                    throw e;
                }
            }
        }

        public boolean checkCustomerAlreadyExists(MigrationResponse data) {
            String email = data.getMigrationData().getData().getEmail();
            return merchantServiceProvider.checkCustomerAlreadyExists(email);
        }

        public void populateCart(String email, CartData cartData) {
            Builders.validate(merchantServiceProvider.createCustomerCart(email, cartData), "Failed to add cart to customer id: %s");
        }

        public void confirmCustomer(String campaignCustomerUuid) throws IOException {
            Builders.validate(expresslyProvider.finaliseMigrationOfCustomerData(campaignCustomerUuid), String.format("Couldn't confirm the migration of %s with xly server", campaignCustomerUuid));
        }

        public void getPopup(HttpServletRequest request, HttpServletResponse response) throws IOException {
            merchantServiceProvider.popupHandler(request, response, expresslyProvider);
        }

        public void loginAndRedirectCustomer(String merchantUserReference, HttpServletRequest request, HttpServletResponse response) throws IOException {
            merchantServiceProvider.loginAndRedirectCustomer(merchantUserReference, request, response);
        }

        public void handleCustomerAlreadyExists(String email, HttpServletRequest request, HttpServletResponse response) throws IOException {
            merchantServiceProvider.handleCustomerAlreadyExists(email, request, response);
        }
    }
}