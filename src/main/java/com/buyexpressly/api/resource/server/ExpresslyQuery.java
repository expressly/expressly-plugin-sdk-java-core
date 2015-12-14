package com.buyexpressly.api.resource.server;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ExpresslyQuery extends StringEntity {

    public ExpresslyQuery(String string) throws UnsupportedEncodingException {
        super(string, ContentType.APPLICATION_JSON);
    }

    public static  <T> ExpresslyQuery toJsonEntity(T request) throws IOException {
        String json = new ObjectMapper().writer().writeValueAsString(request);
        return new ExpresslyQuery(json);
    }
}
