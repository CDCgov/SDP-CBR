package gov.cdc.sdp.cbr;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class EmailHeaders implements Processor {

	private String subject = "ERROR";
	private String from = "khennessy@mitre.org";
	private String to = "khennessy@mitre.org";

	@Override
	public void process(Exchange exchange) {
		Message in = exchange.getIn();
		in.setHeader("to", to);
		in.setHeader("from", from);
		in.setHeader("subject", subject);
	}
}
