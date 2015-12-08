package com.buyexpressly.api.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

public class DateTimeIso8601DateSerializer extends JsonSerializer<DateTime> {
    @Override
    public void serialize(DateTime value, JsonGenerator generator, SerializerProvider serializer) throws IOException {
        generator.writeString(ISODateTimeFormat.dateTimeNoMillis().print(value));
    }
}