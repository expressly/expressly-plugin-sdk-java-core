package com.buyexpressly.api.resource.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Metadata {
    private String sender;
    private String locale;
    private List<Tuple> issuerData;

    private Metadata() {

    }

    private Metadata(String sender, String locale, List<Tuple> issuerData) {
        this.sender = sender;
        this.locale = locale;
        this.issuerData = issuerData;
    }

    public static Metadata build(String sender, String locale, Tuple... issuerData) {
        return new Metadata(
                sender == null ? "" : sender,
                locale == null ? "" : locale,
                issuerData == null ? new ArrayList<>() : Arrays.asList(issuerData));
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