package com.rubenlaguna;


import ch.qos.logback.core.CoreConstants;
import static ch.qos.logback.core.encoder.JsonEscapeUtil.jsonEscapeString;



public class GoogleStructuredLoggingEncoder extends ch.qos.logback.classic.encoder.JsonEncoder {

    static int DEFAULT_SIZE = 1024; // JsonEncoder.DEFAULT_SIZE is can't be used from outside package ch.qost.logback.classic.encoder
    static int DEFAULT_SIZE_WITH_THROWABLE = DEFAULT_SIZE * 8;

    @Override
    public byte[] encode(ch.qos.logback.classic.spi.ILoggingEvent event) {
        // Add custom fields to the log event
        final int initialCapacity = event.getThrowableProxy() == null ? DEFAULT_SIZE : DEFAULT_SIZE_WITH_THROWABLE;
        StringBuilder sb = new StringBuilder(initialCapacity);
        sb.append('{'); // JsonEncoder.OPEN_OBJ is private so we can't use it here

        sb.append("\"timestampSeconds\": ");
        sb.append(Long.toString(event.getTimeStamp() / 1000));
        sb.append(",");
        sb.append("\"timestampNanos\": ");
        sb.append(Long.toString(event.getNanoseconds()));
        sb.append(",");
        
        sb.append("\"severity\": \"");
        sb.append(event.getLevel().toString());
        sb.append("\"");
        sb.append(",");

        sb.append("\"message\": \"");
        sb.append(jsonEscapeString(event.getFormattedMessage()));
        sb.append("\"");

        


        //AppenderMemberWithLongValue(sb, "timestampSeconds", event.getTimeStamp() / 1000); // AppenderMemberWithLongValues is private
        //sb.append(','); // JsonEncoder.VALUE_SEPARATOR is private so we can't use it here
        //AppenderMemberWithLongValue(sb, "timestampNanos", event.getNanoseconds());



        sb.append('}'); // JsonEncoder.CLOSE_OBJ is private so we can't use it here
        sb.append(CoreConstants.JSON_LINE_SEPARATOR);
        return sb.toString().getBytes(CoreConstants.UTF_8_CHARSET);
    }
}
