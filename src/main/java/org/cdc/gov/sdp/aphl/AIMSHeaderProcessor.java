package org.cdc.gov.sdp.aphl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.aws.s3.S3Constants;
import org.cdc.gov.sdp.CBR;


public class AIMSHeaderProcessor implements Processor {

	public static String AIMSPlatformSender = "AIMSPlatformSender";
	public static String AIMSPlatformRecipient = "AIMSPlatformRecipient";
	public static String AIMSPlatformSenderProject = "AIMSPlatformSenderProject";
	public static String AIMSPlatformSenderProtocol = "AIMSPlatformSenderProtocol";
	public static String AIMSPlatformSenderEncryptionType = "AIMSPlatformSenderEncryptionType";
	public static String AIMSPlatformMessageId = "AIMSPlatformMessageId";
	public static String AIMSPlatformFilename = "AIMSPlatformFilename";

	private String project = "";
	private String recipient = "";
	private String sender = "";
	private String protocol = "";
	private String encryptionType = "";
	
	public void process(Exchange exchange) {
		Message in = exchange.getIn();
		Map<String, String> aimsHeaders = new HashMap<String, String>();
		in.setHeader(S3Constants.S3_HEADERS, aimsHeaders);
		aimsHeaders.put(AIMSPlatformSender, stringOrNull(getSender((String) in.getHeader(CBR.SENDER))));
		aimsHeaders.put(AIMSPlatformRecipient, stringOrNull(getRecipient((String) in.getHeader(CBR.RECIPIENT))));
		aimsHeaders.put(AIMSPlatformSenderProject, stringOrNull(getProject((String) in.getHeader(AIMSPlatformSenderProject))));
		aimsHeaders.put(AIMSPlatformSenderProtocol, stringOrNull(getProtocol((String) in.getHeader(AIMSPlatformSenderProtocol))));
		aimsHeaders.put(AIMSPlatformSenderEncryptionType,
				 stringOrNull(getEncryptionType((String) in.getHeader(AIMSPlatformSenderEncryptionType))));
		aimsHeaders.put( AIMSPlatformMessageId,stringOrNull( (String) in.getHeader(CBR.ID)));
		aimsHeaders.put(CBR.CBR_DELIVERED_TIME, new Date(System.currentTimeMillis()).toString());
		aimsHeaders.put(CBR.SOURCE_ATTRIBUTES,
				urlEncodeUTF8((Map<String, String>) in.getHeader(CBR.SOURCE_ATTRIBUTES)));
	
		String [] headers = new String[]{CBR.BATCH,CBR.BATCH_INDEX,CBR.CBR_ID,CBR.CBR_RECEIVED_TIME,CBR.SOURCE,CBR.SOURCE_ID};
		for (int i = 0; i < headers.length; i++) {
			Object v = in.getHeader(headers[i]);
			if(v!=null){
				aimsHeaders.put(headers[i], v.toString());
			}
		}
		
		aimsHeaders.values().removeIf(Objects::isNull);
		in.setBody(in.getBody(Map.class).get("payload"));
	}
	
	
	private String stringOrNull(Object v){
		return v == null? null :v.toString();
	}

	private String urlEncodeUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private String urlEncodeUTF8(Map<String, ?> map) {
		if(map == null){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			Object value = entry.getValue();
			String v = value == null? "null" : urlEncodeUTF8(value.toString());
			sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()),
					v));
		}
		return sb.toString();
	}

	public String getProject() {
		return project;
	}

	public String getProject(String p) {
		return p != null ? p : project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getRecipient(String r) {
		return r != null ? r : recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getSender() {
		return sender;
	}

	public String getSender(String s) {
		return s != null ? s : sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getProtocol(String p) {
		return p != null ? p : protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getEncryptionType() {
		return encryptionType;
	}

	public String getEncryptionType(String e) {
		return e != null ? e : encryptionType;
	}

	public void setEncryptionType(String encryptionType) {
		this.encryptionType = encryptionType;
	}

}
