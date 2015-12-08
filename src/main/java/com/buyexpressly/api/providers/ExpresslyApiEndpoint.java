package com.buyexpressly.api.providers;

public enum ExpresslyApiEndpoint {
    PING("admin/ping"),
    REGISTER("v2/plugin/merchant"),
    UNINSTALL("v2/plugin/merchant/{uuid}"),
    POPUP_HTML("v2/migration/{uuid}"),
    CUSTOMER("v2/migration/{uuid}/user"),
    CONFIRM_MIGRATION("v2/migration/{uuid}/success"),
    GET_BANNER("v2/banner/{merchantUuid}");

    private final String endpoint;

    ExpresslyApiEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
