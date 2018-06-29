package gov.cdc.sdp.cbr.appender;

import javax.management.Notification;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

public class NotificationBuilder {
    private static final String NL = System.getProperty("line.separator");
    
    private String type;
    private Object source;
    private long sequenceNumber;
    private long timestamp;
    private String message;
    private String stacktrace;
    
    public NotificationBuilder() {
    }
    
    public NotificationBuilder type(String value) {
        this.type = value;
        return this;
    }

    public NotificationBuilder source(String value) {
        this.source = value;
        return this;
    }
    
    public NotificationBuilder sequencsNumber(long value) {
        this.sequenceNumber = value;
        return this;
    }
    
    public NotificationBuilder timestamp(long value) {
        this.timestamp = value;
        return this;
    }
    
    public NotificationBuilder message(String value) {
        this.message = value;
        return this;
    }

    public NotificationBuilder stacktrace(IThrowableProxy iThrowableProxy) {
        StringBuilder buf = new StringBuilder();
        if (iThrowableProxy != null) {
            for (StackTraceElementProxy s : iThrowableProxy.getStackTraceElementProxyArray()) {
                buf.append(s.getSTEAsString());
                buf.append(NL);
            }
        }
        this.stacktrace = buf.length() > 0 ? buf.toString() : null;
        return this;
    }
    
    public Notification build() {
        Notification notification = new Notification(this.type, 
                this.source, 
                this.sequenceNumber, 
                this.timestamp, 
                this.message);
        if (this.stacktrace != null) {
            notification.setUserData(this.stacktrace);
        }
        return notification;
    }
}
