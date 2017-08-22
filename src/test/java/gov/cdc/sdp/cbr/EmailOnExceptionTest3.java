package gov.cdc.sdp.cbr;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import gov.cdc.sdp.cbr.common.SDPTestBase;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:EmailOnException.xml" })
@PropertySource("classpath:application.properties")
public class EmailOnExceptionTest3 {
	
	Object lock = new Object();

	@Autowired
	protected CamelContext camelContext;

	@EndpointInject(uri = "mock:mock_endpoint")
	protected MockEndpoint mockEndpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Before
	public void setup() throws SQLException {
		synchronized(lock) {
			mockEndpoint.reset();
			DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = ds.getConnection();
				ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS testdb (recordId bigserial primary key,"
						+ " status varchar (255) default 'new', routing varchar (255));");
				ps.executeUpdate();
			} finally {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		}
	}
	
	@After
	public void tearDown() throws SQLException {
		synchronized(lock) {
			DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
			Connection conn = null;
			PreparedStatement ps = null;
			try {
				conn = ds.getConnection();
				ps = conn.prepareStatement("DROP TABLE testdb;");
				ps.executeUpdate();
			} finally {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		}
	}

	
	@Test
	public void consumeSuccessTest() throws Exception {
		DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("sdpqDataSource");
		Connection conn = null;
		PreparedStatement ps = null;
		
		mockEndpoint.expectedMessageCount(1);
		mockEndpoint.setAssertPeriod(5000);
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("INSERT INTO testdb (status, routing) values('new', 'success');");
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		synchronized(lock) {
			mockEndpoint.assertIsSatisfied();
		}
		try {
			conn = ds.getConnection();
			ps = conn.prepareStatement("SELECT * from testdb;");
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				String status = rs.getString("status");
				assertEquals("consumed", status);
				count++;
			}
			assertEquals(1,count);
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		synchronized(lock) {
			mockEndpoint.assertIsSatisfied();
		}
	}

}
