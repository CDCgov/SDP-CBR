package org.cdc.gov.sdp.queue;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "/DatabaseQueueComponentTest-context.xml" })
@PropertySource("/application.properties")
public class DatabaseQueueComponentTest {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:foo")
	protected MockEndpoint foo;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Test
	@DirtiesContext
	public void testQueueConsumer() throws Exception {
		DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

		String minimum_required_headers = "(id, cbr_id, source, source_id, payload, cbr_recevied_time)";
		String col_val = minimum_required_headers
				+ " values (1337, 'cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
				+ new Date(System.currentTimeMillis()) + "')";
		String tableName = "message_queue";

		String create_dummy_data = "INSERT into " + tableName + col_val;
		String check_sent = "SELECT * FROM " + tableName + " WHERE id=1337 AND status='sent'";
		check_sent = "SELECT (id, status) FROM " + tableName + " WHERE id=1337";
		String clear_dummy_data = "DELETE FROM " + tableName + " WHERE id=1337";
		String get_count = "select * from " + tableName;
		int initial_count = 0;

		try {
			initial_count = jdbcTemplate.queryForList(get_count).size();
			int rows_affected = jdbcTemplate.update(create_dummy_data);
			assertEquals(1, rows_affected);

			foo.expectedMessageCount(rows_affected);
			MockEndpoint.assertIsSatisfied(camelContext);

			List<Map<String, Object>> lst = jdbcTemplate.queryForList(check_sent);
			assertEquals(1, lst.size());
		} finally {
			jdbcTemplate.update(clear_dummy_data);
			List<Map<String, Object>> lst = jdbcTemplate.queryForList(get_count);
			assertEquals(initial_count, lst.size());
		}
	}

	@Test
	@DirtiesContext
	public void testQueueProducer() throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(
				(DataSource) camelContext.getRegistry().lookupByName("nndssDataSource"));
		try {
			Exchange exchange = new DefaultExchange(camelContext);
			Message msg = new DefaultMessage();

			Map map = new HashMap<>();
			map.put("recordId", "testQueueProducer_rec");
			map.put("messageId", "testQueueProducer_msg");
			map.put("payloadName", "Name");
			map.put("payloadBinaryContent", readFile("src/test/resources/BatchTest_GenV2_2msgs.txt"));
			map.put("payloadTextContent", readFile("src/test/resources/BatchTest_GenV2_2msgs.txt"));
			map.put("localFileName", "file??");
			map.put("service", "service");
			map.put("action", "action");
			map.put("arguments", "arge");
			map.put("fromPartyId", "testQueueProducer");
			map.put("messageRecipient", "recipient");
			map.put("receivedTime", new Date().toString());
			msg.setBody(map);

			exchange.setIn(msg);

			foo.expectedMessageCount(3);
			template.send(exchange);
			MockEndpoint.assertIsSatisfied(camelContext);
			List lst = jdbcTemplate.queryForList("select * from message_queue where SOURCE_ID='testQueueProducer_rec'");
			assertEquals(3, lst.size());
			lst = jdbcTemplate.queryForList(
					"select * from message_queue where SOURCE_ID='testQueueProducer_rec' AND BATCH_INDEX=0");
			assertEquals(1, lst.size());
		} finally {
			jdbcTemplate.update("delete from message_queue where SOURCE_ID='testQueueProducer_rec'");

			List lst = jdbcTemplate.queryForList("select * from message_queue where SOURCE_ID='testQueueProducer_rec'");
			assertEquals(0, lst.size());
		}
	}

	private String readFile(String file) throws IOException {
		return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
	}

}
