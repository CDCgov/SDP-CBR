package gov.cdc.sdp.cbr;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.Date;
import java.util.Map;

public class HTTP4Transformer implements Processor {
  /**
   * Converts the message into an HTTP multipart entity.
   */
  public void process(Exchange exchange) throws Exception {
    String[] headers = { CBR.SOURCE, CBR.SOURCE_ID, CBR.SOURCE_RECEIVED_TIME,
        CBR.BATCH, CBR.BATCH_INDEX, CBR.CBR_ID, CBR.SENDER, CBR.RECIPIENT,
        CBR.SOURCE_ATTRIBUTES, CBR.CBR_RECEIVED_TIME };
    MultipartEntityBuilder mpb = MultipartEntityBuilder.create();
    String file = exchange.getIn().getBody(String.class);
    Map<String, Object> sourceHeaders = exchange.getIn().getHeaders();

    for (String header : headers) {
      if (sourceHeaders.get(header) != null) {
        mpb.addTextBody(header, sourceHeaders.get(header).toString());
      }
    }
    mpb.addTextBody(CBR.CBR_DELIVERED_TIME, new Date(System.currentTimeMillis()).toString());
    mpb.addBinaryBody(CBR.PAYLOAD, file.getBytes());
    exchange.getIn().setBody(mpb.build());
  }
}