package org.cdc.gov.sdp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.cdc.gov.sdp.model.SDPMessage;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class HL7V2BatchSplitterTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test() throws Exception {
		HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
		Message msg = new DefaultMessage();
		SDPMessage sdpMsg = new SDPMessage();
		sdpMsg.setId("Test_message");
		msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
		msg.setBody(readFile("src/test/resources/BatchTest_GenV2_2msgs.txt"));
		List<Message> messages = splitter.splitMessage(msg);
		assertEquals(3, messages.size());
		for (Message m : messages) {
			SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
			assertEquals("Test_message_batch_0", out.getBatchId());
		}
	}

	private String readFile(String file) throws IOException {
		return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
	}
}
