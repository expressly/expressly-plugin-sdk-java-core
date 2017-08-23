package com.buyexpressly.api.resource.merchant;

import java.util.List;

public final class InvoiceListRequest {
    private List<InvoiceRequest> customers;

    private InvoiceListRequest() {

    }

    public List<InvoiceRequest> getCustomers() {
        return customers;
    }
}
