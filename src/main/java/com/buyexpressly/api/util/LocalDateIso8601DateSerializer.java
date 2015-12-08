package com.buyexpressly.api.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.LocalDate;

import java.io.IOException;

public class LocalDateIso8601DateSerializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider serializer) throws IOException {
        generator.writeString(value.toString());
    }
}