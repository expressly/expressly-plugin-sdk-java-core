package com.buyexpressly.api.resource.merchant;

public final class PingRegisteredResponse {
    private static final PingRegisteredResponse SUCCESS = new PingRegisteredResponse();

    private final boolean registered = true;
    private final String version = "V2";
    private final String lightbox = "javascript";
    private String platformName = "Expressly Java SDK";
    private String platformVersion = "2.7.0";

    private PingRegisteredResponse() {
    }

    public boolean isRegistered() {
        return registered;
    }

    public static PingRegisteredResponse build() {
        return SUCCESS;
    }

}
