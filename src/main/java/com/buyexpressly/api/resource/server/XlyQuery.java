package com.buyexpressly.api.resource.server;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class XlyQuery extends StringEntity {

    public XlyQuery(String string) throws UnsupportedEncodingException {
        super(string, ContentType.APPLICATION_JSON);
    }

    public static  <T> XlyQuery toJsonEntity(T request) throws IOException {
        ObjectMapper om = new ObjectMapper();
        String json = new ObjectMapper().writer().writeValueAsString(request);
        return new XlyQuery(json);
    }
}
