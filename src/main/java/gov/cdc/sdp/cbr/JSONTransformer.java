package gov.cdc.sdp.cbr;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

public class JSONTransformer implements Processor {

	public void process(Exchange exchange){
		Message in = exchange.getIn();
		in.setBody(new Gson().fromJson((String) in.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class));
	}
}
