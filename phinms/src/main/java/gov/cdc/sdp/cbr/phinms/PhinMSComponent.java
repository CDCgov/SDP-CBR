package gov.cdc.sdp.cbr.phinms;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import javax.sql.DataSource;

public class PhinMSComponent extends UriEndpointComponent {
  private static final Logger LOG = LoggerFactory.getLogger(PhinMSComponent.class);

  public PhinMSComponent() {
    super(PhinMSEndpoint.class);
  }

  public PhinMSComponent(Class<? extends Endpoint> endpointClass) {
    super(endpointClass);
  }

  public PhinMSComponent(CamelContext context) {
    super(context, PhinMSEndpoint.class);
  }

  public PhinMSComponent(CamelContext context, Class<? extends Endpoint> endpointClass) {
    super(context, endpointClass);
  }

  @Override
  protected Endpoint createEndpoint(String uri, String tableName, Map<String, Object> parameters) throws Exception {
    DataSource ds = resolveAndRemoveReferenceParameter(parameters, "dataSource", DataSource.class);

    if (ds == null) {
      LOG.error("DataSource must be configured");
      throw new IllegalArgumentException("DataSource must be configured");
    }
    PhinMSEndpoint endpoint = new PhinMSEndpoint(uri, this, ds, tableName);
    return endpoint;
  }
}
