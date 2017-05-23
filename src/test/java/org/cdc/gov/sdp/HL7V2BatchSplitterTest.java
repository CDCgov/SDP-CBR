package org.cdc.gov.sdp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
public class HL7V2BatchSplitterTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test() throws Exception{
		HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
		Message msg = new DefaultMessage();		
		msg.setHeader(CBR.ID, "Test_message");
		msg.setBody(readFile("src/test/resources/BatchTest_GenV2_2msgs.txt"));
		List<Message> messages = splitter.splitMessage(msg);
		System.out.println(messages);
		assertEquals(3,messages.size());
	}

	private String readFile(String file) throws IOException{
		return  new String(java.nio.file.Files.readAllBytes(
			    java.nio.file.Paths.get(file)));
	}
}
