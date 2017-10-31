package gov.cdc.sdp.cbr.queue;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Before;
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
@ContextConfiguration(locations = { "classpath:DatabaseQueueParallelTest-context.xml" })
@PropertySource("classpath:application.properties")
public class DatabaseQueueParallelTest {

    @Autowired
    protected CamelContext camelContext;

    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mock_endpoint;

    @EndpointInject(uri = "mock:mock_endpoint2")
    protected MockEndpoint mock_endpoint2;

    @EndpointInject(uri = "mock:mock_endpoint3")
    protected MockEndpoint mock_endpoint3;

    @Before
    public void setUp() {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        String minimum_required_headers = "(cbr_id, source, source_id, payload, cbr_recevied_time)";
        String col_val = minimum_required_headers + " values ('cbr_1337', 'mockland', 'mockland_1', 'the payload', '"
                + new Date(System.currentTimeMillis()) + "')";
        String tableName = "message_queue";

        String create_dummy_data = "INSERT into " + tableName + col_val;

        for (int i = 0; i < 1000; i++) {
            jdbcTemplate.update(create_dummy_data);
        }

    }

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    @DirtiesContext
    public void testQueueConsumer() throws Exception {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        String tableName = "message_queue";

        String clear_dummy_data = "DELETE FROM " + tableName + " WHERE cbr_id='cbr_1337'";
        String check_sent = "SELECT * FROM " + tableName + " WHERE cbr_id='cbr_1337'";
        try {
            mock_endpoint.expectedMessageCount(1000);
            mock_endpoint.setAssertPeriod(1000);
            mock_endpoint.assertIsSatisfied();
        } finally {
            jdbcTemplate.update(clear_dummy_data);
            List<Map<String, Object>> lst = jdbcTemplate.queryForList(check_sent);
            assertEquals(0, lst.size());
        }
    }

}
