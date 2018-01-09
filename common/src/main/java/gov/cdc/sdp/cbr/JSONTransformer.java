package gov.cdc.sdp.cbr;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

/**
 * Given a message with a JSON body, converts it into an SDPMessage object
 * for internal processing.  Not currently used or tested -- should be
 * revisited and revised before use.
 * 
 */
@Deprecated
public class JSONTransformer implements Processor {

    public void process(Exchange exchange) {
        Message in = exchange.getIn();
        in.setBody(new Gson().fromJson((String) in.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class));
    }
}
