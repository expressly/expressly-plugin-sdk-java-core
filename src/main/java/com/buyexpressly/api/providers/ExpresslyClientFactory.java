package com.buyexpressly.api.providers;

import com.buyexpressly.api.util.Builders;

import java.util.Arrays;
import java.util.List;

class ExpresslyClientFactory {
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST", "DELETE");
    private final String expresslyEndpoint;
    private final String expresslyApiKey;

    ExpresslyClientFactory(String expresslyEndpoint, String expresslyApiKey) {
        Builders.pattern(expresslyEndpoint, "expresslyEndpoint", "http(s)?://.+");
        Builders.validateApiKey(expresslyApiKey);
        this.expresslyEndpoint = expresslyEndpoint;
        this.expresslyApiKey = expresslyApiKey;
    }

    ExpresslyHttpClient makeClient(String methodName, String endpoint) {
        Builders.validate(ALLOWED_METHODS.contains(methodName), "Illegal request method");
        return new ExpresslyHttpClient(methodName, endpoint, expresslyApiKey, expresslyEndpoint);
    }
}