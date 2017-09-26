package gov.cdc.sdp.cbr;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class ArrayListAggregationStrategy implements AggregationStrategy {

	public ArrayListAggregationStrategy() {
		super();
	}

	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Message newIn = newExchange.getIn();
		Object newBody = newIn.getBody();
		Exchange returnExchange = oldExchange;
		ArrayList list = null;
		if (oldExchange == null) {
			list = new ArrayList();
			list.add(newBody);
			newIn.setBody(list);
			returnExchange = newExchange;
		} else {
			Message in = oldExchange.getIn();
			list = in.getBody(ArrayList.class);
			list.add(newBody);
		}

		Message msg = returnExchange.getIn();
		Integer totalMsgs = msg.getHeader("MSG_COUNT", Integer.class);
		Integer totalErrors = msg.getHeader("ERROR_COUNT", Integer.class);
		if (totalMsgs == null) {
			totalMsgs = new Integer(0);
		}
		if (totalErrors == null) {
			totalErrors = new Integer(0);
			msg.setHeader("ERROR_COUNT", totalErrors);
		}

		msg.setHeader("MSG_COUNT", new Integer(totalMsgs + 1));

		if (newExchange.getException() != null) {
			msg.setHeader("ERROR_COUNT", new Integer(totalErrors + 1));
		}
		return returnExchange;
	}

}