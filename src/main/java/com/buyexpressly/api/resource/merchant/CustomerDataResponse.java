package com.buyexpressly.api.resource.merchant;

import com.buyexpressly.api.resource.server.Customer;
import com.buyexpressly.api.resource.server.Metadata;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class CustomerDataResponse {
    private Metadata meta;
    private Customer data;

    private CustomerDataResponse(Builder builder) {
        this.meta = builder.meta;
        this.data = builder.data;
    }

    private CustomerDataResponse() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Metadata getMeta() {
        return meta;
    }

    public Customer getData() {
        return data;
    }

    public static final class Builder {
        private Metadata meta;
        private Customer data;

        public Builder withMeta(Metadata meta) {
            this.meta = meta;
            return this;
        }

        public Builder withData(Customer data) {
            this.data = data;
            return this;
        }

        public CustomerDataResponse build() {
            return new CustomerDataResponse(this);
        }
    }

}
