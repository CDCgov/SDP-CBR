package org.mycompany;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class Transformer implements Processor {
	public void process(Exchange exchange) throws Exception {
        Map myMap = exchange.getIn().getBody(Map.class);
        Message msg = exchange.getIn();
        msg.setHeaders(myMap);
        msg.setBody(myMap.get("payloadTextContent"));
        msg.setHeader("SOURCE", "PHINMS");
        msg.setHeader("SOURCE_ID", myMap.get("recordId"));
        msg.setHeader("ID", "PHINMS_"+myMap.get("recordId"));
   
    }
}

