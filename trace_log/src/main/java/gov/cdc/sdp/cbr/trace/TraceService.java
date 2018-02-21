package gov.cdc.sdp.cbr.trace;

import java.io.InvalidObjectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import gov.cdc.sdp.cbr.trace.model.TraceLog;
import gov.cdc.sdp.cbr.trace.model.TraceStatus;

@Service
public class TraceService {

    private String tableName;

    private DataSource traceLogDs;

    public TraceService(DataSource traceLogDs, String tableName) throws SQLException {
        this.traceLogDs = traceLogDs;
        this.tableName = tableName;
        setupTable(traceLogDs, tableName);
    }

    /**
     * Adds a new message to the trace log for a given inbound message to CBR.
     * 
     * @param cbrId
     *            The CBR ID of the originating message that was sent to CBR.
     * @param source
     *            The source of the message that was sent to CBR.
     * @param status
     *            The severity of the trace log.
     * @param description
     *            A brief description of the log event.
     * @return The created and saved log event.
     * @throws SQLException
     */
    public TraceLog addTraceMessage(String cbrId, String source, TraceStatus status, String description) throws SQLException {
        assert cbrId != null;
        assert status != null;
        assert description != null;
        assert source != null;
        assert description.length()<=256;
        
        TraceLog log = new TraceLog(cbrId, source, status, description, new Date());
        save(log);
        return log;
    }

    /**
     * Finds and generates the highest severity status across all trace logs for
     * a given CBR ID.
     * 
     * @param cbrId
     *            The CBR ID of the originating message that was sent to CBR.
     * @return The highest possible status condition for the given message.
     * 
     * @throws IllegalArgumentException
     *             Thrown when the specified message has not been logged into
     *             the system.
     * @throws SQLException
     *             Thrown when a database connection fails. 
     * @throws InvalidObjectException 
     *              Thrown when the status cannot be converted from the tuple
     */
    public TraceStatus getStatus(String cbrId) throws IllegalArgumentException, SQLException, InvalidObjectException {
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = traceLogDs.getConnection();
            ps = conn.prepareStatement(
                      "SELECT max(status) from " + tableName
                    + " WHERE CBR_ID = ?");
            ps.setString(1, cbrId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("No logs found for CBR ID " + cbrId);
            }
            Integer statusValue = rs.getInt(1);
            if (statusValue == 0) {
                throw new IllegalArgumentException("No logs found for CBR ID " + cbrId);
            }
            TraceStatus maxStatus = TraceStatus.getStatus(statusValue);
            if (maxStatus == null) {
                throw new InvalidObjectException("Invalid status found for CBR ID " + cbrId);
            }
            return maxStatus;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    
    /**
     * Finds and generates the highest severity status across all trace logs for
     * a given CBR ID.
     * 
     * @param cbrId
     *            The CBR ID of the originating message that was sent to CBR.
     * @param source
     *            The source of the originating message that was sent to CBR.
     * @return The highest possible status condition for the given message.
     * 
     * @throws IllegalArgumentException
     *             Thrown when the specified message has not been logged into
     *             the system.
     * @throws SQLException
     *             Thrown when a database connection fails. 
     * @throws InvalidObjectException 
     *              Thrown when the status cannot be converted from the tuple
     */
    public TraceStatus getStatus(String cbrId, String source) throws IllegalArgumentException, SQLException, InvalidObjectException {
        
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = traceLogDs.getConnection();
            ps = conn.prepareStatement(
                      "SELECT max(status) from " + tableName
                    + " WHERE CBR_ID = ? AND SOURCE = ?");
            ps.setString(1, cbrId);
            ps.setString(2,  source);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new IllegalArgumentException("No logs found for CBR ID " + cbrId);
            }
            Integer statusValue = rs.getInt(1);
            if (statusValue == 0) {
                throw new IllegalArgumentException("No logs found for CBR ID " + cbrId);
            }
            TraceStatus maxStatus = TraceStatus.getStatus(statusValue);
            if (maxStatus == null) {
                throw new InvalidObjectException("Invalid status found for CBR ID " + cbrId);
            }
            return maxStatus;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Returns an ordered list of all log event for a given inbound message to
     * CBR.
     * 
     * @param cbrId
     *            The CBR ID of the originating message that was sent to CBR.
     * @return An ordered list of messages by creation date. If no messages are
     *         found, an empty list
     * @throws SQLException 
     * @throws InvalidObjectException 
     */
    public List<TraceLog> getTrace(String cbrId) throws SQLException, InvalidObjectException {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = traceLogDs.getConnection();
            ps = conn.prepareStatement(
                      "SELECT * from " + tableName
                    + " WHERE CBR_ID = ?");
            ps.setString(1, cbrId);
            ResultSet rs = ps.executeQuery();
            List<TraceLog> messages = new ArrayList<TraceLog>();
            while (rs.next()) {
                Integer statusValue = rs.getInt("status");
                TraceStatus status = TraceStatus.getStatus(statusValue);
                if (status == null) {
                    throw new InvalidObjectException("Invalid status found for CBR ID " + cbrId);
                }
                messages.add(
                        new TraceLog(
                                rs.getString("cbr_id"),
                                rs.getString("source"),
                                status,
                                rs.getString("description"),
                                rs.getTimestamp("created_at")
                                ));
            }
            return messages;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Returns an ordered list of all log event for a given inbound message to
     * CBR.
     * 
     * @param cbrId
     *            The CBR ID of the originating message that was sent to CBR.
     * @param source
     * 			  The source of the originating message that was sent to CBR.            	
     * @return An ordered list of messages by creation date. If no messages are
     *         found, an empty list
     * @throws SQLException 
     * @throws InvalidObjectException 
     */
    public List<TraceLog> getTrace(String cbrId, String source) throws SQLException, InvalidObjectException {

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = traceLogDs.getConnection();
            ps = conn.prepareStatement(
                      "SELECT * from " + tableName
                    + " WHERE CBR_ID = ? AND SOURCE = ?");
            ps.setString(1, cbrId);
            ps.setString(2,  source);
            ResultSet rs = ps.executeQuery();
            List<TraceLog> messages = new ArrayList<TraceLog>();
            while (rs.next()) {
                Integer statusValue = rs.getInt("status");
                TraceStatus status = TraceStatus.getStatus(statusValue);
                if (status == null) {
                    throw new InvalidObjectException("Invalid status found for CBR ID " + cbrId);
                }
                messages.add(
                        new TraceLog(
                                rs.getString("cbr_id"),
                                rs.getString("source"),
                                status,
                                rs.getString("description"),
                                rs.getTimestamp("created_at")
                                ));
            }
            return messages;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    
    
    private boolean save(TraceLog log) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (cbr_id, source, status, description, created_at)" + " values (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = traceLogDs.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, log.getCbrId());
            ps.setString(2, log.getSource());
            ps.setInt(3, log.getStatus().getLevel());
            ps.setString(4, log.getDescription());
            ps.setTimestamp(5, new Timestamp(log.getCreatedAt().getTime()));
            ps.executeUpdate();
            return true;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void setupTable(DataSource ds, String tableName) throws SQLException {

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + "  id           bigserial primary key,"
                + "  cbr_id       varchar(255) NOT NULL, " 
                + "  source       varchar(255) NOT NULL, " 
                + "  status       int, "
                + "  description  varchar(255) NOT NULL, " 
                + "  created_at   timestamp NOT NULL)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = ds.getConnection();
            ps = conn.prepareStatement(sql);
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
