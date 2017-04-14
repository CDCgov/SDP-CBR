package org.cdc.gov.sdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultMessage;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

public class HL7V2BatchSplitter {

	public List<Message> split(Exchange exchange) throws HL7Exception {
		return splitMessage(exchange.getIn());
	}

	public List splitMessage(org.apache.camel.Message inMessage) throws HL7Exception {
		String batch = inMessage.getBody(String.class);
		List<String> segments = Arrays.asList(batch.split("\r"));
		List messages = new ArrayList();
		int messageCount = 0;
		
		ListIterator<String> iter = segments.listIterator();

		while (iter.hasNext()) {
			String st = iter.next();
			
			if (st.trim().startsWith("MSH")) {
				try {
					messageCount++;
					String hl7 = createMessage(iter);
					DefaultMessage message = new DefaultMessage();
					message.setHeaders(inMessage.getHeaders());
					message.setHeader("BATCH", true);
					message.setHeader("BATCH_INDEX", messageCount);
					System.out.println(hl7);
					message.setBody(hl7);
					messages.add(message);
				} catch (HL7Exception e) {
					// need to log that we missed a message for some reason
					e.printStackTrace();
				}
			}
		}

		return messages;
	}

	private String createMessage(ListIterator<String> iter) throws HL7Exception {
		StringBuffer buff = new StringBuffer();
		while (iter.hasNext()) {
			String st = iter.next();
			if (st.trim().startsWith("BTS")) {
				break;
			}
			if(st.trim().startsWith("MSH")){
				iter.previous();
				break;
			}
			buff.append(st);
			buff.append("\r");
		}

		return buff.toString();
	}
}
