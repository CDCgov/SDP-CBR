package gov.cdc.sdp.cbr;

import org.apache.camel.Exchange;
import org.apache.commons.fileupload.RequestContext;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SDPRequestContext implements RequestContext {

	private Exchange exchange;

	public SDPRequestContext(Exchange exchange) {
		this.exchange = exchange;
	}

	public String getCharacterEncoding() {
		return StandardCharsets.UTF_8.toString();
	}

	//could compute here (we have stream cache enabled)
	public int getContentLength() {
		return (int) -1;
	}

	public String getContentType() {
		return exchange.getIn().getHeader("Content-Type").toString();
	}

	public InputStream getInputStream() throws IOException {
		return this.exchange.getIn().getBody(InputStream.class);
	}
}