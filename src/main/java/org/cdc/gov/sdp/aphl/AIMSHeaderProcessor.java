package org.cdc.gov.sdp.aphl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.aws.s3.S3Constants;
import org.cdc.gov.sdp.model.SDPMessage;

import com.google.gson.Gson;

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

	@Override
	public void process(Exchange exchange) {
		Message in = exchange.getIn();
		Map<String, String> aimsHeaders = new HashMap<String, String>();
		SDPMessage sdpMsg = new Gson().fromJson((String) in.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
		in.setHeader(S3Constants.S3_HEADERS, aimsHeaders);
		aimsHeaders.put(AIMSPlatformSender, sdpMsg.getSender());
		aimsHeaders.put(AIMSPlatformRecipient, sdpMsg.getRecipient());
		aimsHeaders.put(AIMSPlatformSenderProject,
				stringOrNull(getProject((String) in.getHeader(AIMSPlatformSenderProject))));
		aimsHeaders.put(AIMSPlatformSenderProtocol,
				stringOrNull(getProtocol((String) in.getHeader(AIMSPlatformSenderProtocol))));
		aimsHeaders.put(AIMSPlatformSenderEncryptionType,
				stringOrNull(getEncryptionType((String) in.getHeader(AIMSPlatformSenderEncryptionType))));
		aimsHeaders.put(AIMSPlatformMessageId, sdpMsg.getId());
		aimsHeaders.put(SDPMessage.SDP_MESSAGE_HEADER, ((String) in.getHeader(SDPMessage.SDP_MESSAGE_HEADER)));

		aimsHeaders.values().removeIf(Objects::isNull);
	}

	private String stringOrNull(Object v) {
		return v == null ? null : v.toString();
	}

	private String urlEncodeUTF8(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private String urlEncodeUTF8(Map<String, ?> map) {
		if (map == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			Object value = entry.getValue();
			String v = value == null ? "null" : urlEncodeUTF8(value.toString());
			sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()), v));
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
