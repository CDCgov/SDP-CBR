package gov.cdc.sdp.cbr.log;

import javax.sql.DataSource;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

@UriEndpoint(scheme = "sdplog", title = "SDPLog", syntax = "sdplog:message", label = "database,sql,log")
public class LogEndpoint extends DefaultEndpoint {

	@UriParam(description = "Sets the DataSource to use to communicate with the database.")
	private DataSource dataSource;
	private String log_msg;

	public LogEndpoint(String uri, Component component, DataSource ds, String log_message) {
		super(uri, component);
		this.dataSource = ds;
		this.log_msg = log_message;
	}

	@Override
	public Producer createProducer() throws Exception {
		return new LogProducer(this, getEndpointUri(), dataSource, log_msg);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return null;
	}
}
