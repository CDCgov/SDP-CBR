package gov.cdc.sdp.cbr;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

/**
 * Transformer that populates an SDPMessage object and necessary headers based
 * on the headers and content of an incoming Exchange with a MultipartFile as
 * a body.
 */
public class GenericTransformer implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Gson gson = new Gson();
        
        Message inMsg = exchange.getIn();

        MultipartFile body = (MultipartFile) inMsg.getBody();
        
        String bodyString = new String(body.getBytes());

        // TODO: Reconcile exchange id CBR_ID (See PhinMSTransformer) with CBR_ID from REST api.      
        // String exchg_src = exchange.getFromRouteId().toUpperCase();
        String sourceId = (String) inMsg.getHeader("sourceId");
        String cbr_id = (String) inMsg.getHeader("CBR_ID");

        SDPMessage sdpMessage = new SDPMessage();
        sdpMessage.setBatch(false);
        sdpMessage.setBatchId(null);
        sdpMessage.setBatchIndex(0);
        sdpMessage.setCbrReceivedTime(new Date(System.currentTimeMillis()).toString());
        sdpMessage.setId(cbr_id);
        sdpMessage.setPayload(bodyString);
        sdpMessage.setSourceId(sourceId);
        sdpMessage.setSourceAttributes((Map)inMsg.getHeader("METADATA"));
        
        inMsg.setBody(bodyString);

        inMsg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, gson.toJson(sdpMessage));
        inMsg.setHeader(CBR.CBR_ID, cbr_id);
        inMsg.setHeader(CBR.ID, cbr_id);
        inMsg.setHeader("sourceId", sourceId);
    }

}
