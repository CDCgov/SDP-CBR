package org.cdc.gov.sdp.model;

import java.util.Map;

/**
 * SDP-CBR message
 * @author ECOLE
 *
 */
public class SDPMessage {
	
	public final static String SDP_MESSAGE_HEADER = "SDP_MESSAGE_HEADER";

	/**
	 * Whether the message is a batch message and has been split into individual messages.  
	 * If it is a batch mehttps://www.amazon.com/Josie-Marks/e/B00YFPK5IIssage and has not been split this will be false.
	 */
	private boolean batch;
	
	/**
	 * The identifier of the individual batch within a message. If the batch does not contain an identifier this field will be empty.
	 */
	private boolean batch_id;
	
	/**
	 * If the message is a batch message and has been split this will contain the index of the message inside the batch. Index position starts at 0. This field has no meaning if the batch property is set to false or is not included
	 */
	private int batch_index;

	/**
	 * The time that the CBR service received the message from the source.  The format of this will be the same as the source_recieved_time.
	 */
	private String cbr_received_time;

	/**
	 * The CBR identifier for the message. The general case for this field will be a concatenation of the source, source_id and batch_index if applicable",
     */
	private String id;
	
	/**
	 * the message from the source system
	 */
	private String payload;
       
	/**
	 * If available from the source this will contain the information about the intended recipient of the message.  If this information cannot be derived from the source this field will be null.
	 */
	private String recipient;
    
	/**
	 * If available from the source this will contain the information about the sender of the message. If this information cannot be derived  from the source this filed will be null
	 */
	private String sender;

	/**
	 * The identifier of the source system that the message was obtained from
	 */
	private String source;
	
	/**
	 * Name value pairs of additional information provided by the source of the message
	 */
	private Map source_attributes;
	
	/**
	 * The identifier of the message from the source system
	 */
	private String source_id;
	
	/**
	 * The time the message was received by the source system, the value will be formatted as defined by RFC3339, e.g. 1985-04-12T23:20:50.52Z for times in UTC or 1996-12-19T16:39:57-08:00 for times in other time zones.   If this information is not available the field will be null.
	 */
	private String source_received_time;

	public boolean isBatch() {
		return batch;
	}

	public void setBatch(boolean batch) {
		this.batch = batch;
	}

	public boolean isBatchId() {
		return batch_id;
	}

	public void setBatchId(boolean batch_id) {
		this.batch_id = batch_id;
	}

	public int getBatchIndex() {
		return batch_index;
	}

	public void setBatchIndex(int batch_index) {
		this.batch_index = batch_index;
	}

	public String getCbrReceivedTime() {
		return cbr_received_time;
	}

	public void setCbrReceivedTime(String cbr_received_time) {
		this.cbr_received_time = cbr_received_time;
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

	public Map getSourceAttributes() {
		return source_attributes;
	}

	public void setSourceAttributes(Map source_attributes) {
		this.source_attributes = source_attributes;
	}

	public String getSourceId() {
		return source_id;
	}

	public void setSourceId(String source_id) {
		this.source_id = source_id;
	}

	public String getSourceReceivedTime() {
		return source_received_time;
	}

	public void setSourceReceivedTime(String source_received_time) {
		this.source_received_time = source_received_time;
	}

	
}
