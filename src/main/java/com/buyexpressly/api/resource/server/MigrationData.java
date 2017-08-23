package com.buyexpressly.api.resource.server;

public final class MigrationData {
    private Metadata meta;
    private Customer data;

    private MigrationData() {
    }

    public Metadata getMeta() {
        return meta;
    }

    public Customer getData() {
        return data;
    }

}
