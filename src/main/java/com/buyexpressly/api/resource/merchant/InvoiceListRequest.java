package com.buyexpressly.api.resource.merchant;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class InvoiceListRequest {
    private List<InvoiceRequest> customers;

    private InvoiceListRequest() {

    }

    public List<InvoiceRequest> getCustomers() {
        return customers;
    }
}
