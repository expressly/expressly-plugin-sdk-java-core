package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;

public final class Email {
    private String alias;
    private String email;

    private Email() {
    }

    private Email(Builder builder) {
        this.alias = builder.alias;
        this.email = builder.email;
    }

    public String getAlias() {
        return alias;
    }

    public String getEmail() {
        return email;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String alias;
        private String email;

        public Builder withAlias(String alias) {
            this.alias = alias;
            Builders.len(alias, "alias", 32);
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            Builders.len(email, "email", 128);
            return this;
        }

        public Email build() {
            Builders.required(email, "email");
            return new Email(this);
        }

    }

}
