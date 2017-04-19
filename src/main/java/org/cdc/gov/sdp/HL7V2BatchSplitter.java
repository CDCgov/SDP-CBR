package org.cdc.gov.sdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;

import ca.uhn.hl7v2.HL7Exception;


public class HL7V2BatchSplitter {

	public List<Message> split(Exchange exchange) throws HL7Exception {
		System.out.println("CALLING SPLITTER WITH EXCHANGE");
		return splitMessage(exchange.getIn());
	}

	public List<Message> splitMessage(org.apache.camel.Message inMessage) {
		System.out.println("CALLING SPLITTER WITH MESSAGE");
		String batch = inMessage.getBody(String.class);
		List<String> segments = Arrays.asList(batch.split("\r|\r\n|\n"));
		System.out.println("Number of segments = "+ segments.size());
		List<Message> messages = new ArrayList<Message>();
		int messageCount = 0;
		
		ListIterator<String> iter = segments.listIterator();

		while (iter.hasNext()) {
			String st = iter.next();
			
			if (st.trim().startsWith("MSH")) {
				try {
					iter.previous();
					String hl7 = createMessage(iter);
					DefaultMessage message = new DefaultMessage();
					message.setHeaders(inMessage.getHeaders());
					message.setHeader(CBR.BATCH, true);
					message.setHeader(CBR.BATCH_INDEX, messageCount);
					System.out.println(hl7);
					message.setBody(hl7);
					messages.add(message);
				} catch (HL7Exception e) {
					// need to log that we missed a message for some reason
					e.printStackTrace();
				}
			}
		}
		System.out.println("RETURNING MESSAGES " + messages);
		return messages;
	}

	/* This method continues through an iterator looking for an end of batch header segment or the 
	 * start of a new message with the MSH header segment.  It is expected that 
	 */
	private String createMessage(ListIterator<String> iter) throws HL7Exception {
		StringBuffer buff = new StringBuffer();
		while (iter.hasNext()) {
			String st = iter.next();
			if (st.trim().startsWith("BTS")) {
				break;
			}
			if(buff.length() != 0 && st.trim().startsWith("MSH")){
				iter.previous();
				break;
			}
			buff.append(st);
			buff.append("\r");
		}

		return buff.toString();
	}
}
