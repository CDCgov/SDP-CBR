package gov.cdc.sdp.cbr.appender;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

public class JmxNotifier extends NotificationBroadcasterSupport implements JmxNotifierMBean {

    public static final String OBJECT_NAME = String.format("%s:type=LogbackAppender", JmxNotifier.class.getName());
    
    public JmxNotifier() throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException  {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(this, new ObjectName(OBJECT_NAME));
    }
    
    public static ObjectName objectName() throws MalformedObjectNameException {
        return new ObjectName(OBJECT_NAME);
    }
    
    @Override
    public void sendNotification(Notification notification) {
        super.sendNotification(notification);
    }

	@Override
	public String getLog() {
		String text = "File Contents";
		try {
//			Path path = FileSystems.getDefault().getPath(".").toAbsolutePath();
//			text = path.toString();
			text = new String(Files.readAllBytes(Paths.get("./log_files/sdp_console.log")), StandardCharsets.UTF_8);
		} catch (Exception e) {

			text = "ERROR";
			e.printStackTrace();
		}
		return text;
	}
}
