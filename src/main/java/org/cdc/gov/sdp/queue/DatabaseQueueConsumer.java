package org.cdc.gov.sdp.queue;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

public class DatabaseQueueConsumer extends DefaultConsumer {

	public DatabaseQueueConsumer(Endpoint endpoint, Processor processor) {
		super(endpoint, processor);
		// TODO Auto-generated constructor stub
	}

}
