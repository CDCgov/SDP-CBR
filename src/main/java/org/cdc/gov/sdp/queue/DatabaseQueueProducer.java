package org.cdc.gov.sdp.queue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.cdc.gov.sdp.CBR;

public class DatabaseQueueProducer extends DefaultProducer {

	public DatabaseQueueProducer(Endpoint endpoint, String uri, DataSource ds, String msg) {
		super(endpoint);
	}

	@Override
	public DatabaseQueueEndpoint getEndpoint() {
		return (DatabaseQueueEndpoint) super.getEndpoint();
	}

	@Override
	public void process(final Exchange exchange) throws Exception {
		
	}
}
