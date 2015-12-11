package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class MigrationResponse {
    private MigrationData migration;
    private CartData cart;

    private MigrationResponse() {

    }

    public MigrationData getMigrationData() {
        return migration;
    }

    public CartData getCartData() {
        return cart;
    }
}
