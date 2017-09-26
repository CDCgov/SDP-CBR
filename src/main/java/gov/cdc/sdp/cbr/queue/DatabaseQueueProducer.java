package gov.cdc.sdp.cbr.queue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

import gov.cdc.sdp.cbr.CBR;
import gov.cdc.sdp.cbr.model.SDPMessage;

/**
 * The producer reads data from the message headers and stores it in the
 * provided datasource and table. Note that the column structure is currently
 * hardcoded.
 *
 * @author Betsy Cole
 */
public class DatabaseQueueProducer extends DefaultProducer {

    private static Logger logger = LogManager.getLogger("SDPQueueLogger");

    private String queueInsertCommand;
    private DataSource queueDataSource;

    public DatabaseQueueProducer(Endpoint endpoint, String uri, DataSource ds, String tableName) {
        super(endpoint);

        queueInsertCommand = "INSERT INTO " + tableName
                + " (cbr_id, source, source_id, source_attributes,batch,batch_index, batch_id, payload, cbr_recevied_time, sender, recipient,  attempts, status, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?)";
        queueDataSource = ds;
    }

    @Override
    public DatabaseQueueEndpoint getEndpoint() {
        return (DatabaseQueueEndpoint) super.getEndpoint();
    }

    @Override
    public void process(final Exchange exchange) throws Exception {
        Connection queueConnection = null;
        PreparedStatement ps = null;
        try {
            queueConnection = queueDataSource.getConnection();
            ps = queueConnection.prepareStatement(this.queueInsertCommand);

            Map<String, Object> source_headers = exchange.getIn().getHeaders();

            // :#CBR_ID, :#SOURCE, :#SOURCE_ID, :#SOURCE_ATTRIBUTES, :#BATCH,
            // :#BATCH_INDEX, :#payload, :#CBR_RECEIVED_TIME, :#SENDER,
            // :#RECIPIENT, 0, 'queued', now(), now()
            Date now = new Date();
            SDPMessage sdpMsg = new Gson().fromJson((String) exchange.getIn().getHeader(SDPMessage.SDP_MESSAGE_HEADER),
                    SDPMessage.class);
            ps.setString(1, (String) source_headers.get(CBR.CBR_ID));
            ps.setString(2, sdpMsg.getSource());
            ps.setString(3, sdpMsg.getSourceId());
            ps.setString(4, new Gson().toJson(sdpMsg.getSourceAttributes()));
            ps.setBoolean(5, sdpMsg.isBatch());
            ps.setInt(6, sdpMsg.getBatchIndex());
            ps.setString(7, sdpMsg.getBatchId());
            ps.setString(8, sdpMsg.getPayload());
            ps.setString(9, sdpMsg.getCbrReceivedTime());
            ps.setString(10, sdpMsg.getSender());
            ps.setString(11, sdpMsg.getRecipient());
            ps.setInt(12, 0);
            ps.setString(13, "queued");
            ps.setString(14, now.toString());
            ps.setString(15, now.toString());

            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("An error occured when attempting to log to the SDP Log");
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (queueConnection != null) {
                queueConnection.close();
            }
        }
    }
}
