package gov.cdc.sdp.cbr.appender;

import java.util.concurrent.atomic.AtomicLong;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEventVO;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;


public class JmxNotificationAppender extends AppenderBase<ILoggingEvent> {

    private AtomicLong seqCounter = new AtomicLong(1);
    private Layout<ILoggingEvent> layout;
    
    //private JmxNotificationPublisher jmxNotificationPublisher;
    JmxNotifier jmxNotifier;
    
    public JmxNotificationAppender() throws Exception {
    	System.out.println("Starting Appender");
    	jmxNotifier = new JmxNotifier();
    }
    
    @Override
    public void start() {
        try {
            super.start();
            super.addInfo(String.format("Appender [%s] started", super.name));
        }
        catch(Exception e) {
            addError(String.format("Unable to start Appender [%s]", super.name), e);
        } 
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent event) {
				LoggingEventVO vo = LoggingEventVO.build(event);
				jmxNotifier.sendNotification(
						new NotificationBuilder()
							.type(vo.getLevel().toString())
							.source(vo.getLoggerName())
							.sequencsNumber(this.seqCounter.getAndIncrement())
							.timestamp(vo.getTimeStamp())
							.message(this.layout.doLayout(event))
							.stacktrace(event.getThrowableProxy())
							.build());     
    }
    
    public Layout<ILoggingEvent> getLayout() {
        return this.layout;
    }
    
    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }
}
