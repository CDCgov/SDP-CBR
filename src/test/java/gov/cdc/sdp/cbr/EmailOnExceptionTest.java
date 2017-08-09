package gov.cdc.sdp.cbr;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
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
@ContextConfiguration(locations = { "/EmailOnException.xml" })
@PropertySource("/application.properties")
public class EmailOnExceptionTest extends SDPTestBase {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:mock_endpoint")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Test
	public void noS3AtUriTest() throws Exception {
		mockEndpoint.expectedMessageCount(1);
		Exchange exchange = new DefaultExchange(camelContext);
		template.send(exchange);
		mockEndpoint.assertIsSatisfied();
	}

}
