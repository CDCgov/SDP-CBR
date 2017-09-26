package gov.cdc.sdp.cbr;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
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
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import gov.cdc.sdp.cbr.common.SDPTestBase;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:SDPMessageIdRepositoryTest-context.xml" })
@PropertySource("classpath:application.properties")
public class SDPMessageIdRepositoryTest extends SDPTestBase {

    @Autowired
    private CamelContext camelContext;

    @EndpointInject(uri = "mock:idempotence")
    protected MockEndpoint idempotenceEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void testIdempotenceOverShutdown() throws InterruptedException, IOException {
        String sourceFile = "src/test/resources/hl7v2.txt";

        Connection c = null;
        PreparedStatement ps = null;
        try {
            DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
            c = ds.getConnection();
            ps = c.prepareStatement(
                    "INSERT INTO testIdTable (message_id) values('PHINMS_testIdempotenceInMemory_rec')");
            ps.executeUpdate();
        } catch (SQLException e) {
            fail("SQL EXCEPTION");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException e) {
            }

        }

        idempotenceEndpoint.expectedMessageCount(0);

        // Build content
        Exchange exchange = new DefaultExchange(camelContext);
        Message msg = new DefaultMessage();

        Map<String, String> map = new HashMap<>();
        map.put("recordId", "testIdempotenceInMemory_rec");
        map.put("messageId", "testIdempotenceInMemory_msg");
        map.put("payloadName", "Name");
        map.put("payloadBinaryContent", readFile(sourceFile));
        map.put("payloadTextContent", readFile(sourceFile));
        map.put("localFileName", "file??");
        map.put("service", "service");
        map.put("action", "action");
        map.put("arguments", "arge");
        map.put("fromPartyId", "testIdempotenceInMemory");
        map.put("messageRecipient", "recipient");
        map.put("receivedTime", new Date().toString());
        msg.setBody(map);

        exchange.setIn(msg);
        template.send(exchange);

        // Resend
        exchange = new DefaultExchange(camelContext);
        msg = new DefaultMessage();
        map = new HashMap(map);
        map.put("payloadTextContent", readFile(sourceFile));
        msg.setBody(map);
        exchange.setIn(msg);
        template.send(exchange);

        // Confirm it was NOT delivered -- id in memory before starting.
        MockEndpoint.assertIsSatisfied(camelContext);
    }

    @Test
    public void testIdempotenceInMemory() throws InterruptedException, IOException {
        String sourceFile = "src/test/resources/hl7v2.txt";

        idempotenceEndpoint.expectedMessageCount(1);

        // Build content
        Exchange exchange = new DefaultExchange(camelContext);
        Message msg = new DefaultMessage();

        Map<String, String> map = new HashMap<>();
        map.put("recordId", "testIdempotenceInMemory_rec2");
        map.put("messageId", "testIdempotenceInMemoryr_msg2");
        map.put("payloadName", "Name");
        map.put("payloadBinaryContent", readFile(sourceFile));
        map.put("payloadTextContent", readFile(sourceFile));
        map.put("localFileName", "file??");
        map.put("service", "service");
        map.put("action", "action");
        map.put("arguments", "arge");
        map.put("fromPartyId", "testIdempotenceInMemory");
        map.put("messageRecipient", "recipient");
        map.put("receivedTime", new Date().toString());
        msg.setBody(map);

        exchange.setIn(msg);
        template.send(exchange);

        // Resend
        exchange = new DefaultExchange(camelContext);
        msg = new DefaultMessage();
        msg.setBody(new HashMap(map));
        exchange.setIn(msg);
        template.send(exchange);

        // Confirm it was delivered -- only one of two should have gone
        // through.
        MockEndpoint.assertIsSatisfied(camelContext);
    }

    @After
    public void tearDown() {
        SDPMessageIdRepository idRepo = (SDPMessageIdRepository) camelContext.getRegistry().lookupByName("idRepo");
        idRepo.delete();
    }
}
