package org.cdc.gov.sdp;

import java.io.FileReader;
import java.util.List;

import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;

public class HL7V2BatchSplitterTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test() {
		HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
		Message msg = new DefaultMessage();
		msg.setBody(readFile(""));
		List<Message> messages = splitter.splitMessage(msg);
	}

	private String readFile(String file){
		//FileReader in = new FileReader(file);
		return "";
	}
}
