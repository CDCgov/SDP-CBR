package gov.cdc.sdp.cbr.aphl;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.aws.s3.S3Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AIMSHeaderProcessor implements Processor {
  private static final Logger LOG = LoggerFactory.getLogger(AIMSHeaderProcessor.class);

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
    // aimsHeaders.put(SDPMessage.SDP_MESSAGE_HEADER, ((String)
    // in.getHeader(SDPMessage.SDP_MESSAGE_HEADER)));

    aimsHeaders.values().removeIf(Objects::isNull);
    in.setBody((in.getHeader(SDPMessage.SDP_MESSAGE_HEADER)));
  }

  private String stringOrNull(Object value) {
    return value == null ? null : value.toString();
  }

  private String urlEncodeUTF8(String string) {
    try {
      return URLEncoder.encode(string, "UTF-8");
    } catch (UnsupportedEncodingException uee) {
      LOG.error("Unsupported Encoding", uee);
      throw new UnsupportedOperationException(uee);
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
      String valueString = value == null ? "null" : urlEncodeUTF8(value.toString());
      sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()), valueString));
    }
    return sb.toString();
  }

  public String getProject() {
    return project;
  }

  public String getProject(String projectParam) {
    return projectParam != null ? projectParam : project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getRecipient(String recipientParam) {
    return recipientParam != null ? recipientParam : recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getSender() {
    return sender;
  }

  public String getSender(String senderParam) {
    return senderParam != null ? senderParam : sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getProtocol(String protocolParam) {
    return protocolParam != null ? protocolParam : protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getEncryptionType() {
    return encryptionType;
  }

  public String getEncryptionType(String encryptionTypeParam) {
    return encryptionTypeParam != null ? encryptionTypeParam : encryptionType;
  }

  public void setEncryptionType(String encryptionType) {
    this.encryptionType = encryptionType;
  }

}
