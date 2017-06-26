package org.cdc.gov.sdp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.cdc.gov.sdp.common.SDPTestBase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import static org.apache.camel.component.hl7.HL7.terser;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "/HL7FiltersTest-context.xml" })
@PropertySource("/application.properties")
public class HL7FiltersTest extends SDPTestBase {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:mock_endpoint_smith")
	protected MockEndpoint mockEndpointSmith;

	@EndpointInject(uri = "mock:mock_endpoint")
	protected MockEndpoint mockEndpointAll;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;
	
	private static boolean routeConfigured = false;
	
	@Before
	public void setUp() throws Exception {
		if (!routeConfigured) {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() {
					from("direct:hapi")
		            .convertBodyTo(String.class)
		            .transform().simple("${in.body.trim()}")
				      .filter(terser("/.PID-5-1").isEqualTo("SMITH"))
				      .to("mock:mock_endpoint_smith");
				}
			});
			routeConfigured = true;
		}
	}
	
	@Test
	public void testHapiFiltersPID1() throws InterruptedException, IOException {
		mockEndpointAll.reset();
		mockEndpointSmith.reset();
		mockEndpointSmith.expectedMessageCount(0);
		mockEndpointAll.expectedMessageCount(3);
		String sourceFile = "src/test/resources/BatchTest_GenV2_2msgs.txt";
		Exchange exchange = new DefaultExchange(camelContext);
		Message msg = new DefaultMessage();

		Map<String, String> map = new HashMap<>();
		map.put("recordId", "testQueueProducer_rec");
		map.put("messageId", "testQueueProducer_msg");
		map.put("payloadName", "Name");
		map.put("payloadBinaryContent", readFile(sourceFile));
		map.put("payloadTextContent", readFile(sourceFile));
		map.put("localFileName", "file??");
		map.put("service", "service");
		map.put("action", "action");
		map.put("arguments", "arge");
		map.put("fromPartyId", "testQueueProducer");
		map.put("messageRecipient", "recipient");
		map.put("receivedTime", new Date().toString());
		msg.setBody(map);

		exchange.setIn(msg);

		template.send(exchange);

		mockEndpointAll.assertIsSatisfied();
		mockEndpointSmith.assertIsSatisfied();
	}
	
	@Test
	public void testHapiFiltersPID0001() throws InterruptedException, IOException {
		mockEndpointAll.reset();
		mockEndpointSmith.reset();
		String sourceFile = "src/test/resources/hl7v2.txt";
		Exchange exchange = new DefaultExchange(camelContext);
		Message msg = new DefaultMessage();

		Map<String, String> map = new HashMap<>();
		map.put("recordId", "testQueueProducer_rec");
		map.put("messageId", "testQueueProducer_msg");
		map.put("payloadName", "Name");
		map.put("payloadBinaryContent", readFile(sourceFile));
		map.put("payloadTextContent", readFile(sourceFile));
		map.put("localFileName", "file??");
		map.put("service", "service");
		map.put("action", "action");
		map.put("arguments", "arge");
		map.put("fromPartyId", "testQueueProducer");
		map.put("messageRecipient", "recipient");
		map.put("receivedTime", new Date().toString());
		msg.setBody(map);

		exchange.setIn(msg);

		mockEndpointSmith.expectedMessageCount(1);
		mockEndpointAll.expectedMessageCount(1);
		template.send(exchange);

		mockEndpointAll.assertIsSatisfied();
		mockEndpointSmith.assertIsSatisfied();
	}
	
//	@Override
//	protected RouteBuilder createRouteBuilder() {
//		// Run during setup before the test, builds the route
//		return new RouteBuilder() {
//			@Override
//			public void configure() {
//				from("direct:start").to("mock:mock_endpoint");
//			}
//		};
//	}
	

}
