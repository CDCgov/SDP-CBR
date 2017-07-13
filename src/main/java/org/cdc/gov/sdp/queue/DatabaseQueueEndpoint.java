package org.cdc.gov.sdp.queue;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

@UriEndpoint(scheme = "sdpqueue", title = "SDPQueue", syntax = "sdpqueue", label = "database,sql,queue")
public class DatabaseQueueEndpoint extends DefaultEndpoint {
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseQueueEndpoint.class);

	@UriParam(description = "Sets the DataSource to use to communicate with the database.")
	private DataSource dataSource;
	@UriParam(description = "Sets the table in which the data will be stored.")
	private String tableName;
	@UriParam(description = "Delay (in seconds) between polls.")
	private int delay;
	@UriParam(description = "InitialDelay (in seconds) before first poll.")
	private int initialDelay;

	public DatabaseQueueEndpoint(String uri, Component component, DataSource ds, String tableName, int delay,
			int initialDelay) {
		super(uri, component);
		this.dataSource = ds;
		this.tableName = tableName;
		this.delay = delay;
		this.initialDelay = initialDelay;
	}

	@Override
	public Producer createProducer() throws Exception {
		return new DatabaseQueueProducer(this, getEndpointUri(), dataSource, tableName);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return new DatabaseQueueConsumer(this, dataSource, processor, tableName, delay, initialDelay);
	}

	private String outputClass = "String";// TODO this wont always be a string,
											// yeah?

	public List<?> queryForList(String[] headers, ResultSet rs, boolean allowMapToClass) throws SQLException {

		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

		while (rs.next()) {
			HashMap<String, String> row = new HashMap<String, String>();
			for (int i = 0; i < headers.length; i++) {
				row.put(headers[i], rs.getString(headers[i]));
			}
			data.add(row);
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	public Object queryForObject(ResultSet rs) throws SQLException {
		Object result = null;
		if (outputClass == null) {
			RowMapper rowMapper = new ColumnMapRowMapper();
			RowMapperResultSetExtractor<Map<String, Object>> mapper = new RowMapperResultSetExtractor<Map<String, Object>>(
					rowMapper);
			List<Map<String, Object>> data = mapper.extractData(rs);
			if (data.size() > 1) {
				LOG.error("Query result not unique for outputType=SelectOne. Got " + data.size() + " count instead.");
				throw new SQLDataException(
						"Query result not unique for outputType=SelectOne. Got " + data.size() + " count instead.");
			} else if (data.size() == 1) {
				// Set content depend on number of column from query result
				Map<String, Object> row = data.get(0);
				if (row.size() == 1) {
					result = row.values().iterator().next();
				} else {
					result = row;
				}
			}
		} else {
			Class<?> outputClzz = getCamelContext().getClassResolver().resolveClass(outputClass);
			RowMapper rowMapper = new BeanPropertyRowMapper(outputClzz);
			RowMapperResultSetExtractor<?> mapper = new RowMapperResultSetExtractor(rowMapper);
			List<?> data = mapper.extractData(rs);
			if (data.size() > 1) {
				LOG.error("Query result not unique for outputType=SelectOne. Got " + data.size() + " count instead.");
				throw new SQLDataException(
						"Query result not unique for outputType=SelectOne. Got " + data.size() + " count instead.");
			} else if (data.size() == 1) {
				result = data.get(0);
			}
		}

		// If data.size is zero, let result be null.
		return result;
	}
}
