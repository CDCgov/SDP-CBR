package org.cdc.gov.sdp.queue;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

public class DatabaseQueueComponent extends UriEndpointComponent {

	public DatabaseQueueComponent() {
		super(DatabaseQueueEndpoint.class);
	}

	public DatabaseQueueComponent(Class<? extends Endpoint> endpointClass) {
		super(endpointClass);
	}

	public DatabaseQueueComponent(CamelContext context) {
		super(context, DatabaseQueueEndpoint.class);
	}

	public DatabaseQueueComponent(CamelContext context, Class<? extends Endpoint> endpointClass) {
		super(context, endpointClass);
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		DataSource ds = resolveAndRemoveReferenceParameter(parameters, "dataSource", DataSource.class);
		String tableName = getAndRemoveParameter(parameters, "tableName", String.class);
//		if (ds == null) {
//			throw new IllegalArgumentException("DataSource must be configured");
//		}
		DatabaseQueueEndpoint endpoint = new DatabaseQueueEndpoint(uri, this, ds, tableName, remaining);
		return endpoint;
	}
}
