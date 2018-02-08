package gov.cdc.sdp.cbr.queue;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:MessageQueueRouteTest-context.xml" })
@PropertySource("classpath:application.properties")

public class MessageQueueRouteTest extends CamelTestSupport {
    protected MockEndpoint resultEndpoint;
    protected String startEndpointUri = "activemq:queue:CBR";
    
    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mock_endpoint;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;
    
    @Autowired
    protected CamelContext camelContext;
    
    @Test
    public void testQueueToEndpoint() throws Exception {
    	mock_endpoint.expectedMessageCount(1);
    	
    	// Post SDP message to queue
    	Exchange exchange = new DefaultExchange(camelContext);
        Message msg = new DefaultMessage();
  
        Map<String, String> map = new HashMap<>();
        map.put("recordId", "testQueueProducer_rec");
        map.put("messageId", "testQueueProducer_msg");
        map.put("payloadName", "Name");
        map.put("localFileName", "file??");
        map.put("service", "service");
        map.put("action", "action");
        map.put("arguments", "arge");
        map.put("fromPartyId", "testQueueProducer");
        map.put("messageRecipient", "recipient");
        msg.setBody(map);
        exchange.setIn(msg);
        template.send(exchange);
        
        // Deliver to endpoint, messagecount 1
        // Out queue now has 1 message, per context route
        MockEndpoint.assertIsSatisfied(camelContext);
    }
   
    @Test
    public void testJmsRouteWithTextMessage() throws Exception {
    	mock_endpoint.expectedMessageCount(1);
        String expectedBody = "Hello there!";

        Map<String, String> map = new HashMap<>();
        map.put("recordId", "testQueueProducer_rec");
        map.put("messageId", "testQueueProducer_msg");
        map.put("payloadName", "Name");
        map.put("localFileName", "file??");
        map.put("service", "service");
        map.put("action", "action");
        map.put("arguments", "arge");
        map.put("fromPartyId", "testQueueProducer");
        map.put("messageRecipient", "recipient");
        
        resultEndpoint.expectedBodiesReceived(map);
        resultEndpoint.message(0).header("cheese").isEqualTo(123);

        sendExchange(map);

        //resultEndpoint.assertIsSatisfied();
        mock_endpoint.assertIsSatisfied(camelContext);
    }

    protected void sendExchange(final Object expectedBody) {
        template.sendBodyAndHeader(startEndpointUri, expectedBody, "cheese", 123);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        resultEndpoint = (MockEndpoint) context.getEndpoint("mock:mock_endpoint");
    }

}
