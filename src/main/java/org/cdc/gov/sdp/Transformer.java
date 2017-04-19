package org.cdc.gov.sdp;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class Transformer implements Processor {
	
	public void process(Exchange exchange) throws Exception {
        Map myMap = exchange.getIn().getBody(Map.class);
        Message msg = exchange.getIn();
        msg.setBody(myMap.remove("payloadTextContent"));
        msg.setHeader(CBR.SOURCE_ATTRIBUTES, myMap);
        msg.setHeader(CBR.SOURCE, "PHINMS");
        msg.setHeader(CBR.SOURCE_ID, myMap.get("recordId"));
        msg.setHeader(CBR.ID, "PHINMS_"+myMap.get("recordId"));
        msg.setHeader(CBR.SENDER, myMap.get("fromPartyId"));
        msg.setHeader(CBR.RECIPIENT, myMap.get("recipientId"));
        msg.setHeader(CBR.SOURCE_RECEIVED_TIME, myMap.get("receivedTime"));
        msg.setHeader(CBR.CBR_RECEIVED_TIME, new Date(System.currentTimeMillis()));    
    }
}

