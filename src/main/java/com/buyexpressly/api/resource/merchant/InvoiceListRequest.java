package com.buyexpressly.api.resource.merchant;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class InvoiceListRequest {
    private List<InvoiceRequest> customers;

    private InvoiceListRequest() {

    }

    public InvoiceListRequest(List<InvoiceRequest> customers) {
        this.customers = customers;
    }

    public List<InvoiceRequest> getCustomers() {
        return customers;
    }
}
