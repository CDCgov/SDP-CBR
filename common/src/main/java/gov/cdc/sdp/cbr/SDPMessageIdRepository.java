package gov.cdc.sdp.cbr;

import org.apache.camel.processor.idempotent.jdbc.AbstractJdbcMessageIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

public class SDPMessageIdRepository extends AbstractJdbcMessageIdRepository<String> {

  private static final Logger LOG = LoggerFactory.getLogger(SDPMessageIdRepository.class);

  final String tableName;

  /**
   * Creates a SDPMessageIdRepository.
   * @param ds The data source that this repository will use
   * @param processorName Message processor to use before storing the message
   * @param tableName database table name
   * @throws IllegalArgumentException if an invalid table name is provided
   */
  public SDPMessageIdRepository(DataSource ds, String processorName,
      String tableName) throws IllegalArgumentException {
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
      ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName
          + " (message_id varchar(255) primary key)");
      ps.executeUpdate();
    } catch (SQLException createException) {
      LOG.error("Cannot initialize -- SQL error creating table.", createException);
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException closeException) {
        LOG.error("Cannot initialize -- SQL error closing the connection.",
            closeException);
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
    } catch (SQLException deleteException) {
      LOG.error("Cannot delete from " + tableName, deleteException);
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException closeException) {
        LOG.error("Cannot delete -- SQL error closing the connection.",
            closeException);
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
    } catch (SQLException deleteException) {
      LOG.error("Cannot delete from " + tableName + " with message_id "
          + messageId, deleteException);
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException closeException) {
        LOG.error("Cannot delete -- SQL error closing the connection.",
            closeException);
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
    } catch (SQLException insertException) {
      LOG.error("Cannot insert " + messageId + " into " + tableName, insertException);
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException closeException) {
        LOG.error("Cannot insert -- SQL error closing the connection.",
            closeException);
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
    } catch (SQLException queryException) {
      LOG.error("Cannot select from " + tableName, queryException);
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException closeException) {
        LOG.error("Cannot queryForInt -- SQL error closing the connection.",
            closeException);
      }
    }
    return 0;
  }

}
