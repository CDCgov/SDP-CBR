package org.cdc.gov.sdp.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.cdc.gov.sdp.CBR;

public class LogProducer extends DefaultProducer {

	private static Logger logger = LogManager.getLogger("SDPLogLogger");

	private final String[] log_keys = { CBR.SOURCE, CBR.SOURCE_ID, CBR.CBR_ID, CBR.BATCH, CBR.BATCH_INDEX, CBR.MESSAGE,
			CBR.ROUTE, CBR.TIMESTAMP };
	private final String table_name = "log_table";

	private String log_message;
	private HashMap<String, Object> log_object;

	private DataSource sql_ds;

	private String column_order;
	private String values;
	private String sql_insert_command;

	public LogProducer(Endpoint endpoint, String uri, DataSource ds, String msg) {
		super(endpoint);
		log_message = msg;
		log_object = new HashMap<String, Object>();

		column_order = "(";
		values = " values (";
		for (int i = 0; i < log_keys.length; i++) {
			column_order += log_keys[i];
			values += "?";
			if (i != log_keys.length - 1) {
				column_order += ", ";
				values += ", ";
			}
		}
		column_order += ")";
		values += ")";

		sql_insert_command = "insert into " + table_name + " " + column_order + values;
		sql_ds = ds;
	}

	@Override
	public LogEndpoint getEndpoint() {
		return (LogEndpoint) super.getEndpoint();
	}

	private Connection log_sqlcon;
	private PreparedStatement log_insert;

	@Override
	public void process(final Exchange exchange) throws Exception {
		try {
			if (log_sqlcon == null) {
				log_sqlcon = sql_ds.getConnection();
			}

			if (log_insert == null) {
				log_insert = log_sqlcon.prepareStatement(sql_insert_command);
			}

			String[] headers = { CBR.SOURCE, CBR.SOURCE_ID, CBR.CBR_ID, CBR.BATCH, CBR.BATCH_INDEX };
			Map<String, Object> source_headers = exchange.getIn().getHeaders();

			for (String h : headers) {
				if (source_headers.get(h) != null) {
					log_object.put(h, source_headers.get(h).toString());
				} else {
					logger.info("MISSING HEADER " + h);
					log_object.put(h, "Header_Was_Missing");
				}
			}
			log_object.put(CBR.MESSAGE, log_message);
			log_object.put(CBR.ROUTE, exchange.getFromRouteId());
			log_object.put(CBR.TIMESTAMP, new Date(System.currentTimeMillis()).toString());

			for (int i = 0; i < log_keys.length; i++) {
				log_insert.setString(i + 1, log_object.get(log_keys[i]).toString());
			}
			log_insert.executeUpdate();
		} catch (Exception e) {
			logger.error("An error occured when attempting to log to the SDP Log");
			e.printStackTrace();
		}
	}
}
