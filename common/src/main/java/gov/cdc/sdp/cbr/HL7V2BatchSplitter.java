package gov.cdc.sdp.cbr;

import com.google.gson.Gson;

import ca.uhn.hl7v2.HL7Exception;

import gov.cdc.sdp.cbr.model.SDPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Splitter for the PHIN Batch Message Protocol. More information can be found here:
 * ftp://ftp.cdc.gov/pub/software/TIMS/2009%20RVCT%20Documentation/Messaging%20Information/PHIN%20Batch%20Message%20Protocol%20Profile.pdf
 */
public class HL7V2BatchSplitter {
  private static final Logger LOG = LoggerFactory.getLogger(HL7V2BatchSplitter.class);

  public List<Message> split(Exchange exchange) throws HL7Exception {
    LOG.debug("CALLING SPLITTER WITH EXCHANGE");
    return splitMessage(exchange.getIn());
  }

  /**
   * Splits a Camel message containing a PHIN Batch Message into multiple Camel
   * messages containing a single HL7 message.
   * @param inMessage the message to be split
   * @return list of HL7 messages
   */
  public List<Message> splitMessage(org.apache.camel.Message inMessage) {
    LOG.debug("CALLING SPLITTER WITH MESSAGE");
    String batch = inMessage.getBody(String.class);
    List<Message> messages = new ArrayList<Message>();
    int messageCount = 0;
    int batchCount = 0;
    SDPMessage sdpMsg = new Gson().fromJson(
        (String) inMessage.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
    List<List<String>> fhs = readFileHeaderSegment(batch);
    for (Iterator<List<String>> iterator = fhs.iterator(); iterator.hasNext();) {
      List<String> list = iterator.next();
      List<List<String>> batches = readBatches(list);
      LOG.debug("NUMBER OF BATCHES " + batches.size());
      for (Iterator<List<String>> batchIter = batches.iterator(); batchIter.hasNext();) {
        List<String> batchList = batchIter.next();
        List<String> batchMessages = readMessages(batchList);
        for (Iterator<String> batchMessageIter = batchMessages.iterator();
            batchMessageIter.hasNext();) {
          SDPMessage newSdpMsg = sdpMsg.clone();
          DefaultMessage message = new DefaultMessage();
          Iterator<String> headerKeys = inMessage.getHeaders().keySet().iterator();
          while (headerKeys.hasNext()) {
            String key = headerKeys.next();
            if (!SDPMessage.SDP_MESSAGE_HEADER.equals(key)) {
              message.setHeader(key, inMessage.getHeader(key));
            }
          }
          newSdpMsg.setBatch(true);
          newSdpMsg.setBatchId(sdpMsg.getId() + "_batch_" + batchCount);
          newSdpMsg.setBatchIndex(messageCount);
          newSdpMsg.setId(sdpMsg.getId() + "_" + messageCount);
          String hl7 = batchMessageIter.next();
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
        batchCount++;
      }

    }
    LOG.debug("NUMBER OF MESSAGES " + messages.size());

    return messages;
  }

  /**
   * Splits the File Header Segments of the a PHIN Batch Message Protocol message.
   * @param data The raw message text
   * @return a list of segments
   */
  public List<List<String>> readFileHeaderSegment(String data) {
    LOG.debug("SPLITTING FHS");
    BufferedReader bf = new BufferedReader(new StringReader(data));
    List<String> segments = bf.lines().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    LOG.debug("Number of segments = " + segments.size());
    ListIterator<String> iter = segments.listIterator();
    List<List<String>> fhs = new ArrayList<List<String>>();
    while (iter.hasNext()) {
      String st = iter.next();
      if (st.trim().startsWith("FHS")) {
        List<String> batch = readUntil(iter, "FTS", false);
        fhs.add(batch);
      }
    }
    return fhs;
  }

  /**
   * Reads batch segments in a PHIN Batch Message Protocol file segment.
   * @param segments list of file segments
   * @return a list of segments that contains a list of batches
   */
  public List<List<String>> readBatches(List<String> segments) {
    ListIterator<String> iter = segments.listIterator();
    List<List<String>> batches = new ArrayList<List<String>>();
    while (iter.hasNext()) {
      String st = iter.next();
      if (st.trim().startsWith("BHS")) {
        List<String> batch = readUntil(iter, "BTS", false);
        batches.add(batch);
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
