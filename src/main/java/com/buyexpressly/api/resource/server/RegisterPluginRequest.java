package com.buyexpressly.api.resource.server;


import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

import java.io.UnsupportedEncodingException;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RegisterPluginRequest {
    private String apiKey;
    private String apiBaseUrl;
    private String pluginVersion;

    public RegisterPluginRequest() throws UnsupportedEncodingException {
    }

    public RegisterPluginRequest(String apiKey, String apiBaseUrl, String pluginVersion) throws UnsupportedEncodingException {
        this.apiKey = apiKey;
        this.apiBaseUrl = apiBaseUrl;
        this.pluginVersion = pluginVersion;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }



}