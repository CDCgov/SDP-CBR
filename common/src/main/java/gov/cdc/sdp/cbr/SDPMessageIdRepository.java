package gov.cdc.sdp.cbr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.camel.processor.idempotent.jdbc.AbstractJdbcMessageIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An repository for message ids to enable persistent idempotence.  Allows for 
 * dynamic table names, enabling multiple sets of ids.  
 *
 */
public class SDPMessageIdRepository extends AbstractJdbcMessageIdRepository<String> {

    private static final Logger LOG = LoggerFactory.getLogger(SDPMessageIdRepository.class);

    final String tableName;

    public SDPMessageIdRepository(DataSource ds, String processorName, String tableName)
            throws IllegalArgumentException {
        super(ds, processorName);
        if (!Pattern.matches("[a-zA-Z][a-zA-Z0-9]*", tableName)) {
            throw new IllegalArgumentException("Invalid table name.");
        } else {
            this.tableName = tableName;
            setDataSource(ds);
            createIfNotExists();
        }
    }

    private void createIfNotExists() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getDataSource().getConnection();
            ps = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " + tableName + " (message_id varchar(255) primary key)");
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot initialize -- SQL error creating table.", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    @Override
    protected int delete() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getDataSource().getConnection();
            ps = conn.prepareStatement("DELETE FROM " + tableName);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot delete from " + tableName, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return 0;
    }

    @Override
    protected int delete(String messageId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getDataSource().getConnection();
            ps = conn.prepareStatement("DELETE FROM " + tableName + " where message_id=?");
            ps.setString(1, messageId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot delete from " + tableName + " with message_id " + messageId, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return 0;
    }

    @Override
    protected int insert(String messageId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getDataSource().getConnection();
            ps = conn.prepareStatement("INSERT INTO " + tableName + " (message_id) values(?)");
            ps.setString(1, messageId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot insert " + messageId + " into " + tableName, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return 0;
    }

    @Override
    protected int queryForInt(String messageId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getDataSource().getConnection();
            ps = conn.prepareStatement("SELECT count(*) from " + tableName + " where message_id=?");
            ps.setString(1, messageId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            LOG.error("Cannot select from " + tableName, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return 0;
    }

}
