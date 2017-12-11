package gov.cdc.sdp.cbr;

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

	@Autowired
    protected CamelContext camelContext;

    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mock_endpoint;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    @DirtiesContext
    public void testGenericProcessor() throws Exception {
    	mock_endpoint.reset();
        
        String source_file = "src/test/resources/BatchTest_GenV2_2msgs.txt";

        Exchange exchange = new DefaultExchange(camelContext);
        Message msg = new DefaultMessage();

        Map<String, String> map = new HashMap<>();
        map.put("recordId", "testGenericProcessor_rec");
        map.put("sourceId", "testGenericProcessor_src");
        map.put("messageId", "testGenericProcessor_msg");
        map.put("payloadName", "Name");
        map.put("payloadBinaryContent", readFile(source_file));
        map.put("payloadTextContent", readFile(source_file));
        map.put("localFileName", "file??");
        map.put("service", "service");
        map.put("action", "action");
        map.put("arguments", "arge");
        map.put("fromPartyId", "testGenericProcessor");
        map.put("messageRecipient", "recipient");
        map.put("receivedTime", new Date().toString());
        msg.setBody(map);
        
        msg.setHeader("FILTER", true);

        exchange.setIn(msg);

        mock_endpoint.expectedMessageCount(1);
        mock_endpoint.expectedHeaderReceived("CBR_ID", "_ROUTE1_testGenericProcessor_src");
        mock_endpoint.expectedHeaderReceived("sourceId", "testGenericProcessor_src");
        
        
        template.send(exchange);

        MockEndpoint.assertIsSatisfied(camelContext);
    }

    private String readFile(String file) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
    }

}
