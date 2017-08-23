package com.buyexpressly.api.resource.merchant;

import org.joda.time.LocalDate;

public final class InvoiceRequest {
    private String email;
    private LocalDate from;
    private LocalDate to;

    private InvoiceRequest() {
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }
}
