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
    
    // Configuration parameter for time format
    private String timeFormat = "timeStampObject"; // default value
    
    /**
     * Sets the time format for log output.
     * @param timeFormat One of: "timeStampObject", "timestampFieldsTopLevel", "timeRFC3339"
     */
    public void setTimeFormat(String timeFormat) {
        if (timeFormat != null && 
            ("timeStampObject".equals(timeFormat) || 
             "timestampFieldsTopLevel".equals(timeFormat) || 
             "timeRFC3339".equals(timeFormat))) {
            this.timeFormat = timeFormat;
        } else {
            addWarn("Invalid timeFormat value: " + timeFormat + ". Using default: timeStampObject");
            this.timeFormat = "timeStampObject";
        }
    }
    
    /**
     * Gets the current time format setting.
     * @return the current time format
     */
    public String getTimeFormat() {
        return timeFormat;
    }
    
    /**
     * Writes time fields to the JSON output based on the configured timeFormat.
     * @param jg JsonGenerator to write to
     * @param event The logging event containing timestamp information
     * @throws IOException if writing fails
     */
    private void writeTimeFields(JsonGenerator jg, ILoggingEvent event) throws IOException {
        switch (timeFormat) {                
            case "timestampFieldsTopLevel":
                // GKE preference #2: timestampSeconds / timestampNanos at top level
                jg.writeFieldName("timestampSeconds");
                jg.writeNumber(event.getTimeStamp() / 1000);
                jg.writeFieldName("timestampNanos");
                jg.writeNumber(event.getNanoseconds());
                break;
                
            case "timeRFC3339":
                // GKE preference #3: time in RFC3339 format
                jg.writeFieldName("time");
                jg.writeString(formatter.format(event.getInstant()));
                break;
                
            case "timeStampObject":
                // GKE preference #1: timestamp.seconds / timestamp.nanos
            default:
                // Fallback to timestamp object format
                jg.writeFieldName("timestamp");
                jg.writeStartObject();
                jg.writeFieldName("seconds");
                jg.writeNumber(event.getTimeStamp() / 1000);
                jg.writeFieldName("nanos");
                jg.writeNumber(event.getNanoseconds());
                jg.writeEndObject();
                break;
        }
    }

    @Override
    public byte[] encode(ch.qos.logback.classic.spi.ILoggingEvent event) {
        if (!isStarted()) {
          throw new IllegalStateException("Encoder not started");
        }

        StringWriter sw = new StringWriter();
        try (JsonGenerator jg = jsonFactory.createGenerator(sw)) {
            jg.writeStartObject();

            // Generate time fields based on configuration
            writeTimeFields(jg, event);

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
