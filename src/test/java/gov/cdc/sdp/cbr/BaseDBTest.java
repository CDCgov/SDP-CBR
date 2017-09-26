package gov.cdc.sdp.cbr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class BaseDBTest {

	public void setupDb(DataSource ds) throws IOException, SQLException {
		String sql = new String(
				java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("./src/test/resources/sql/test_db.sql")));
		String[] commands = sql.split(";");
		Connection conn = ds.getConnection();
		for (int i = 0; i < commands.length; i++) {
			conn.createStatement().execute(commands[i]);
		}
		conn.close();
	}
}
