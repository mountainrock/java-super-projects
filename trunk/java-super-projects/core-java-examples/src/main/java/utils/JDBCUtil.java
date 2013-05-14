package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class JDBCUtil {
	public static int insert(Connection con, String query)
	{
		Statement statement = null;
		int out = -1;
		try {
			statement = con.createStatement();
			out = statement.executeUpdate(query);
			con.commit();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}
		return out;
	}

	public static List read(Connection con, String query)
	{
		List table = new ArrayList();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(query);

			while (rs.next()) {
				List row = new ArrayList();

				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i + 1));
				}
				table.add(row);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		}
		return table;

	}

}
