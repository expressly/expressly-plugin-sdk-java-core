package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.ObjectMapperFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public final class ExpresslyQuery extends StringEntity {

    private ExpresslyQuery(String string) throws UnsupportedEncodingException {
        super(string, ContentType.APPLICATION_JSON);
    }

    public static  <T> ExpresslyQuery toJsonEntity(T request) throws IOException {
        String json = ObjectMapperFactory.make().writer().writeValueAsString(request);
        return new ExpresslyQuery(json);
    }
}
