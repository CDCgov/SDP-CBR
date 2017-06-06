package org.cdc.gov.sdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.cdc.gov.sdp.model.SDPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import ca.uhn.hl7v2.HL7Exception;

public class HL7V2BatchSplitter {
	private static final Logger LOG = LoggerFactory.getLogger(HL7V2BatchSplitter.class);

	public List<Message> split(Exchange exchange) throws HL7Exception {
		LOG.debug("CALLING SPLITTER WITH EXCHANGE");
		return splitMessage(exchange.getIn());
	}

	public List<Message> splitMessage(org.apache.camel.Message inMessage) {
		LOG.debug("CALLING SPLITTER WITH MESSAGE");
		String batch = inMessage.getBody(String.class);
		List<Message> messages = new ArrayList<Message>();
		int messageCount = 0;
		SDPMessage sdpMsg = new Gson().fromJson((String) inMessage.getHeader(SDPMessage.SDP_MESSAGE_HEADER),
				SDPMessage.class);
		List<List<String>> fhs = readFHS(batch);
		for (Iterator<List<String>> iterator = fhs.iterator(); iterator.hasNext();) {
			List<String> list = iterator.next();
			List<List<String>> batches = readBatches(list);
			for (Iterator<List<String>> batchIter = batches.iterator(); batchIter.hasNext();) {
				List<String> batchList = batchIter.next();
				List<String> batchMessages = readMessages(batchList);
				for (Iterator<String> batchMessageIter = batchMessages.iterator(); batchMessageIter.hasNext();) {
					SDPMessage newSdpMsg = sdpMsg.clone();
					String hl7 = batchMessageIter.next();
					DefaultMessage message = new DefaultMessage();
					Iterator<String> headerKeys = inMessage.getHeaders().keySet().iterator();
					while (headerKeys.hasNext()) {
						String key = headerKeys.next();
						if (!SDPMessage.SDP_MESSAGE_HEADER.equals(key)) {
							message.setHeader(key, inMessage.getHeader(key));
						}
					}
					
					// TODO: Set batch id
					newSdpMsg.setBatch(true);
					newSdpMsg.setBatchIndex(messageCount);
					newSdpMsg.setId(sdpMsg.getId() + "_" + messageCount);
					newSdpMsg.setPayload(hl7);
					message.setHeader(CBR.ID, newSdpMsg.getId());
					message.setHeader(CBR.CBR_ID, newSdpMsg.getId().trim());
					message.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(newSdpMsg));
					LOG.debug(message.getHeader(CBR.ID).toString());
					LOG.debug(hl7);
					message.setBody(hl7);
					messages.add(message);
					messageCount++;
				}
			}

		}
		LOG.debug("NUMBER OF MESSAGES " + messages.size());

		return messages;
	}

	public List<List<String>> readFHS(String data) {
		LOG.debug("SPLITTING FHS");
		List<String> segments = Arrays.asList(data.split("\r?\n"));
		LOG.debug("Number of segments = " + segments.size());
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
