package com.buyexpressly.api.resource.server;

import com.buyexpressly.api.util.Builders;

import java.util.Objects;

public final class Tuple {
    private String field;
    private String value;

    private Tuple() {
    }

    private Tuple(String field, String value) {
        Builders.required(field, "field in Tuple");
        Builders.required(value, "value in Tuple");
        this.field = field;
        this.value = value;
    }

    public static Tuple build(String field, String value) {
        return new Tuple(field, value);
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tuple tuple = (Tuple) o;
        return Objects.equals(field, tuple.field)
                && Objects.equals(value, tuple.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, value);
    }

    @Override
    public String toString() {
        return "field='" + field + '\'' +
                ", value='" + value + '\'';
    }
}
