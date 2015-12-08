package com.buyexpressly.api.resource.server;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;

@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Phone {
    private String type;
    private String number;
    private Integer countryCode;

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public Integer getCountryCode() {
        return countryCode;
    }
}
