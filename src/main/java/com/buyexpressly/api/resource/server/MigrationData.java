package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MigrationData {
    private Metadata meta;
    private Customer data;

    public Metadata getMeta() {
        return meta;
    }

    public void setMeta(Metadata meta) {
        this.meta = meta;
    }

    public Customer getData() {
        return data;
    }

    public void setData(Customer data) {
        this.data = data;
    }
}
