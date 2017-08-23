package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;

public final class Address {
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

    private Address() {
    }

    private Address(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.address1 = builder.address1;
        this.address2 = builder.address2;
        this.city = builder.city;
        this.companyName = builder.companyName;
        this.zip = builder.zip;
        this.phone = builder.phone;
        this.addressAlias = builder.addressAlias;
        this.stateProvince = builder.stateProvince;
        this.country = builder.country;
    }

    public static Builder builder() {
        return new Builder();
    }

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

    public static final class Builder {
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

        public Builder withFirstName(String firstName) {
            Builders.len(firstName, "firstName", 64);
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            Builders.len(lastName, "lastName", 64);
            this.lastName = lastName;
            return this;
        }

        public Builder withAddress1(String address1) {
            String address = address1 == null ? null : cleanUpAddress(address1);
            Builders.len(address, "address1", 64);
            this.address1 = address;
            return this;
        }

        public Builder withAddress2(String address2) {
            String address = address2 == null ? "" : cleanUpAddress(address2);
            Builders.len(address, "address2", 64);
            this.address2 = address;
            return this;
        }

        private String cleanUpAddress(String address) {
            return address.replaceAll("[\\.\',]", "");
        }

        public Builder withCity(String city) {
            Builders.len(city, "city", 64);
            this.city = city;
            return this;
        }

        public Builder withCompanyName(String companyName) {
            Builders.len(companyName, "companyName", 64);
            this.companyName = companyName;
            return this;
        }

        public Builder withZip(String zip) {
            Builders.len(zip, "zip", 16);
            this.zip = zip;
            return this;
        }

        public Builder withPhone(String phone) {
            Builders.len(phone, "phone", 24);
            this.phone = Integer.parseInt(phone);
            return this;
        }

        public Builder withAddressAlias(String addressAlias) {
            Builders.len(addressAlias, "addressAlias", 32);
            this.addressAlias = addressAlias;
            return this;
        }

        public Builder withProvince(String province) {
            Builders.len(province, "province", 64);
            this.stateProvince = province;
            return this;
        }

        public Builder withCountry(String country) {
            Builders.len(country, "country", 3);
            this.country = country;
            return this;
        }

        public Address build() {
            Builders.required(address1, "address1");
            Builders.required(city, "city");
            Builders.required(zip, "zip");
            Builders.required(country, "country");
            return new Address(this);
        }
    }
}
