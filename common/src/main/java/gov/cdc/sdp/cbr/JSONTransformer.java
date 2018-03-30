package gov.cdc.sdp.cbr;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class JSONTransformer implements Processor {

  /**
   * Converts the message into JSON.
   */
  public void process(Exchange exchange) {
    Message in = exchange.getIn();
    in.setBody(new Gson().fromJson((String)
        in.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class));
  }
}
