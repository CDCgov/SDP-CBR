package org.cdc.gov.sdp;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class JSONTransformer implements Processor {

	public void process(Exchange in){
		Map<String, Object> map = new HashMap<String, Object>();
//		map.putAll(in.getIn().getHeaders());
		map.put("BODY", in.getIn().getBody());
		map.put("SOURCE_HEADERS", in.getIn().getHeader("SOURCE_HEADERS"));
		map.put("SOURCE", in.getIn().getHeader("SOURCE"));
		map.put("SOURCE_ID", in.getIn().getHeader("SOURCE_ID"));
		map.put("SENDER", in.getIn().getHeader("SENDER"));
		map.put("RECIPIENT", in.getIn().getHeader("RECIPIENT"));
		
		in.getIn().setBody(map);
		
	}
}
