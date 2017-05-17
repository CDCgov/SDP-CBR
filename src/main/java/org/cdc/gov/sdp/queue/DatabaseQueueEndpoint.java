package org.cdc.gov.sdp.queue;

import javax.sql.DataSource;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

@UriEndpoint(scheme = "sdpqueue", title = "SDPQueue", syntax = "sdpqueue", label = "database,sql,queue")
public class DatabaseQueueEndpoint extends DefaultEndpoint {

	@UriParam(description = "Sets the DataSource to use to communicate with the database.")
	private DataSource dataSource;
	@UriParam(description = "Sets the table in which the data will be stored.")
	private String tableName;

	public DatabaseQueueEndpoint(String uri, Component component, DataSource ds, String tableName) {
		super(uri, component);
		this.dataSource = ds;
		this.tableName = tableName;
	}

	@Override
	public Producer createProducer() throws Exception {
		return new DatabaseQueueProducer(this, getEndpointUri(), dataSource, tableName);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return new DatabaseQueueConsumer(this, processor);
	}
}
