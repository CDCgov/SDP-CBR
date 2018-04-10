package gov.cdc.sdp.cbr;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

public class GenericTransformer implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {
    Message inMsg = exchange.getIn();

    MultipartFile body = (MultipartFile) inMsg.getBody();

    String bodyString = new String(body.getBytes());

    // TODO: Reconcile exchange id CBR_ID (See PhinMSTransformer) with CBR_ID from
    // REST api.
    // String exchg_src = exchange.getFromRouteId().toUpperCase();
    String sourceId = (String) inMsg.getHeader("sourceId");
    String cbrId = (String) inMsg.getHeader("CBR_ID");

    SDPMessage sdpMessage = new SDPMessage();
    sdpMessage.setBatch(false);
    sdpMessage.setBatchId(null);
    sdpMessage.setBatchIndex(0);
    sdpMessage.setCbrReceivedTime(new Date(System.currentTimeMillis()).toString());
    sdpMessage.setId(cbrId);
    sdpMessage.setPayload(bodyString);
    sdpMessage.setSourceId(sourceId);
    sdpMessage.setSourceAttributes((Map) inMsg.getHeader("METADATA"));

    inMsg.setBody(bodyString);

    Gson gson = new Gson();
    inMsg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, gson.toJson(sdpMessage));
    inMsg.setHeader(CBR.CBR_ID, cbrId);
    inMsg.setHeader(CBR.ID, cbrId);
    inMsg.setHeader("sourceId", sourceId);
  }

}
