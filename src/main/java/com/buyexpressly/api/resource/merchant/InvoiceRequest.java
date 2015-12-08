package com.buyexpressly.api.resource.merchant;

import com.buyexpressly.api.util.LocalDateIso8601DateSerializer;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.LocalDate;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class InvoiceRequest {
    private String email;
    @JsonSerialize(using = LocalDateIso8601DateSerializer.class)
    private LocalDate from;
    @JsonSerialize(using = LocalDateIso8601DateSerializer.class)
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
