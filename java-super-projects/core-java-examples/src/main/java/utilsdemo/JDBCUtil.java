package utilsdemo;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import oracle.jdbc.driver.OracleDriver;


public class JDBCUtil {
	static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";

	static String DB_URL = "jdbc:oracle:thin:@db004.in.corp.company.com:1522:IRPADV01";// "jdbc:oracle:thin:passport_aquser/admin123@//10.34.131.45:1521/passportdev.dealerportal.irco.com";

	static String DB_USER = "";
	static String DB_PASSWORD = "";

	static Connection getConnection(String url, String user, String pwd)
	{
		Connection con = null;
		try {
			// Class.forName(DB_DRIVER).newInstance();
			DriverManager.setLogWriter(new PrintWriter(System.out));
			con = DriverManager.getConnection(url, user, pwd);

		} catch (Exception e) {
			throw new RuntimeException("Failed to get connection ", e);

		}
		return con;
	}

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
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}

		}
		return out;
	}

	public static List read(Connection con, String query)
	{
		List table = new ArrayList();
		Statement statement = null;
		try {
			statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);

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
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return table;

	}

	
	public static void main(String[] args)
	{
		// OracleDriver o= new OracleDriver();

		final Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
		// String query = "INSERT INTO VALID_ITEM (ID, NAME, QUANTITY,PRICE)" + "VALUES ( 1,'sandeep' ,10 ,22 )";
		String query = "select * from valid_item";
		System.out.println(JDBCUtil.read(connection, query));

	}

}
