package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;

public final class Customer {
    private CustomerData customerData;
    private String email;

    private Customer(CustomerData customerData, String email) {
        this.customerData = customerData;
        this.email = email;
    }

    private Customer() {
    }

    public CustomerData getCustomerData() {
        return customerData;
    }

    public String getEmail() {
        return email;
    }

    public static Customer build(CustomerData data, String email) {
        Builders.required(data, "customerData");
        Builders.required(email, "email");
        Builders.pattern(email, "email", "(.+@.+)");
        return new Customer(data, email);
    }
}
