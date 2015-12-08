package com.buyexpressly.api;

import com.buyexpressly.api.resource.error.ExpresslyException;
import com.buyexpressly.api.resource.merchant.InvoiceListRequest;
import com.buyexpressly.api.resource.merchant.InvoiceResponse;
import com.buyexpressly.api.resource.server.CartData;
import com.buyexpressly.api.resource.server.CustomerData;

import java.util.List;

public interface MerchantServiceProvider {

    // Data Access \\

    /**
     *
     * @param email
     * @return correctly formatted and validated customer data object
     * @throws ExpresslyException if customer is not found in db.
     */
    CustomerData getCustomerData(String email) throws ExpresslyException;

    /**
     *
     * Collates all orders made by each customer of a campaign during a specific period of time.
     *
     * @param request list of campaign emails and time period
     * @return Collection of orders organised by customer within the specified time period, in the correct, validated format.
     */
    List<InvoiceResponse> getInvoices(InvoiceListRequest request) throws ExpresslyException;

    /**
     *
     * Checks for emails that are currently registered and live at the merchant.
     *
     * @param request list of campaign emails
     * @return list of existing emails
     */
    List<String> getExistingEmails(List<String> request);

    /**
     * Checks for campaign emails that have been registered, but deleted in the meantime.
     * @param request list of campaign emails
     * @return list of deleted emails
     */
    List<String> getDeletedEmails(List<String> request);

    /**
     * Checks for group of campaign emails that have failed to terminate the migration successfuly.
     * @param request list of campaign emails
     * @return list of pending emails
     */
    List<String> getPendingEmails(List<String> request);

    /**
     *
     * Adds the customer in the DB
     *
     * @param email
     * @param customerData
     * @return Merchant Reference to id the user (could be email, could be anything else)
     */
    String registerCustomer(String email, CustomerData customerData);

    /**
     * Checks the db for the customer via email
     * @param email
     * @return true if customer is not in the DB
     */
    boolean checkCustomerIsNew(String email);

    /**
     *
     * Adds cart / coupon code to the customer
     *
     * @param email
     * @param cartData
     * @return success status of operation
     */
    boolean storeCartData(String email, CartData cartData);

    //METADATA\\

    /**
     *(can be null)
     *
     * @return db id of the customer
     */
    Integer getCustomerReference(String email);

    /**
     *
     * @return url of the shop
     */
    String getShopUrl();

    /**
     *
     * (can be null)
     *
     * @return locale of the shop in ISO3, ie: GBR
     */
    String getLocale();

    // Merchant Actions \\

    /**
     *
     * @param merchantUserReference detail used by the merchant to id the customer (for example, email address)
     * @return url of the main customer landing page, after customer is logged in
     */
    String loginCustomer(String merchantUserReference);

    /**
     *
     * @param email customer email
     * @return true if email was successfully sent
     */
    boolean sendPasswordResetEmail(String email);

    /**
     *
     * @return final destination of the popup request
     */
    String getPopupDestination();

    /**
     *
     * @return url of the home page location.
     */
    String getHomePageLocation();
}
