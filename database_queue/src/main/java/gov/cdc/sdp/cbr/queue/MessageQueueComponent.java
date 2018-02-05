package gov.cdc.sdp.cbr.queue;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageQueueComponent extends UriEndpointComponent {
	private static final Logger LOG = LoggerFactory.getLogger(MessageQueueComponent.class);
	
	public MessageQueueComponent() {
        super(MessageQueueEndpoint.class);
    }

    public MessageQueueComponent(Class<? extends Endpoint> endpointClass) {
        super(endpointClass);
    }

    public MessageQueueComponent(CamelContext context) {
        super(context, MessageQueueEndpoint.class);
    }

    public MessageQueueComponent(CamelContext context, Class<? extends Endpoint> endpointClass) {
        super(context, endpointClass);
    }
    
	@Override
    protected Endpoint createEndpoint(String uri, String tableName, Map<String, Object> parameters) throws Exception {
        DataSource ds = resolveAndRemoveReferenceParameter(parameters, "dataSource", DataSource.class);
        int delay = this.getAndRemoveParameter(parameters, "delay", Integer.class, 1);
        int initialDelay = this.getAndRemoveParameter(parameters, "initialDelay", Integer.class, 1);
        int limit = this.getAndRemoveParameter(parameters, "limit", Integer.class, 100);
        int maxAttempts = this.getAndRemoveParameter(parameters, "maxAttempts", Integer.class, 3);

        if (ds == null) {
            LOG.error("DataSource must be configured");
            throw new IllegalArgumentException("DataSource must be configured");
        }
        //this.createIfNotExists(ds, tableName);

        MessageQueueEndpoint endpoint = new MessageQueueEndpoint(uri, this, ds, tableName, delay, initialDelay, limit,
                maxAttempts);

        return endpoint;
    }
}
