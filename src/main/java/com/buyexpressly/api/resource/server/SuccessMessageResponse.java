package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SuccessMessageResponse {
    private boolean success;
    private String msg;

    public SuccessMessageResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

}
