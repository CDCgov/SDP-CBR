package gov.cdc.sdp.cbr;

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
@ContextConfiguration(locations = { "classpath:GenericTransformer-context.xml" })
@PropertySource("classpath:application.properties")
public class GenericTransformerTest {

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
    public void testGenericProcessor() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        /*String delete_test_entries = "delete from message_queue where SOURCE_ID='testQueueProducer_rec'";
        String query_queue = "select * from message_queue where SOURCE_ID='testQueueProducer_rec'";
        String query_queue_for_batch_zero = "select * from message_queue where SOURCE_ID='testQueueProducer_rec' AND BATCH_INDEX=0";*/
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

            //List<Map<String, Object>> lst = jdbcTemplate.queryForList(query_queue);
            //assertEquals(3, lst.size());

            //lst = jdbcTemplate.queryForList(query_queue_for_batch_zero);
            //assertEquals(1, lst.size());
        } finally {
            //jdbcTemplate.update(delete_test_entries);

            //List<Map<String, Object>> lst = jdbcTemplate.queryForList(query_queue);
            //assertEquals(0, lst.size());
        }
    }

    private String readFile(String file) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
    }

}
