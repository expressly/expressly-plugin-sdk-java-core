package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;
import com.buyexpressly.api.util.DateTimeIso8601DateSerializer;
import com.buyexpressly.api.util.LocalDateIso8601DateSerializer;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CustomerData {
    private String firstName;
    private String lastName;
    private String gender;
    private String company;
    @JsonSerialize(using = LocalDateIso8601DateSerializer.class)
    private LocalDate dob;
    private String taxNumber;
    @JsonSerialize(using = DateTimeIso8601DateSerializer.class)
    private DateTime dateUpdated;
    @JsonSerialize(using = LocalDateIso8601DateSerializer.class)
    private LocalDate dateLastOrder;
    private Integer numberOrdered;
    private Integer billingAddress;
    private Integer shippingAddress;
    private List<Tuple> onlinePresence = new ArrayList<>();
    private List<Email> emails = new ArrayList<>();
    private List<Phone> phones = new ArrayList<>();
    private List<Address> addresses = new ArrayList<>();

    private CustomerData() {
    }

    private CustomerData(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.company = builder.company;
        this.dob = builder.dob;
        this.taxNumber = builder.taxNumber;
        this.dateUpdated = builder.dateUpdated;
        this.dateLastOrder = builder.dateLastOrder;
        this.numberOrdered = builder.numberOrdered;
        this.onlinePresence = Collections.unmodifiableList(new ArrayList<>(builder.onlineIdentities));
        this.emails = Collections.unmodifiableList(new ArrayList<>(builder.emailAddresses));
        this.phones = Collections.unmodifiableList(new ArrayList<>(builder.phoneNumbers));
        this.addresses = Collections.unmodifiableList(builder.addresses);
        this.billingAddress = builder.billingAddress;
        this.shippingAddress = builder.shippingAddress;
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

    public String getGender() {
        return gender;
    }

    public String getCompany() {
        return company;
    }

    public LocalDate getDob() {
        return dob;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public DateTime getDateUpdated() {
        return dateUpdated;
    }

    public LocalDate getDateLastOrder() {
        return dateLastOrder;
    }

    public Integer getNumberOrdered() {
        return numberOrdered;
    }

    public List<Tuple> getOnlinePresence() {
        return Collections.unmodifiableList(onlinePresence);
    }

    public List<Email> getEmails() {
        return Collections.unmodifiableList(emails);
    }

    public List<Phone> getPhones() {
        return Collections.unmodifiableList(phones);
    }

    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public Integer getBillingAddress() {
        return billingAddress;
    }

    public Integer getShippingAddress() {
        return shippingAddress;
    }

    public static final class Builder {
        private String firstName;
        private String lastName;
        private String gender;
        private String company;
        private LocalDate dob;
        private String taxNumber;
        private DateTime dateUpdated;
        private LocalDate dateLastOrder;
        private Integer numberOrdered;
        private Integer billingAddress;
        private Integer shippingAddress;
        private List<Tuple> onlineIdentities = new ArrayList<>();
        private List<Email> emailAddresses = new ArrayList<>();
        private List<Phone> phoneNumbers = new ArrayList<>();
        private List<Address> addresses = new ArrayList<>();

        private Builder() {

        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withGender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder withCompany(String company) {
            this.company = company;
            return this;
        }

        public Builder withDob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public Builder withTaxNumber(String taxNumber) {
            this.taxNumber = taxNumber;
            return this;
        }

        public Builder withLastUpdatedAtSource(DateTime lastUpdatedAtSource) {
            this.dateUpdated = lastUpdatedAtSource;
            return this;
        }

        public Builder withLastOrderTimeAtSource(LocalDate lastOrderTimeAtSource) {
            this.dateLastOrder = lastOrderTimeAtSource;
            return this;
        }

        public Builder withOrderItemCount(int orderItemCount) {
            this.numberOrdered = orderItemCount;
            return this;
        }

        public Builder withShippingAddress(int shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder withBillingAddress(int billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public Builder addOnlineIdentity(Tuple onlineIdentity) {
            this.onlineIdentities.add(onlineIdentity);
            return this;
        }

        public Builder addEmailAddress(Email emailAddress) {
            this.emailAddresses.add(emailAddress);
            return this;
        }

        public Builder addPhoneNumber(Phone phoneNumber) {
            this.phoneNumbers.add(phoneNumber);
            return this;
        }

        public Builder addAddress(Address address) {
            this.addresses.add(address);
            return this;
        }

        public CustomerData build() {
            Builders.required(firstName, "firstName");
            Builders.required(lastName, "lastName");
            Builders.required(gender, "gender");
            validateAddresses(addresses);
            checkForDefaultAddress();
            return new CustomerData(this);
        }

        private void checkForDefaultAddress() {
            billingAddress = billingAddress == null ? 0 : billingAddress;
            shippingAddress = shippingAddress == null ? 0 : shippingAddress;
        }

        private void validateAddresses(List<Address> addresses) {
            Builders.validate(!addresses.isEmpty(), "At least one address is required");
        }
    }
}
