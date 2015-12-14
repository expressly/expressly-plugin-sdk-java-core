package com.buyexpressly.api;

import com.buyexpressly.api.resource.merchant.EmailAddressRequest;
import com.buyexpressly.api.resource.merchant.EmailStatusListResponse;
import com.buyexpressly.api.resource.merchant.InvoiceListRequest;
import com.buyexpressly.api.resource.merchant.InvoiceResponse;
import com.buyexpressly.api.resource.server.CartData;
import com.buyexpressly.api.resource.server.CustomerData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface MerchantServiceProvider {
    // Called by MerchantServiceRouter.displayPopup()
    void popupHandler(HttpServletRequest request, HttpServletResponse response, ExpresslyProvider expresslyProvider);

    String registerCustomer(String email, CustomerData customerData);

    boolean sendPasswordResetEmail(String customerReference);

    boolean createCustomerCart(String customerReference, CartData cartData);

    CustomerData getCustomerData(String email);

    EmailStatusListResponse checkCustomerStatus(EmailAddressRequest emailAddressRequest);

    List<InvoiceResponse> getInvoices(InvoiceListRequest request);

    String getShopUrl();

    String getLocale();

    boolean checkCustomerAlreadyExists(String email);

    /**
     *
     * Logs the customer session in
     * Needs to know where the customer is being redirected to.!
     * (log in landing page).
     *
     * @param merchantUserReference
     * @param request
     * @param response
     */
    void loginAndRedirectCustomer(String merchantUserReference, HttpServletRequest request, HttpServletResponse response);

    /**
     *
     * @param email can be null
     * @param request
     * @param response
     */
    void handleCustomerAlreadyExists(String email, HttpServletRequest request, HttpServletResponse response);
}
