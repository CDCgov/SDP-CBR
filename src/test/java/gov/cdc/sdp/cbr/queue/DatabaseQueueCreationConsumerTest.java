package gov.cdc.sdp.cbr.queue;

import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseQueueCreationConsumerTest extends CamelTestSupport {

	private static final String route_id = "test_consume";
	private static final String driver = "org.postgresql.Driver";
	private static final String username = "sdp_dbq";
	private static final String password = "bob";
	private static final String dsurl = "jdbc:postgresql://localhost:5432/cbr_db";

	private static final String tableName = "temp_fake_table";
	private static final String drop_temp_table = "drop table if exists " + tableName;
	private static final String does_table_exist = "select * from pg_tables where tablename='" + tableName + "'";

	@EndpointInject(uri = "mock:mock_endpoint")
	protected MockEndpoint mock_endpoint;

	@Produce(uri = "direct:start")
	protected ProducerTemplate template;

	@Override
	protected JndiRegistry createRegistry() throws Exception {
		JndiRegistry jndi = super.createRegistry();
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(dsurl);
		jndi.bind("dataSource", ds);
		return jndi;
	}

	@Override
	protected void doPreSetup() throws Exception {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(dsurl);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		List<Map<String, Object>> lst;

		jdbcTemplate.update(drop_temp_table);
		lst = jdbcTemplate.queryForList(does_table_exist);
		// Ensure table has been dropped prior to routes being built in setup
		assertEquals(0, lst.size());
	}

	@Override
	protected RouteBuilder createRouteBuilder() {
		// Run during setup before the test, builds the route
		return new RouteBuilder() {
			@Override
			public void configure() {
				from("sdpqueue:temp_fake_table?dataSource=#dataSource").id(route_id).to("mock:result");
			}
		};
	}

	@Test
	public void testCreateConsume() throws Exception {
		String rid = context().getRoutes().get(0).getId();
		assertEquals(rid, route_id);
		// Ensure that the route was built

		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setUrl(dsurl);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

		List<Map<String, Object>> lst;
		lst = jdbcTemplate.queryForList(does_table_exist);
		assertEquals(1, lst.size());
		// Ensure that the table exists now
		// Literally nothing else needs to be done
	}
}
