package gov.cdc.sdp.cbr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:SplitAndAggregateTest-context.xml" })
@PropertySource("classpath:application.properties")
public class SplitAndAggregateTest {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:endpoint")
	protected MockEndpoint mockEndpoint;

	@EndpointInject(uri = "mock:split")
	protected MockEndpoint mockSplit;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;
	
	@Test
	public void testAggregation() throws IOException, InterruptedException {
		mockEndpoint.reset();
		mockSplit.reset();
		String source_file = "src/test/resources/BatchTest_GenV2_2msgs.txt";

		Exchange exchange = new DefaultExchange(camelContext);
		Message msg = new DefaultMessage();

		Map<String, String> map = new HashMap<>();
		map.put("recordId", "testQueueProducer_rec");
		map.put("messageId", "testQueueProducer_msg");
		map.put("payloadName", "Name");
		map.put("payloadBinaryContent", readFile(source_file));
		map.put("payloadTextContent", readFile(source_file));
		map.put("localFileName", "file??");
		map.put("service", "service");
		map.put("action", "action");
		map.put("arguments", "arge");
		map.put("fromPartyId", "testQueueProducer");
		map.put("messageRecipient", "recipient");
		map.put("receivedTime", new Date().toString());
		msg.setBody(map);

		exchange.setIn(msg);

		mockEndpoint.expectedMessageCount(1);
		mockEndpoint.expectedHeaderReceived("ERROR_COUNT", 1);
		mockEndpoint.expectedHeaderReceived("MSG_COUNT", 3);
		mockSplit.expectedMessageCount(2);
		template.send(exchange);

		MockEndpoint.assertIsSatisfied(camelContext);
	}
	
	private String readFile(String file) throws IOException {
		return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
	}
}
