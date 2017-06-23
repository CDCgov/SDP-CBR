package org.cdc.gov.sdp;

import java.io.IOException;
import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hl7.HL7DataFormat;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.cdc.gov.sdp.model.SDPMessage;
import org.junit.Test;

import com.google.gson.Gson;

public class FilterTest extends CamelTestSupport {

	@EndpointInject(uri = "mock:mock_endpoint")
	protected MockEndpoint mock_endpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() {
				DataFormat dataFormat = new HL7DataFormat();
				from("direct:start").unmarshal(dataFormat).filter().method(FoodNetFilter.class, "filter")
						.marshal(dataFormat).to("mock:mock_endpoint");
			}
		};
	}

	@Test
	public void testFilter() throws Exception {
		mock_endpoint.expectedMessageCount(1);

		HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();

		Exchange exchange = new DefaultExchange(context());
		SDPMessage sdpMsg = new SDPMessage();
		Message msg = new DefaultMessage();

		sdpMsg.setId("Test_message");
		msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
		msg.setBody(readFile("src/test/resources/BatchTest_GenV2_2msgs.txt"));
		List<Message> messages = splitter.splitMessage(msg);

		for (Message m : messages) {
			exchange.setIn(m);
			template.send(exchange);
		}

		MockEndpoint.assertIsSatisfied(context());
	}

	private String readFile(String file) throws IOException {
		return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
	}
}
