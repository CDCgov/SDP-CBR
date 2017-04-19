package org.cdc.gov.sdp;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class JSONTransformer implements Processor {

	public void process(Exchange exchange){
		Message in = exchange.getIn();
		Map<String, Object> map = new HashMap<String, Object>();
//		map.putAll(in.getIn().getHeaders());
		map.put(CBR.PAYLOAD, in.getBody());
		map.put(CBR.SOURCE_ATTRIBUTES, in.getHeader(CBR.SOURCE_ATTRIBUTES));
		map.put(CBR.SOURCE, in.getHeader(CBR.SOURCE));
		map.put(CBR.SOURCE_ID, in.getHeader(CBR.SOURCE_ID));
		map.put(CBR.SENDER, in.getHeader(CBR.SENDER));
		map.put(CBR.RECIPIENT, in.getHeader(CBR.RECIPIENT));
		map.put(CBR.CBR_RECEIVED_TIME, in.getHeader(CBR.CBR_RECEIVED_TIME));
		in.setBody(map);
		
	}
}
