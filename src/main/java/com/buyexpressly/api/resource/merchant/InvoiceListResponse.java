package com.buyexpressly.api.resource.merchant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InvoiceListResponse {
    private final List<InvoiceResponse> invoices;

    private InvoiceListResponse(Builder builder) {
        this.invoices = Collections.unmodifiableList(builder.invoices);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<InvoiceResponse> getInvoices() {
        return invoices;
    }

    public static final class Builder {
        private final List<InvoiceResponse> invoices = new ArrayList<>();

        public Builder add(InvoiceResponse invoice) {
            invoices.add(invoice);
            return this;
        }

        public Builder addAll(List<InvoiceResponse> retrieved) {
            invoices.addAll(retrieved);
            return this;
        }

        public InvoiceListResponse build() {
            return new InvoiceListResponse(this);
        }
    }
}
