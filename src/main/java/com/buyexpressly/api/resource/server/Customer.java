package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

//TODO[][]: write builder, remove contructor and test
@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Customer {
    private CustomerData customerData;
    private String email;

    public Customer(CustomerData customerData, String email) {
        this.customerData = Builders.requiresNonNull(customerData, "Customer Cannot be null");
        this.email = Builders.requiresNonNull(email, "Invalid Email");
    }

    private Customer() {
    }

    public CustomerData getCustomerData() {
        return customerData;
    }

    public String getEmail() {
        return email;
    }

}
