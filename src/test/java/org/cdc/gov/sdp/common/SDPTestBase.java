package org.cdc.gov.sdp.common;

import java.io.IOException;

import org.apache.camel.test.junit4.CamelTestSupport;

public class SDPTestBase {

	protected String readFile(String file) throws IOException {
		return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
	}
	
}
