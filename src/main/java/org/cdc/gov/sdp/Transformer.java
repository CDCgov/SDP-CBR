package org.cdc.gov.sdp;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.google.gson.Gson;

public class Transformer implements Processor {
	
	public void process(Exchange exchange) throws Exception {
        Map myMap = exchange.getIn().getBody(Map.class);
        Message msg = exchange.getIn();
        msg.setBody(myMap.remove("payloadTextContent"));
        msg.setHeader(CBR.PAYLOAD,  msg.getBody());
        Gson gson = new Gson(); 
        msg.setHeader(CBR.SOURCE_ATTRIBUTES, gson.toJson(myMap));
        msg.setHeader(CBR.SOURCE, "PHINMS");
        msg.setHeader(CBR.SOURCE_ID, myMap.get("recordId"));
        msg.setHeader(CBR.ID, "PHINMS_"+myMap.get("recordId"));
        msg.setHeader(CBR.CBR_ID, "PHINMS_"+myMap.get("recordId"));
        msg.setHeader(CBR.BATCH, false); // setting default value, if batch will be handled later 
        msg.setHeader(CBR.BATCH_INDEX, 0);// setting default value, if batch will be handled later 
        msg.setHeader(CBR.SENDER, myMap.get("fromPartyId"));
        msg.setHeader(CBR.RECIPIENT, myMap.get("recipientId"));
        msg.setHeader(CBR.SOURCE_RECEIVED_TIME, myMap.get("receivedTime"));
        msg.setHeader(CBR.CBR_RECEIVED_TIME, new Date(System.currentTimeMillis()));    
    }
	
}

