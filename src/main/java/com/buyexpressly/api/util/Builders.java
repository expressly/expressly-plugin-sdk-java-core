package com.buyexpressly.api.util;

import com.buyexpressly.api.resource.error.ExpresslyException;

public final class Builders {
    private static final int SECRET_KEY_LENGTH = 32;

    private Builders() {
        throw new ExpresslyException("Builders class cannot be instantiated");
    }

    public static void len(String value, String fieldName, int maxLength) {
        if (isNullOrEmpty(value)) {
            return;
        }
        validate(value.length() <= maxLength, String.format(
                "field [%s] max allowed length is [%s], actual length is [%s]",
                fieldName, maxLength, value.length()));
    }

    public static void pattern(String value, String fieldName, String pattern) {
        if (isNullOrEmpty(value)) {
            return;
        }
        validate(value.matches(pattern), String.format(
                "field [%s] value is invalid, should match pattern [%s]",
                fieldName, pattern));
    }

    public static <T> T required(T value, String fieldName) {
        if (value == null) {
            throw new ExpresslyException(String.format(
                    "field [%s] is required",
                    fieldName));
        }
        return value;
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static void validate(boolean condition, String message) {
        if (!condition) {
            throw new ExpresslyException(message == null ? "Validation condition failed" : message);
        }
    }

    public static <T> T requiresNonNull(T object, String message) {
        if (object == null) {
            throw new ExpresslyException(message);
        }
        return object;
    }

    public static void validateApiKey(String apiKey) {
        validate(!apiKey.isEmpty(), "invalid expressly api key");
        validate(EncodingUtils.fromBase64(apiKey).split(":").length == 2, "invalid expressly api key composition");
        pattern(EncodingUtils.fromBase64(apiKey).split(":")[0], "apiKey:[merchantUuid]", "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
        validate(EncodingUtils.fromBase64(apiKey).split(":")[1].length() == SECRET_KEY_LENGTH, "apiKey:[secretPassword] is not valid");
    }
}
