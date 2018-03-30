package gov.cdc.sdp.cbr.model;

import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * SDP-CBR message.
 *
 * @author ECOLE
 *
 */
public class SDPMessage implements Cloneable {

  public static final String SDP_MESSAGE_HEADER = "SDP_MESSAGE_HEADER";

  /**
   * Whether the message is a batch message and has been split into individual
   * messages. If it is a batch message and has not been split this will be false.
   */
  private boolean batch;

  /**
   * The identifier of the individual batch within a message. If the batch does
   * not contain an identifier this field will be empty.
   */
  private String batchId;

  /**
   * If the message is a batch message and has been split this will contain the
   * index of the message inside the batch. Index position starts at 0. This field
   * has no meaning if the batch property is set to false or is not included
   */
  private int batchIndex;

  /**
   * The time that the CBR service received the message from the source. The
   * format of this will be the same as the source_recieved_time.
   */
  private String cbrReceivedTime;

  /**
   * The CBR identifier for the message. The general case for this field will be a
   * concatenation of the source, source_id and batch_index if applicable",
   */
  private String id;

  /**
   * The message that is from the source system.
   */
  private String payload;

  /**
   * If available from the source this will contain the information about the
   * intended recipient of the message. If this information cannot be derived from
   * the source this field will be null.
   */
  private String recipient;

  /**
   * If available from the source this will contain the information about the
   * sender of the message. If this information cannot be derived from the source
   * this filed will be null
   */
  private String sender;

  /**
   * The identifier of the source system that the message was obtained from.
   */
  private String source;

  /**
   * Name value pairs of additional information provided by the source of the
   * message.
   */
  private Map sourceAttributes = new HashMap();

  /**
   * The identifier of the message from the source system.
   */
  private String sourceId;

  /**
   * The time the message was received by the source system, the value will be
   * formatted as defined by RFC3339, e.g. 1985-04-12T23:20:50.52Z for times in
   * UTC or 1996-12-19T16:39:57-08:00 for times in other time zones. If this
   * information is not available the field will be null.
   */
  private String sourceReceivedTime;

  public boolean isBatch() {
    return batch;
  }

  public void setBatch(boolean batch) {
    this.batch = batch;
  }

  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  public int getBatchIndex() {
    return batchIndex;
  }

  public void setBatchIndex(int batchIndex) {
    this.batchIndex = batchIndex;
  }

  public String getCbrReceivedTime() {
    return cbrReceivedTime;
  }

  public void setCbrReceivedTime(String cbrReceivedTime) {
    this.cbrReceivedTime = cbrReceivedTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @SuppressWarnings("rawtypes")
  public Map getSourceAttributes() {
    return sourceAttributes;
  }

  public void setSourceAttributes(Map sourceAttributes) {
    this.sourceAttributes = sourceAttributes;
  }

  public String getSourceId() {
    return sourceId;
  }

  public void setSourceId(String sourceId) {
    this.sourceId = sourceId;
  }

  public String getSourceReceivedTime() {
    return sourceReceivedTime;
  }

  public void setSourceReceivedTime(String sourceReceivedTime) {
    this.sourceReceivedTime = sourceReceivedTime;
  }

  /**
   * Create a copy of the object.
   */
  @Override
  public SDPMessage clone() {

    SDPMessage msg = new SDPMessage();
    BeanUtils.copyProperties(this, msg);
    // make sure this has a hash of its own so if things change they only
    // effect the single instance and not any of the clones
    // or vise versa
    Map newAtts = new HashMap();
    if (sourceAttributes != null) {
      for (Iterator iterator = sourceAttributes.keySet().iterator(); iterator.hasNext();) {
        Object key = iterator.next();
        newAtts.put(key, sourceAttributes.get(key));
      }
    }
    msg.setSourceAttributes(newAtts);
    return msg;
  }

}
