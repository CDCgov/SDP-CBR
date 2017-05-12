package org.cdc.gov.sdp.queue;

import static org.junit.Assert.*;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "/DatabaseQueueComponentTest-context.xml" })
public class DatabaseQueueComponentTest {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:foo")
	protected MockEndpoint foo;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Test
	@DirtiesContext
	public void testQueueBasic() throws InterruptedException {
		String expectedBody = "TEST";
		foo.expectedBodiesReceived(expectedBody);
		template.sendBodyAndHeader(expectedBody, "CBR_ID", "testQueueBasic");
		MockEndpoint.assertIsSatisfied(camelContext);
	}

	@Test
	@DirtiesContext
	public void testQueueProducer() throws InterruptedException {
		String expectedBody = "TEST";
		foo.expectedBodiesReceived(expectedBody);
		template.sendBodyAndHeader(expectedBody, "CBR_ID", "testQueueProducer");
		MockEndpoint.assertIsSatisfied(camelContext);
	}
	
}
