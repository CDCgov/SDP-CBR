package org.cdc.gov.sdp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.camel.processor.idempotent.jdbc.AbstractJdbcMessageIdRepository;

public class SDPMessageIdRepository extends AbstractJdbcMessageIdRepository<String> {

	final String tableName;

	public SDPMessageIdRepository(DataSource ds, String processorName, String tableName)
			throws IllegalArgumentException {
		super(ds, processorName);
		if (!Pattern.matches("[a-zA-Z][a-zA-Z0-9]*", tableName)) {
			throw new IllegalArgumentException("Invalid table name.");
		} else {
			this.tableName = tableName;
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
			// TODO: Log something.
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO: Log something.
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
			// TODO: Log something.
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO: Log something.
			}
		}
		return 0;
	}

	@Override
	protected int delete(String arg0) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getDataSource().getConnection();
			ps = conn.prepareStatement("DELETE FROM " + tableName + " where message_id=?");
			ps.setString(0, arg0);
			return ps.executeUpdate();
		} catch (SQLException e) {
			// TODO: Log something.
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO: Log something.
			}
		}
		return 0;
	}

	@Override
	protected int insert(String arg0) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getDataSource().getConnection();
			ps = conn.prepareStatement("INSERT INTO " + tableName + " (message_id) values(?)");
			ps.setString(0, arg0);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO: Log something.
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO: Log something.
			}
		}
		return 0;
	}

	@Override
	protected int queryForInt(String arg0) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getDataSource().getConnection();
			ps = conn.prepareStatement("SELECT count(*) from " + tableName + " where message_id=?");
			ResultSet rs = ps.executeQuery();
			return rs.getInt(0);
		} catch (SQLException e) {
			// TODO: Log something.
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO: Log something.
			}
		}
		return 0;
	}

}
