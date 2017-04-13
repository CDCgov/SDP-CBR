package org.cdc.gov.sdp;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class AIMSHeaderProcessor implements Processor {

	public void process(Exchange in){

		Map<String,String> aimsHeaders = new HashMap<String,String>();
		in.getIn().setHeader("CamelAwsS3Headers", aimsHeaders);
//		aimsHeaders.put("AIMSPlatformSender",(String)in.getIn().getHeader("SENDER")); 
//		aimsHeaders.put("AIMSPlatformRecipient",(String)in.getIn().getHeader("RECIPIENT")); 
//		aimsHeaders.put("AIMSPlatformSenderProject",(String)in.getIn().getHeader("PROJECT")); 
//		aimsHeaders.put("AIMSPlatformSenderProtocol",""); 
//		aimsHeaders.put("AIMSPlatformSenderEncryptionType",""); 
//		aimsHeaders.put("AIMSPlatformMessageId",""); 
//		aimsHeaders.put("AIMSPlatformFilename",""); 
	}
}
