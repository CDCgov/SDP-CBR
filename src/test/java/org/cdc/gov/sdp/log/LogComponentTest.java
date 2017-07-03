package org.cdc.gov.sdp.log;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.sql.DataSource;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:LogComponentTest-context.xml" })
public class LogComponentTest {

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:foo")
	protected MockEndpoint foo;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Test
	@DirtiesContext
	public void testLogFunctionality() throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(
				(DataSource) camelContext.getRegistry().lookupByName("sdplogDataSource"));

		try
		{
			String expectedBody = "TEST";
			foo.expectedBodiesReceived(expectedBody);
			template.sendBodyAndHeader(expectedBody, "CBR_ID", "testLogFunctionality");
			MockEndpoint.assertIsSatisfied(camelContext);
			List lst = jdbcTemplate.queryForList("select * from log_table where CBR_ID='testLogFunctionality'");
			assertEquals(2, lst.size());
			lst = jdbcTemplate
					.queryForList("select * from log_table where CBR_ID='testLogFunctionality' AND MESSAGE='headers_set'");
			assertEquals(1, lst.size());
		} finally {
			jdbcTemplate.update("delete from log_table where CBR_ID='testLogFunctionality'");
	
			List lst = jdbcTemplate.queryForList("select * from log_table where CBR_ID='testLogFunctionality'");
			assertEquals(0, lst.size());
		}
	}
}
