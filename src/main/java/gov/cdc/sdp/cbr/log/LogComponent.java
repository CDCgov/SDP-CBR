package gov.cdc.sdp.cbr.log;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogComponent extends UriEndpointComponent {
	private static final Logger LOG = LoggerFactory.getLogger(LogComponent.class);

	public LogComponent() {
		super(LogEndpoint.class);
	}

	public LogComponent(Class<? extends Endpoint> endpointClass) {
		super(endpointClass);
	}

	public LogComponent(CamelContext context) {
		super(context, LogEndpoint.class);
	}

	public LogComponent(CamelContext context, Class<? extends Endpoint> endpointClass) {
		super(context, endpointClass);
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		DataSource ds = resolveAndRemoveReferenceParameter(parameters, "dataSource", DataSource.class);

		if (ds == null) {
			LOG.error("Datasource must be configured");
			throw new IllegalArgumentException("DataSource must be configured");
		}
		LogEndpoint endpoint = new LogEndpoint(uri, this, ds, remaining);
		return endpoint;
	}
}
