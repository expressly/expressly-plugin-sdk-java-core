package com.buyexpressly.api.resource.server;


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
