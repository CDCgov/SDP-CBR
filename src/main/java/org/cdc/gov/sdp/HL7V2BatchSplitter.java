package org.cdc.gov.sdp;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultMessage;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.util.MessageIterator;

public class HL7V2BatchSplitter {

	public List<Message> split(Exchange exchange) {
		return splitMessage(exchange.getIn());
	}

	public List splitMessage(org.apache.camel.Message inMessage) {
		MessageIterator iter = new MessageIterator(inMessage.getBody(Message.class), "", true);
		List messages = new ArrayList();
		int messageCount = 0;
		while (iter.hasNext()) {
			Structure st = iter.next();
			if (st.getName().equalsIgnoreCase("BHS")) {
				try {
					messageCount++;
					String hl7 = createMessage(iter);
					DefaultMessage message = new DefaultMessage();
					message.setHeaders(inMessage.getHeaders());
					message.setHeader("BATCH", true);
					message.setHeader("BATCH_INDEX", messageCount);
					message.setBody(hl7);
					messages.add(message);
				} catch (HL7Exception e) {
					// need to log that we missed a message for some reason
				}
			}
		}

		return messages;
	}

	private String createMessage(MessageIterator iter) throws HL7Exception {
		StringBuffer buff = new StringBuffer();
		while (iter.hasNext()) {
			Structure st = iter.next();
			if (st.getName().equalsIgnoreCase("BTS")) {
				break;
			}
			buff.append(((Segment) st).encode());
			buff.append("\n");
		}

		return buff.toString();
	}
}
