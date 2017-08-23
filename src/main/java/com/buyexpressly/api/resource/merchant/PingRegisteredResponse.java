package com.buyexpressly.api.resource.merchant;

import java.io.IOException;
import java.util.Properties;

public final class PingRegisteredResponse {
    private static final PingRegisteredResponse SUCCESS = new PingRegisteredResponse();

    private final boolean registered = true;
    private final String version = "V2";
    private final String lightbox = "javascript";
    private final String platformName = "Expressly Java SDK";
    private final String platformVersion;

    private PingRegisteredResponse() {
        platformVersion = getVersion();
    }

    public boolean isRegistered() {
        return registered;
    }

    public static PingRegisteredResponse build() {
        return SUCCESS;
    }

    private String getVersion() {
        try {
            Properties versionProperties = new Properties();
            versionProperties.load(PingRegisteredResponse.class.getClassLoader().getResourceAsStream("version.properties"));
            return versionProperties.getProperty("version");
        } catch (IOException ignore) {
            return "2.0.x";
        }
    }
}
