package gov.cdc.sdp.cbr;

import io.netty.handler.codec.http.multipart.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Message;
import org.apache.camel.component.netty4.http.NettyHttpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MultipartTransformer2 implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultipartTransformer2.class);

    private Message msg; 
    
    @Override
    public void process(Exchange exchange) throws Exception {
    	msg = exchange.getIn();
    	
        // get netty message
        NettyHttpMessage nettyHttpMessage = exchange.getIn(NettyHttpMessage.class);
        // use HttpPostRequestDecoder to extract form data
        HttpPostRequestDecoder postRequest = new HttpPostRequestDecoder(nettyHttpMessage.getHttpRequest());
        getHttpDataAttributes(postRequest);
        // return status to client
        //exchange.getIn().setBody(new ResponseWrapper(ResponseStatus.Success));
    }
    public void getHttpDataAttributes(HttpPostRequestDecoder request) {
        //ResponseWrapper result = new ResponseWrapper();
        try {
            for (InterfaceHttpData part : request.getBodyHttpDatas()) {
                if (part instanceof MixedAttribute) {
                    Attribute attribute = (MixedAttribute) part;
                    LOGGER.info(String.format("Found part with key: %s and value: %s ", attribute.getName(), attribute.getValue()));
                } else if (part instanceof MixedFileUpload) {
                    MixedFileUpload attribute = (MixedFileUpload) part;
                    LOGGER.info(String.format("Found part with key: %s and value: %s ", attribute.getName(), attribute.getFilename()));
                    msg.setBody(attribute.getString());
                    break;
                }
            }
        } catch (IOException e) {
            String errorMsg = String.format("Cannot parse request");
            //result.setMessage(errorMsg);
            LOGGER.error(errorMsg,e);
        } finally {
            request.destroy();
        }
    }
}
