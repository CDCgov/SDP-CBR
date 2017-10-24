package gov.cdc.sdp.cbr.filter;

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
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import gov.cdc.sdp.cbr.common.SDPTestBase;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "/HL7FiltersTest-context.xml" })
@PropertySource("/application.properties")
public class HL7FiltersTest extends SDPTestBase {

    @Autowired
    protected CamelContext camelContext;

    @EndpointInject(uri = "mock:mock_endpoint_smith")
    protected MockEndpoint mockEndpointFiltered;

    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mockEndpointAll;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Produce(uri = "direct:start_fn")
    protected ProducerTemplate templateFN;

    @Produce(uri = "direct:start_preg")
    protected ProducerTemplate templatePreg;

    @Test
    public void modelFailure() throws Exception {
        mockEndpointAll.reset();
        mockEndpointAll.expectedMessageCount(0);

        String[] sourceFiles = { "src/test/resources/BatchTest_no_FHS.txt", "src/test/resources/BatchTest_no_BHS.txt",
                "src/test/resources/BatchTest_no_FTS.txt", "src/test/resources/BatchTest_no_BTS.txt",
                "src/test/resources/BatchTest_bad_FHS.txt", "src/test/resources/BatchTest_bad_BHS.txt",
                "src/test/resources/BatchTest_bad_FHS.txt", "src/test/resources/BatchTest_bad_BTS.txt",
                "src/test/resources/BatchTest_bad_FTS.txt", "src/test/resources/BatchTest_MSH_issues.txt" };

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

        for (String sourceFile : sourceFiles) {
            map.put("payloadBinaryContent", readFile(sourceFile));
            map.put("payloadTextContent", readFile(sourceFile));
            map.put("receivedTime", new Date().toString());
            msg.setBody(map);
            exchange.setIn(msg);
            template.send(exchange);
        }

        mockEndpointAll.assertIsSatisfied();
    }

    @Test
    public void testHapiFiltersLastName() throws InterruptedException, IOException {
        mockEndpointAll.reset();
        mockEndpointFiltered.reset();
        mockEndpointFiltered.expectedMessageCount(0);
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
        mockEndpointFiltered.assertIsSatisfied();
    }

    @Test
    public void testHapiFiltersLastNameSmith() throws InterruptedException, IOException {
        mockEndpointAll.reset();
        mockEndpointFiltered.reset();
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

        mockEndpointFiltered.expectedMessageCount(1);
        mockEndpointAll.expectedMessageCount(1);
        template.send(exchange);

        mockEndpointAll.assertIsSatisfied();
        mockEndpointFiltered.assertIsSatisfied();
    }

    @Test
    public void testHapiFiltersFoodNet() throws InterruptedException, IOException {
        mockEndpointAll.reset();
        mockEndpointFiltered.reset();
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

        mockEndpointFiltered.expectedMessageCount(1);
        mockEndpointAll.expectedMessageCount(3);
        templateFN.send(exchange);

        mockEndpointAll.assertIsSatisfied();
        mockEndpointFiltered.assertIsSatisfied();
    }

    @Test
    public void testHapiFilterOBXPregnancy() throws InterruptedException, IOException {
        mockEndpointAll.reset();
        mockEndpointFiltered.reset();
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

        mockEndpointAll.expectedMessageCount(3);
        mockEndpointFiltered.expectedMessageCount(3);
        templatePreg.send(exchange);

        mockEndpointAll.assertIsSatisfied();
        mockEndpointFiltered.assertIsSatisfied();
    }
}
