package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Customer {
    private CustomerData customerData;
    private String email;
    private String userReference;

    public Customer(CustomerData customerData, String email, String userReference) {
        this.customerData = Builders.requiresNonNull(customerData, "Customer Cannot be null");
        this.email = Builders.requiresNonNull(email, "Invalid Email");
        this.userReference = userReference;
    }

    private Customer() {
    }

    public CustomerData getCustomerData() {
        return customerData;
    }

    public String getEmail() {
        return email;
    }

    public String getUserReference() {
        return userReference;
    }

}
