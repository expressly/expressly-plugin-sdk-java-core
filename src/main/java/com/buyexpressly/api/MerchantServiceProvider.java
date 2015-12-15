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

/**
 *
 * Provider needed for the the expressly sdk to interface with the merchant's system
 *
 * Handles db interactions
 * Handles customer flows throughout the migration
 *
 * Used by {@link MerchantServiceRouter}
 *
 * Example implementation can be found at https://github.com/expressly/expressly-plugin-java-reference-implementation
 *
 */
public interface MerchantServiceProvider {
    /**
     *
     * Request the popup from the expressly server and render it.
     *
     * @param request from the servlet, the uri property includes the campaignCustomerUuid necessary to request the popup.
     * @param response from the servlet, needed to handle rendering the popup.
     * @param expresslyProvider used to request the popup html from the expressly server
     */
    void popupHandler(HttpServletRequest request, HttpServletResponse response, ExpresslyProvider expresslyProvider);

    /**
     *
     * Persists customer in the database
     *
     * the returned value will be used in the future to refer to the customer just created
     * it does not need to be the email, just needs to be the same throughout the flow.
     * will be referred to as customer reference
     *
     * @param email of the customer
     * @param customerData containing the information needed to store the customer
     * @return
     */
    String registerCustomer(String email, CustomerData customerData);

    /**
     * Reset user password and send them an email
     * so they can login in the future
     *
     * @param customerReference used to identify the customer
     * @return true if reset and email sending is successful
     */
    boolean sendPasswordResetEmail(String customerReference);

    /**
     *
     * Persists the cart information against the customer.
     * Product is optional.
     *
     * @param customerReference used to identify the customer
     * @param cartData object containing product id (optional) and coupon code
     * @return true if cart creation is successful
     */
    boolean createCustomerCart(String customerReference, CartData cartData);

    /**
     *
     * Returns the information of the requested customer in the correct format
     *
     * @param email of the customer to be retrieved
     * @return customer data object
     */
    CustomerData getCustomerData(String email);

    /**
     *
     * Goes through the list of emails in the request object,
     * Checks which of the emails exist in the customer database and flags them as existing,
     * checks which of the emails have existed in the past but are now inactive, and flags them as deleted,
     * checks which of the emails have been added to the database, but the registration hasn't been concluded, and flags them as pending
     *
     * @param emailAddressRequest object containing the list of emails to verify
     * @return an object with the emails organised by status. Any email that is not existing, pending or deleted is not included in this response.
     */
    EmailStatusListResponse checkCustomerStatus(EmailAddressRequest emailAddressRequest);

    /**
     *
     * Collects the orders information on each of the customers identified in the request and within the specified time interval
     * (defined in the request)
     *
     * @param request defines the emails and time intervals of the customers under analysis
     * @return a collection of properly formatted invoice objects containing the information requested
     */
    List<InvoiceResponse> getInvoices(InvoiceListRequest request);

    /**
     *
     * @return the url of the merchant, to be used in the customer metadata query
     */
    String getShopUrl();

    /**
     *
     * @return a string to be used in the locale metadata field of request (example: "GBR")
     */
    String getLocale();

    /**
     *
     * Checks the database for whether the customer already exists
     *
     * @param email of the customer to check
     * @return TRUE if the customer already exists
     */
    boolean checkCustomerAlreadyExists(String email);

    /**
     *
     * Logs the customer session in
     * handles the redirect of the customer
     * (for example, to the log in landing page).
     *
     * @param merchantUserReference customer reference used to identify the customer
     * @param request from the servlet, in case it is needed to handle the query
     * @param response from the servlet, in case it is needed to handle the redirect
     */
    void loginAndRedirectCustomer(String merchantUserReference, HttpServletRequest request, HttpServletResponse response);

    /**
     *
     * method called when the existence of the customer in the shop is verified before the migration is concluded.
     * user experience should be handled smoothly
     * for example by being redirected to the homepage (avoid a black screen)
     *
     * @param email can be null
     * @param request from the servlet
     * @param response from the servlet
     */
    void handleCustomerAlreadyExists(String email, HttpServletRequest request, HttpServletResponse response);
}
