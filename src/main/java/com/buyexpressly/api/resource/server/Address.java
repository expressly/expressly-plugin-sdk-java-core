package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Address {
    private String firstName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String companyName;
    private String zip;
    private Integer phone;
    private String addressAlias;
    private String stateProvince;
    private String country;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getZip() {
        return zip;
    }

    public Integer getPhone() {
        return phone;
    }

    public String getAddressAlias() {
        return addressAlias;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public String getCountry() {
        return country;
    }
}
