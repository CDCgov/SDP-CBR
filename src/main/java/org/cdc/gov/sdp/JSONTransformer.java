package org.cdc.gov.sdp;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.cdc.gov.sdp.model.SDPMessage;

import com.google.gson.Gson;

public class JSONTransformer implements Processor {

	public void process(Exchange exchange){
		Message in = exchange.getIn();
		in.setBody(new Gson().fromJson((String) in.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class));
	}
}
