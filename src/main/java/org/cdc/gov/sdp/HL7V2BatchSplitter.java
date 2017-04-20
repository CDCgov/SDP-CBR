package org.cdc.gov.sdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

	// public List<Message> splitMessage(org.apache.camel.Message inMessage) {
	// System.out.println("CALLING SPLITTER WITH MESSAGE");
	// String batch = inMessage.getBody(String.class);
	// StringReader reader = new StringReader(batch);
	//
	// List<String> segments = Arrays.asList(batch.split("\\r?\\n"));
	// System.out.println("Number of segments = "+ segments.size());
	// List<Message> messages = new ArrayList<Message>();
	// int messageCount = 0;
	//
	// ListIterator<String> iter = segments.listIterator();
	//
	// while (iter.hasNext()) {
	// String st = iter.next();
	//
	// if (st.trim().startsWith("MSH")) {
	// try {
	// iter.previous();
	// String hl7 = createMessage(iter);
	// DefaultMessage message = new DefaultMessage();
	// message.setHeaders(inMessage.getHeaders());
	// message.setHeader(CBR.BATCH, true);
	// message.setHeader(CBR.BATCH_INDEX, messageCount);
	// System.out.println(hl7);
	// message.setBody(hl7);
	// messages.add(message);
	// } catch (HL7Exception e) {
	// // need to log that we missed a message for some reason
	// e.printStackTrace();
	// }
	// }
	// }
	// return messages;
	// }

	public List<Message> splitMessage(org.apache.camel.Message inMessage) {
		System.out.println("CALLING SPLITTER WITH MESSAGE");
		String batch = inMessage.getBody(String.class);
		List<Message> messages = new ArrayList<Message>();
		int messageCount = 0;

		List<List<String>> fhs = readFHS(batch);
		for (Iterator<List<String>> iterator = fhs.iterator(); iterator.hasNext();) {
			List<String> list = iterator.next();
			List<List<String>> batches = readBatches(list);
			for (Iterator<List<String>> batchIter = batches.iterator(); batchIter.hasNext();) {
				List<String> batchList = batchIter.next();
				List<String> batchMessages = readMessages(batchList);
				for (Iterator<String> batchMessageIter = batchMessages.iterator(); batchMessageIter.hasNext();) {
					String hl7 = batchMessageIter.next();
					DefaultMessage message = new DefaultMessage();
					Iterator<String> headerKeys = inMessage.getHeaders().keySet().iterator();
					while(headerKeys.hasNext()){
						String key = headerKeys.next();
						message.setHeader(key, inMessage.getHeader(key));
					}
					message.setHeader(CBR.BATCH, true);
					message.setHeader(CBR.BATCH_INDEX, messageCount);
					message.setHeader(CBR.ID, inMessage.getHeader(CBR.ID).toString() + "_"+messageCount);
					System.out.println(hl7);
					System.out.println();
					System.out.println(message.getHeader(CBR.ID));
					System.out.println();
					message.setBody(hl7);
					messages.add(message);
					messageCount++;
				}
			}

		}
		System.out.println("NUMBER OF MESSAGES "  + messages.size());
		
		return messages;
	}

	public List<List<String>> readFHS(String data) {
		System.out.println("SPLITTING FHS");
		List<String> segments = Arrays.asList(data.split("\r?\n"));
		System.out.println("Number of segments = " + segments.size());
		ListIterator<String> iter = segments.listIterator();
		List<List<String>> fhs = new ArrayList<List<String>>();
		while (iter.hasNext()) {
			String st = iter.next();
			if (st.trim().startsWith("FHS")) {
				List<String> b = readUntil(iter, "FTS", false);
				fhs.add(b);
			}
		}
		return fhs;
	}

	public List<List<String>> readBatches(List<String> segments) {
		System.out.println("SPLITTING BHS");
		ListIterator<String> iter = segments.listIterator();
		List<List<String>> batches = new ArrayList<List<String>>();
		while (iter.hasNext()) {
			String st = iter.next();
			if (st.trim().startsWith("BHS")) {
				List<String> b = readUntil(iter, "BTS", false);
				batches.add(b);
			}
		}
		return batches;
	}

	private List<String> readMessages(List<String> list) {
		ListIterator<String> iter = list.listIterator();
		List<String> messages = new ArrayList<String>();
		StringBuffer buff = new StringBuffer();
		while (iter.hasNext()) {
			String st = iter.next().trim();
			if (buff.length() != 0 && st.startsWith("MSH")) {
				messages.add(buff.toString());
				buff.setLength(0);
			}
			buff.append(st);
			buff.append("\r");
		}
		if (buff.length() != 0) {
			messages.add(buff.toString());
		}
		return messages;
	}

	private List<String> readUntil(ListIterator<String> iter, String seg, boolean maintain) {
		List<String> segments = new ArrayList<String>();
		boolean found = false;
		while (iter.hasNext()) {
			String st = iter.next().trim();
			if (st.startsWith(seg)) {
				found = true;
				break;
			}
			segments.add(st);
		}
		if (!found) {
			// throw exception;
		}
		if (maintain) {
			iter.previous();
		}
		return segments;
	}

}
