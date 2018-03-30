package gov.cdc.sdp.cbr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.model.SDPMessage;

public class HL7V2BatchSplitterTest {

  private static final String MESSAGE_ID = "Test_message";
  private static final String VALID_BATCH_FILE_NAME = "src/test/resources/BatchTest_GenV2_2msgs.txt";

  private static final String INVALID_BATCH_NO_FHS = "src/test/resources/BatchTest_no_FHS.txt";
  private static final String INVALID_BATCH_BAD_FHS = "src/test/resources/BatchTest_bad_FHS.txt";

  private static final String INVALID_BATCH_NO_BHS = "src/test/resources/BatchTest_no_BHS.txt";
  private static final String INVALID_BATCH_BAD_BHS = "src/test/resources/BatchTest_bad_BHS.txt";

  private static final String INVALID_BATCH_NO_FTS = "src/test/resources/BatchTest_no_FTS.txt";
  private static final String INVALID_BATCH_BAD_FTS = "src/test/resources/BatchTest_bad_FTS.txt";

  private static final String INVALID_BATCH_NO_BTS = "src/test/resources/BatchTest_no_BTS.txt";
  private static final String INVALID_BATCH_BAD_BTS = "src/test/resources/BatchTest_bad_BTS.txt";

  private static final String INVALID_BATCH_MSH_ISSUES = "src/test/resources/BatchTest_bad_BTS.txt";

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void mshProblems() throws Exception {
    // TODO
    // c. Malformed where MSH-21 cannot be parsed/determined.
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_MSH_ISSUES));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  @Test
  public void validBatchTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(VALID_BATCH_FILE_NAME));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  @Test
  public void missingFHSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_NO_FHS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(0, messages.size());
  }

  @Test
  public void malformedFHSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_BAD_FHS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  @Test
  public void missingBHSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_NO_BHS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(0, messages.size());
  }

  @Test
  public void malformedBHSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_BAD_BHS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  @Test
  public void missingFTSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_NO_FTS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  @Test
  public void malformedFTSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_BAD_FTS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  @Test
  public void missingBTSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_NO_BTS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  @Test
  public void malformedBTSTest() throws Exception {
    HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
    Message msg = new DefaultMessage();
    SDPMessage sdpMsg = new SDPMessage();
    sdpMsg.setId(MESSAGE_ID);

    String expected_bh = MESSAGE_ID + "_batch_0";
    msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
    msg.setBody(readFile(INVALID_BATCH_BAD_BTS));

    List<Message> messages = splitter.splitMessage(msg);
    assertEquals(3, messages.size());
    for (Message m : messages) {
      SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
      assertEquals(expected_bh, out.getBatchId());
    }
  }

  private String readFile(String file) throws IOException {
    return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
  }
}
