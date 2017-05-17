package org.cdc.gov.sdp.queue;

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

/**
 * The producer reads data from the message headers and stores it in the provided datasource
 * and table. Note that the column structure is currently hardcoded.
 * 
 * @author Betsy Cole
 */
public class DatabaseQueueProducer extends DefaultProducer {
	
	private static Logger logger = LogManager.getLogger("SDPQueueLogger");

	private String queueInsertCommand;
	private DataSource queueDataSource;
	private Connection queueConnection;
	private PreparedStatement ps;

	public DatabaseQueueProducer(Endpoint endpoint, String uri, DataSource ds, String tableName) {
		super(endpoint);
		queueInsertCommand = "INSERT INTO " + tableName + 
				" (cbr_id, source, source_id, source_attributes , batch, batch_index, payload, cbr_recevied_time, sender, recipient,  attempts, status, created_at, updated_at) " + 
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		queueDataSource = ds;
	}

	@Override
	public DatabaseQueueEndpoint getEndpoint() {
		return (DatabaseQueueEndpoint) super.getEndpoint();
	}

	@Override
	public void process(final Exchange exchange) throws Exception {
		try {
			if (queueConnection == null) {
				queueConnection = queueDataSource.getConnection();
			}

			if (ps == null) {
				ps = queueConnection.prepareStatement(this.queueInsertCommand);
			}

			Map<String, Object> source_headers = exchange.getIn().getHeaders();
			
			
		//	:#CBR_ID, :#SOURCE, :#SOURCE_ID, :#SOURCE_ATTRIBUTES, :#BATCH, :#BATCH_INDEX, :#payload, :#CBR_RECEIVED_TIME, :#SENDER, :#RECIPIENT,  0, 'queued', now(), now()
			Date now = new Date();
			ps.setString(1,(String)source_headers.get(CBR.CBR_ID));
			ps.setString(2,(String)source_headers.get(CBR.SOURCE));
			ps.setString(3,(String)source_headers.get(CBR.SOURCE_ID));
			ps.setString(4,(String)source_headers.get(CBR.SOURCE_ATTRIBUTES));
			ps.setBoolean(5,(Boolean)source_headers.get(CBR.BATCH));
			ps.setInt(6,(Integer)source_headers.get(CBR.BATCH_INDEX));
			ps.setString(7,(String)source_headers.get(CBR.PAYLOAD));
			ps.setString(8,source_headers.get(CBR.CBR_RECEIVED_TIME).toString());
			ps.setString(9,(String)source_headers.get(CBR.SENDER));
			ps.setString(10,(String)source_headers.get(CBR.RECIPIENT));
			ps.setInt(11,0);
			ps.setString(12,"queued");
			ps.setString(13,now.toString());
			ps.setString(14,now.toString());

			ps.executeUpdate();
		} catch (Exception e) {
			logger.error("An error occured when attempting to log to the SDP Log");
			e.printStackTrace();
		}
		System.out.println("Do something");
	}
}
