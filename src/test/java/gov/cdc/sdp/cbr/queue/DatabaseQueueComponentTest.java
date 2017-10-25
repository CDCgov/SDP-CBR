package gov.cdc.sdp.cbr.queue;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:DatabaseQueueComponentTest-context.xml" })
@PropertySource("classpath:application.properties")
public class DatabaseQueueComponentTest {

    private static final String DELETE_FROM = "DELETE FROM ";

	private static final String SELECT_FROM = "SELECT * FROM ";

	private static final String HEADERS = "(id, cbr_id, source, source_id, payload, cbr_recevied_time)";

	private static final String INSERT_INTO = "INSERT into ";

	@Autowired
    protected CamelContext camelContext;

    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mock_endpoint;

    @EndpointInject(uri = "mock:mock_endpoint2")
    protected MockEndpoint mock_endpoint2;

    @EndpointInject(uri = "mock:mock_endpoint3")
    protected MockEndpoint mock_endpoint3;

    @EndpointInject(uri = "mock:mock_endpoint4")
    protected MockEndpoint mock_endpoint4;
    
    @EndpointInject(uri = "mock:mock_endpoint5")
    protected MockEndpoint mock_endpoint5;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    @DirtiesContext
    public void testLimitedQueueConsumer_Pass() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String minimum_required_headers = HEADERS;
        String col_val = minimum_required_headers
                + " values (1337, 'cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
                + new Date(System.currentTimeMillis()) + "')";
        String tableName = "message_queue_four";

        String create_dummy_data = INSERT_INTO + tableName + col_val;
        String check_sent = SELECT_FROM + tableName + " WHERE id=1337";
        String clear_dummy_data = DELETE_FROM + tableName + " WHERE id=1337";
        String get_count = SELECT_FROM + tableName;

        int initial_count = 0;

        try {
            initial_count = jdbcTemplate.queryForList(get_count).size();
            int rows_affected = jdbcTemplate.update(create_dummy_data);
            assertEquals(1, rows_affected);

            DatabaseQueueEndpoint endpoint100 = (DatabaseQueueEndpoint) camelContext.getRegistry().lookupByName("consumer_from");
            DatabaseQueueEndpoint endpoint5 = (DatabaseQueueEndpoint) camelContext.getRegistry().lookupByName("limit_from");

            assertEquals(endpoint100.getLimit(), 100);
            assertEquals(endpoint5.getLimit(), 5);
            mock_endpoint4.expectedMessageCount(rows_affected);
            mock_endpoint4.assertIsSatisfied();

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
    public void testMaxAttemptsQueueConsumer_fail() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String[] all_entries = new String[20];        
        String tableName = "message_queue_five";
                
        for (int i = 0; i < 20; i++) {
        	String content = HEADERS + " values ("+ i +", 'cbr_"+ i + "', 'mockland', 'mockland_"
        	  + i + "', 'the payload', '" + new Date(System.currentTimeMillis()) + "')";
        	all_entries[i] = INSERT_INTO + tableName + content;        	
        }
        String check_sent = SELECT_FROM + tableName + " WHERE status='sent'";
        String check_unsent = SELECT_FROM + tableName + " WHERE status='queued'";
        String clear_dummy_data = DELETE_FROM + tableName + " WHERE source='mockland'";
        String get_count = SELECT_FROM + tableName;

        int initial_count = 0;

        try {
            initial_count = jdbcTemplate.queryForList(get_count).size();
            int rows_affected = 0; 
            int[] ra = jdbcTemplate.batchUpdate(all_entries);
            for (int r : ra) {
              rows_affected += r; 
            }
            assertEquals(20, rows_affected);

            mock_endpoint5.expectedMessageCount(0);            
            mock_endpoint5.assertIsSatisfied();

            List<Map<String, Object>> lst = jdbcTemplate.queryForList(check_sent);
            assertEquals(0, lst.size());
            lst = jdbcTemplate.queryForList(check_unsent);
            assertEquals(20, lst.size());
        } finally {
            jdbcTemplate.update(clear_dummy_data);
            List<Map<String, Object>> lst = jdbcTemplate.queryForList(get_count);
            assertEquals(initial_count, lst.size());
        }
    }
    
    @Test
    @DirtiesContext
    public void testInitialDelayQueueConsumer_Fail() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String minimum_required_headers = HEADERS;
        String col_val = minimum_required_headers
                + " values (1337, 'cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
                + new Date(System.currentTimeMillis()) + "')";
        String tableName = "message_queue_two";

        String create_dummy_data = INSERT_INTO + tableName + col_val;
        String check_sent = SELECT_FROM + tableName + " WHERE id=1337";
        String clear_dummy_data = DELETE_FROM + tableName + " WHERE id=1337";
        String get_count = SELECT_FROM + tableName;

        int initial_count = 0;

