package com.rubenlaguna;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class GoogleStructuredLoggingJacksonEncoder extends EncoderBase<ILoggingEvent> {

    private static JsonFactory jsonFactory = new JsonFactory();
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final java.time.format.DateTimeFormatter formatter =
            java.time.format.DateTimeFormatter.ISO_INSTANT;

    @Override
    public byte[] encode(ch.qos.logback.classic.spi.ILoggingEvent event) {
        if (!isStarted()) {
          throw new IllegalStateException("Encoder not started");
        }

        StringWriter sw = new StringWriter();
        try (JsonGenerator jg = jsonFactory.createGenerator(sw)) {
            jg.writeStartObject();


            // For GKE logging agent we only need one of the time-related fields.
            // I've include all of them for educational purposes.
            // In order of preference, GKE will read
            // 1. timestamp.seconds / timestamp.nanos if exists
            // 2. timestempSeconds / timestampNanos if exists
            // 3. time (RFC3339 format) if exists

            jg.writeFieldName("time");
            jg.writeString(formatter.format(event.getInstant()));

            jg.writeFieldName("timestamp");
            jg.writeStartObject();
            jg.writeFieldName("seconds");
            jg.writeNumber(event.getTimeStamp() / 1000);
            jg.writeFieldName("nanos");
            jg.writeNumber(event.getNanoseconds());
            jg.writeEndObject();

            jg.writeFieldName("timestampSeconds");
            jg.writeNumber(event.getTimeStamp() / 1000);

            jg.writeFieldName("timestampNanos");
            jg.writeNumber(event.getNanoseconds());

            jg.writeFieldName("severity");
            jg.writeString(event.getLevel().toString());

            jg.writeFieldName("message");
            jg.writeString(event.getFormattedMessage());

            jg.writeEndObject();
            jg.flush();
            jg.close();
            // return "{}".getBytes(StandardCharsets.UTF_8);
            sw.write('\n');
            return sw.toString().getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            addWarn("Error encountered while encoding log event. Event: " + event, e);
            return EMPTY_BYTES;
        }
    }

    @Override
    public byte[] headerBytes() {
        return EMPTY_BYTES;
    }

    @Override
    public byte[] footerBytes() {
        return EMPTY_BYTES;
    }
}
