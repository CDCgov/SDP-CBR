package gov.cdc.sdp.cbr;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

public class PhinMSTransformer implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Map myMap = exchange.getIn().getBody(Map.class);
		Gson gson = new Gson();

		String recordId = myMap.get("recordId").toString();

		Message msg = exchange.getIn();
		msg.setBody(myMap.remove("payloadTextContent"));

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
