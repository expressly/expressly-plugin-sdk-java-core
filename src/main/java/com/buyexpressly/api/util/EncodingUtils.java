package com.buyexpressly.api.util;

import com.buyexpressly.api.resource.error.ExpresslyException;

import javax.xml.bind.DatatypeConverter;

public final class EncodingUtils {
    private static final String BASE64_PATTERN = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{4})$";

    private EncodingUtils() {
        throw new ExpresslyException("EncodingUtils cannot be instantiated");
    }

    public static String fromBase64(String token) {
        validateEncoding(token, "FieldToDecode");
        return new String(DatatypeConverter.parseBase64Binary(token));
    }

    public static String toBase64(String token) {
        return DatatypeConverter.printBase64Binary(token.getBytes());
    }

    static void validateEncoding(String token, String fieldName) {
        Builders.pattern(token, fieldName, BASE64_PATTERN);
    }
}
