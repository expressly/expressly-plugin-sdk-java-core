package com.buyexpressly.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MerchantServiceRoute {
    PING("/expressly/api/ping", "GET", false),
    DISPLAY_POPUP("/expressly/api/(?<campaignCustomerUuid>[A-z0-9\\-]{36})", "GET", true),
    GET_CUSTOMER("/expressly/api/user/(?<email>.+@.+)", "GET", true),
    GET_INVOICES("/expressly/api/batch/invoice", "POST", true),
    CHECK_EMAILS("/expressly/api/batch/customer", "POST", true),
    PING_REGISTERED("/expressly/api/registered", "GET", true),
    CONFIRM_MIGRATION("/expressly/api/(?<campaignCustomerUuid>[A-z0-9\\-]{36})/migrate", "GET", true);

    private final Pattern uriRegex;
    private final String httpMethod;
    private final boolean authenticated;
    private final Set<String> uriParameterNames;

    MerchantServiceRoute(String uriRegex, String httpMethod, boolean authenticated) {
        this.uriRegex = Pattern.compile(uriRegex);
        this.httpMethod = httpMethod;
        this.authenticated = authenticated;
        this.uriParameterNames = getUriParameterNames(uriRegex);
    }

    public Pattern getUriRegex() {
        return uriRegex;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public Map<String, String> getUriParameters(String uri) {
        if (uriParameterNames.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> parameterMap = new HashMap<>(uriParameterNames.size());
        Matcher matcher = uriRegex.matcher(uri);
        if (matcher.find()) {
            for (String name : uriParameterNames) {
                parameterMap.put(name, matcher.group(name));
            }
        }
        return parameterMap;
    }

    private boolean matches(String uri) {
        return uriRegex.matcher(uri).matches();
    }

    public static MerchantServiceRoute findRoute(String httpMethod, String uri) {
        for (MerchantServiceRoute route : values()) {
            if (route.getHttpMethod().equalsIgnoreCase(httpMethod) && route.matches(uri)) {
                return route;
            }
        }

        throw new IllegalArgumentException(String.format(
                "invalid expressly merchant route, httpMethod=%s, uri=%s",
                httpMethod,
                uri));
    }

    private static Set<String> getUriParameterNames(String uriRegex) {
        Set<String> names = null;

        Matcher matcher = Pattern.compile("\\?\\<([^>]*)\\>").matcher(uriRegex);
        while (matcher.find()) {
            if (names == null) {
                names = new HashSet<>();
            }
            names.add(matcher.group(1));
        }

        return names == null ? Collections.<String>emptySet() : Collections.unmodifiableSet(names);
    }
}
