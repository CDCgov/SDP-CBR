package gov.cdc.sdp.cbr;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * An aggregation strategy is a means for aggregating a set of received message.
 * The ArrayListAggregationStrategy creates a new exchange from a collection of
 * incoming messages.
 * 
 * Each incoming message can be in a success or an error state.  The 
 * ArrayListAggregationStrategy maintains a counter of total messages and a 
 * counter of messages in error.
 *
 */
public class ArrayListAggregationStrategy implements AggregationStrategy {

    public ArrayListAggregationStrategy() {
        super();
    }

    /**
     * The message invoked when this class is used as the aggregation strategy
     * for a route in camel.
     */
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