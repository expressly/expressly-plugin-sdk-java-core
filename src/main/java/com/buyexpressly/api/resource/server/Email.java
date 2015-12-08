package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Email {
    private String alias;
    private String email;

    public String getAlias() {
        return alias;
    }

    public String getEmail() {
        return email;
    }
}
