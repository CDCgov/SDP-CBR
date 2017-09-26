package gov.cdc.sdp.cbr;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import de.saly.javamail.mock2.MockMailbox;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:EmailOnException.xml" })
@PropertySource("classpath:application.properties")
public class EmailOnExceptionTest2 extends BaseDBTest {

	Object lock = new Object();
	String address = "cbr_errors@cdc.gov";
	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:mock_endpoint")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Before
	public void setup() throws SQLException, IOException {
		MockMailbox.resetAll();
		synchronized (lock) {
			mockEndpoint.reset();
			DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
			super.setupDb(ds);
		}
	}

	@Test
	public void consumeFailedTest() throws Exception {
		assertEquals("Should be 0 messages in inbox", 0, MockMailbox.get(address).getInbox().getMessageCount());
		DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
		Connection conn = null;
		PreparedStatement ps = null;

		mockEndpoint.expectedMessageCount(1);
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("INSERT INTO testdb (status, routing) values('new', 'error');");
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		mockEndpoint.setResultWaitTime(20000);
		mockEndpoint.setAssertPeriod(4000);
		mockEndpoint.assertIsSatisfied();
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("SELECT * from testdb;");
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				String status = rs.getString("status");
				assertEquals("consumeFailed", status);
				count++;
			}
			assertEquals(1, count);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		synchronized (lock) {
			mockEndpoint.assertIsSatisfied();
			assertEquals("Should be 1 message in inbox", 1, MockMailbox.get(address).getInbox().getMessageCount());
		}

	}

}
