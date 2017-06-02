package org.cdc.gov.sdp;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "/PhinMSDBTest-context.xml" })
@PropertySource("/application.properties")
public class PhinMSDBTest {
	
	@Autowired
	protected CamelContext camelContext;
	
	private String sql = "select * from message_inq where applicationStatus is NULL and processingStatus = 'queued'";
	
	@Test
	public void testInsertIntoPhinMS() {
		DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("phinMSDataSource");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		String query = "INSERT INTO message_inq "
				+ "(localFileName, service, action, encryption) "
				+ "values (?, ?, ?, ?)";	
		int rows = jdbcTemplate.update(query, "localFile1", "service1", "action1", "SHA-128");
		assertEquals(1, rows);

		List<Map<String,Object>> results = jdbcTemplate.queryForList("select * from message_inq where localFileName='localFile1'");
		assertEquals(1, results.size());
		
		jdbcTemplate.update("delete from message_inq where localFileName='localFile1'");
		
		results = jdbcTemplate.queryForList("select * from message_inq where localFileName='localFile1'");
		assertEquals(0, results.size());
	}
	
}
