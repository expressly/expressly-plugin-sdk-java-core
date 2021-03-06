package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;

public final class Phone {
    private String type;
    private String number;
    private Integer countryCode;

    private Phone() {
    }

    private Phone(Builder builder) {
        this.type = builder.type;
        this.number = builder.number;
        this.countryCode = builder.countryCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public static final class Builder {
        private String type;
        private String number;
        private Integer countryCode;

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder withCountryCode(Integer countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Phone build() {
            Builders.required(number, "number");
            Builders.len(number, "number", 24);
            return new Phone(this);
        }
    }
}
