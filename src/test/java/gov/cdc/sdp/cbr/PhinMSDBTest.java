package gov.cdc.sdp.cbr;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.ResultSet;
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
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:PhinMSDBTest-context.xml" })
@PropertySource("classpath:application.properties")
public class PhinMSDBTest {
	
	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:phinmsDb")
	protected MockEndpoint phinMSDb;

	@Produce(uri = "direct:phinms")
	protected ProducerTemplate template;
	
	private final String file_name = "phinms_input.csv";
	private final String input_file_path = "src/test/resources/" + file_name;

	@Test
	public void testPhinMSProducer() {
		JdbcTemplate jdbcTemplate = null;
		int count = -1;
		try {
			DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("phinMSDataSource");
			jdbcTemplate = new JdbcTemplate(ds);
			
			count = jdbcTemplate.queryForObject("select count(*) from message_inq", Integer.class);
			
			Exchange exchange = new DefaultExchange(camelContext);
			Message msg = new DefaultMessage();
			String expected_content = readFile(input_file_path);
			msg.setBody(expected_content);
			exchange.setIn(msg);
			phinMSDb.expectedMessageCount(1);
			template.send(exchange);
			MockEndpoint.assertIsSatisfied(camelContext);
			int linesInFile = getLinesInFile(input_file_path);
			
			int countDelta = jdbcTemplate.queryForObject("select count(*) from message_inq", Integer.class) - count;
			
			assertEquals(linesInFile-1, countDelta);
		} catch (IOException e) {
			fail("Could not read input file");
		} catch (InterruptedException e) {
			fail("Interrupted Exception thrown");
		} finally {
			if (jdbcTemplate != null) {
				jdbcTemplate.execute("DELETE from message_inq where fromPartyId='testPhinMS_Producer'");

				if (count > 0) {
					assertEquals(count, (int)jdbcTemplate.queryForObject("select count(*) from message_inq", Integer.class));
				}
			}
		}
	}
	

	@Test
	public void testPhinMSProducerMultiLine() {
		JdbcTemplate jdbcTemplate = null;
		int count = -1;
		try {
			DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("phinMSDataSource");
			jdbcTemplate = new JdbcTemplate(ds);
			
			count = jdbcTemplate.queryForObject("select count(*) from message_inq", Integer.class);
		
			String fileName = "phinms_input_multi.csv";
			String inputFilePath = "src/test/resources/" + fileName;
			Exchange exchange = new DefaultExchange(camelContext);
			Message msg = new DefaultMessage();
			String expected_content = readFile(inputFilePath);
			msg.setBody(expected_content);
			exchange.setIn(msg);
			phinMSDb.expectedMessageCount(1);
			template.send(exchange);
			MockEndpoint.assertIsSatisfied(camelContext);
			int countDelta = jdbcTemplate.queryForObject("select count(*) from message_inq", Integer.class) - count;
			
			assertEquals(13, countDelta);
		} catch (IOException e) {
			fail("Could not read input file");
		} catch (InterruptedException e) {
			fail("Interrupted Exception thrown");
		} finally {
			if (jdbcTemplate != null) {
				jdbcTemplate.execute("DELETE from message_inq where fromPartyId='TEST'");

				if (count > 0) {
					assertEquals(count, (int)jdbcTemplate.queryForObject("select count(*) from message_inq", Integer.class));
				}
			}
		}
	}
	
	
	private String readFile(String file) throws IOException {
		return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
	}
	
	private int getLinesInFile(String file) throws IOException {
		LineNumberReader lnr = null;
		try {
			lnr = new LineNumberReader(new FileReader(new File(file)));
			lnr.skip(Long.MAX_VALUE);
			int lines = lnr.getLineNumber();
			return lines;
		} finally {
			if (lnr != null)
				lnr.close();
		}
	}
}
