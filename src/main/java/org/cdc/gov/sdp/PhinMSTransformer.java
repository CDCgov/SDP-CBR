package org.cdc.gov.sdp;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.cdc.gov.sdp.model.SDPMessage;

import com.google.gson.Gson;

public class PhinMSTransformer implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Map myMap = exchange.getIn().getBody(Map.class);
		Gson gson = new Gson();

		String recordId = myMap.get("recordId").toString();

		Message msg = exchange.getIn();
		myMap.remove("payloadTextContent");
		msg.setBody(myMap);

		SDPMessage sdpMessage = new SDPMessage();
		sdpMessage.setBatch(false);
		sdpMessage.setBatchId(null);
		sdpMessage.setBatchIndex(0);
		sdpMessage.setCbrReceivedTime(new Date(System.currentTimeMillis()).toString());
		sdpMessage.setId("PHINMS_" + recordId);
		sdpMessage.setPayload(msg.getBody().toString());
		sdpMessage.setRecipient((String) myMap.get("recipientId"));
		sdpMessage.setSender((String) myMap.get("fromPartyId"));
		sdpMessage.setSource("PHINMS");
		sdpMessage.setSourceId(recordId);
		sdpMessage.setSourceReceivedTime((String) myMap.get("receivedTime"));
		sdpMessage.setSourceAttributes(myMap);

		msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, gson.toJson(sdpMessage));
		msg.setHeader(CBR.CBR_ID, "PHINMS_" + recordId);
		msg.setHeader("recordId", recordId);

	}

}
