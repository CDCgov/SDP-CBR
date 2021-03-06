package gov.cdc.sdp.cbr;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.Date;
import java.util.Map;

public class PhinMSTransformer implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Map myMap = exchange.getIn().getBody(Map.class);

    Message msg = exchange.getIn();
    msg.setBody(myMap.remove("payloadTextContent"));

    SDPMessage sdpMessage = new SDPMessage();
    sdpMessage.setBatch(false);
    sdpMessage.setBatchId(null);
    sdpMessage.setBatchIndex(0);
    sdpMessage.setCbrReceivedTime(new Date(System.currentTimeMillis()).toString());

    String exchgSrc = exchange.getFromRouteId().toUpperCase();
    String recordId = myMap.get("recordId").toString();
    String cbrId = exchgSrc + "_" + recordId;    
    sdpMessage.setId(cbrId);
    sdpMessage.setPayload(msg.getBody().toString());
    sdpMessage.setRecipient((String) myMap.get("recipientId"));
    sdpMessage.setSender((String) myMap.get("fromPartyId"));
    sdpMessage.setSource(exchgSrc);
    sdpMessage.setSourceId(recordId);
    sdpMessage.setSourceReceivedTime((String) myMap.get("receivedTime"));
    sdpMessage.setSourceAttributes(myMap);

    Gson gson = new Gson();
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, gson.toJson(sdpMessage));
    msg.setHeader(CBR.CBR_ID, cbrId);
    msg.setHeader(CBR.ID, cbrId);
    msg.setHeader("recordId", recordId);

  }

}
