package com.buyexpressly.api.resource.merchant;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class PingResponse {
    private static final PingResponse SUCCESS = new PingResponse();

    private final String expressly = "Stuff is happening!";

    private PingResponse() {

    }

    public static PingResponse build() {
        return SUCCESS;
    }
}
