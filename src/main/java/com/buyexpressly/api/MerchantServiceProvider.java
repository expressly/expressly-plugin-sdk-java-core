package com.buyexpressly.api;

import com.buyexpressly.api.resource.merchant.EmailAddressRequest;
import com.buyexpressly.api.resource.merchant.EmailStatusListResponse;
import com.buyexpressly.api.resource.merchant.InvoiceListRequest;
import com.buyexpressly.api.resource.merchant.InvoiceResponse;
import com.buyexpressly.api.resource.server.CartData;
import com.buyexpressly.api.resource.server.CustomerData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface MerchantServiceProvider {
    // Called by MerchantServiceRouter.displayPopup()
    void popupHandler(HttpServletRequest request, HttpServletResponse response, ExpresslyProvider expresslyProvider) throws IOException;

    String registerCustomer(String email, CustomerData customerData);

    boolean sendPasswordResetEmail(String customerReference);

    boolean createCustomerCart(String customerReference, CartData cartData);

    CustomerData getCustomerData(String customerReference);

    EmailStatusListResponse checkCustomerStatus(EmailAddressRequest emailAddressRequest);

    List<InvoiceResponse> getInvoices(InvoiceListRequest request);

    String getShopUrl();

    String getLocale();

    String getCustomerReference(String email);

    boolean checkCustomerAlreadyExists(String email);

    String getMigratedRedirectLocation();

    void loginCustomer(String merchantUserReference, HttpServletRequest request, HttpServletResponse response) throws IOException;

    void handleCustomerAlreadyExists(String email, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
