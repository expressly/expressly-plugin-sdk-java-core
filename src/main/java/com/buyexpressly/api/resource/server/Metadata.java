package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.util.List;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Metadata {
    private String sender;
    private String locale;
    private List<Tuple> issuerData;

    private Metadata() {

    }

    public Metadata(String sender, String locale, List<Tuple> issuerData) {
        this.sender = sender;
        this.locale = locale;
        this.issuerData = issuerData;
    }

    public String getSender() {
        return sender;
    }

    public String getLocale() {
        return locale;
    }

    public List<Tuple> getIssuerData() {
        return issuerData;
    }
}
