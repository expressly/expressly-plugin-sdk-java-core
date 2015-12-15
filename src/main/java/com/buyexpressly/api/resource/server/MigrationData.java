package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
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
