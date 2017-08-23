package com.buyexpressly.api.resource.server;

public final class SuccessMessageResponse {
    private boolean success;
    private String msg;

    private SuccessMessageResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

}
