package org.cdc.gov.sdp.queue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Component for handing messages within a defined database structure. The table
 * name and datasource to use are provided as parameters. Currently, the
 * remaining portion of the URI, after the component prefix, is not used.
 *
 * @author Betsy Cole
 */
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

	public String getCreateCommand(String tableName) {
		return ("CREATE TABLE IF NOT EXISTS " + tableName + " (id bigserial primary key,"
				+ "cbr_id varchar(255) NOT NULL, source varchar(255) NOT NULL, source_id varchar(255) NOT NULL,"
				+ "source_attributes text default NULL, batch boolean default false, batch_index int default 0,"
				+ "payload text NOT NULL, cbr_recevied_time varchar (255) NOT NULL, cbr_delivered_time varchar (255) default NULL,"
				+ "sender varchar (255) default NULL, recipient varchar (255) default NULL, errorCode varchar (255) default NULL,"
				+ "errorMessage varchar (255) default NULL, attempts int  default 0, status varchar (255) default 'queued',"
				+ "created_at varchar (255) default NULL, updated_at varchar (255) default NULL)");
	}

	public void createIfNotExists(DataSource ds, String tableName) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement(getCreateCommand(tableName));
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	protected Endpoint createEndpoint(String uri, String tableName, Map<String, Object> parameters) throws Exception {
		DataSource ds = resolveAndRemoveReferenceParameter(parameters, "dataSource", DataSource.class);
		if (ds == null) {
			throw new IllegalArgumentException("DataSource must be configured");
		}
		this.createIfNotExists(ds, tableName);

		DatabaseQueueEndpoint endpoint = new DatabaseQueueEndpoint(uri, this, ds, tableName);

		return endpoint;
	}
}