        try {
            initial_count = jdbcTemplate.queryForList(get_count).size();
            int rows_affected = jdbcTemplate.update(create_dummy_data);
            assertEquals(1, rows_affected);

            mock_endpoint2.expectedMessageCount(rows_affected);
            mock_endpoint2.setResultWaitTime(5 * 1000);
            mock_endpoint2.assertIsNotSatisfied();

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
    public void testInitialDelayQueueConsumer_Pass() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String minimum_required_headers = HEADERS;
        String col_val = minimum_required_headers
                + " values (1337, 'cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
                + new Date(System.currentTimeMillis()) + "')";
        String tableName = "message_queue_two";

        String create_dummy_data = INSERT_INTO + tableName + col_val;
        String check_sent = SELECT_FROM + tableName + " WHERE id=1337";
        String clear_dummy_data = DELETE_FROM + tableName + " WHERE id=1337";
        String get_count = SELECT_FROM + tableName;

        int initial_count = 0;

        try {
            initial_count = jdbcTemplate.queryForList(get_count).size();
            int rows_affected = jdbcTemplate.update(create_dummy_data);
            assertEquals(1, rows_affected);

            mock_endpoint2.expectedMessageCount(rows_affected);
            // using 6.75 seconds because the wait time isn't exactly 7 seconds
            mock_endpoint2.setResultMinimumWaitTime(6750);
            mock_endpoint2.assertIsSatisfied();

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
    public void testDelayedQueueConsumer_Fail() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String minimum_required_headers = HEADERS;
        String col_val = minimum_required_headers
                + " values (1337, 'cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
                + new Date(System.currentTimeMillis()) + "')";
        String tableName = "message_queue_three";

        String create_dummy_data = INSERT_INTO + tableName + col_val;
        String check_sent = SELECT_FROM + tableName + " WHERE id=1337";
        String clear_dummy_data = DELETE_FROM + tableName + " WHERE id=1337";
        String get_count = SELECT_FROM + tableName;

        int initial_count = 0;

        try {
            initial_count = jdbcTemplate.queryForList(get_count).size();
            int rows_affected = jdbcTemplate.update(create_dummy_data);
            assertEquals(1, rows_affected);

            mock_endpoint3.expectedMessageCount(rows_affected);
            mock_endpoint3.setResultWaitTime(1 * 1000);
            mock_endpoint3.assertIsNotSatisfied();

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
    public void testDelayedQueueConsumer_Pass() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String minimum_required_headers = HEADERS;
        String col_val = minimum_required_headers
                + " values (1337, 'cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
                + new Date(System.currentTimeMillis()) + "')";
        String tableName = "message_queue_three";

        String create_dummy_data = INSERT_INTO + tableName + col_val;
        String check_sent = SELECT_FROM + tableName + " WHERE id=1337";
        String clear_dummy_data = DELETE_FROM + tableName + " WHERE id=1337";
        String get_count = SELECT_FROM + tableName;

        int initial_count = 0;

        try {
            initial_count = jdbcTemplate.queryForList(get_count).size();
            int rows_affected = jdbcTemplate.update(create_dummy_data);
            assertEquals(1, rows_affected);

            mock_endpoint3.expectedMessageCount(rows_affected);
            mock_endpoint3.assertIsSatisfied();

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
    public void testQueueConsumer() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String minimum_required_headers = HEADERS;
        String col_val = minimum_required_headers
                + " values (1337, 'cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
                + new Date(System.currentTimeMillis()) + "')";
        String tableName = "message_queue";

        String create_dummy_data = INSERT_INTO + tableName + col_val;
        String check_sent = SELECT_FROM + tableName + " WHERE id=1337";
        String clear_dummy_data = DELETE_FROM + tableName + " WHERE id=1337";
        String get_count = SELECT_FROM + tableName;

        int initial_count = 0;

        try {
            initial_count = jdbcTemplate.queryForList(get_count).size();
            int rows_affected = jdbcTemplate.update(create_dummy_data);
            assertEquals(1, rows_affected);

            mock_endpoint.expectedMessageCount(rows_affected);
            mock_endpoint.assertIsSatisfied();

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
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String delete_test_entries = "delete from message_queue where SOURCE_ID='testQueueProducer_rec'";
        String query_queue = "select * from message_queue where SOURCE_ID='testQueueProducer_rec'";
        String query_queue_for_batch_zero = "select * from message_queue where SOURCE_ID='testQueueProducer_rec' AND BATCH_INDEX=0";
        String source_file = "src/test/resources/BatchTest_GenV2_2msgs.txt";

        try {
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

            mock_endpoint.expectedMessageCount(3);
            template.send(exchange);

            MockEndpoint.assertIsSatisfied(camelContext);

            List<Map<String, Object>> lst = jdbcTemplate.queryForList(query_queue);
            assertEquals(3, lst.size());

            lst = jdbcTemplate.queryForList(query_queue_for_batch_zero);
            assertEquals(1, lst.size());
        } finally {
            jdbcTemplate.update(delete_test_entries);

            List<Map<String, Object>> lst = jdbcTemplate.queryForList(query_queue);
            assertEquals(0, lst.size());
        }
    }

    private String readFile(String file) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
    }

}
