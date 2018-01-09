package gov.cdc.sdp.cbr;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/*
 * This class was initially used to transform a message for sending outside of
 * CBR.  We do not currently have any mechanic that sends a message via HTTP.
 * 
 * If this functionality is re-enabled, this class should be revisited and 
 * revised -- it is not currently integrated properly with the rest of the
 * code base.
 */
@Deprecated
public class HTTP4Transformer implements Processor {
    public void process(Exchange exchange) throws Exception {
        String[] headers = { CBR.SOURCE, CBR.SOURCE_ID, CBR.SOURCE_RECEIVED_TIME, CBR.BATCH, CBR.BATCH_INDEX,
                CBR.CBR_ID, CBR.SENDER, CBR.RECIPIENT, CBR.SOURCE_ATTRIBUTES, CBR.CBR_RECEIVED_TIME };
        MultipartEntityBuilder mpb = MultipartEntityBuilder.create();
        String file = exchange.getIn().getBody(String.class);
        Map<String, Object> source_headers = exchange.getIn().getHeaders();

        for (String h : headers) {
            if (source_headers.get(h) != null) {
                mpb.addTextBody(h, source_headers.get(h).toString());
            }
        }
        mpb.addTextBody(CBR.CBR_DELIVERED_TIME, new Date(System.currentTimeMillis()).toString());
        mpb.addBinaryBody(CBR.PAYLOAD, file.getBytes());
        exchange.getIn().setBody(mpb.build());
    }
}