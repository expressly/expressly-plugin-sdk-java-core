package com.buyexpressly.api.resource.merchant;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class PingRegisteredResponse {
    private static final PingRegisteredResponse SUCCESS = new PingRegisteredResponse();

    private final boolean registered = true;

    private PingRegisteredResponse() {

    }

    public static PingRegisteredResponse build() {
        return SUCCESS;
    }

}
