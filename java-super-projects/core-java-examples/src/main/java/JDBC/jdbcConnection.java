/*
 * Created on Jan 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package JDBC;


import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class jdbcConnection {
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	DatabaseMetaData dm = null;

	PrintWriter pw = null;

	void loadDriver(String driver)
	{
		try {
			Driver d = (Driver) Class.forName(driver).newInstance();
			Class.forName(driver);
		} catch (Exception e) {
			System.out.println("No Class found " + e.toString());
		}
	}

	void getConnection(String dburl, String login, String pwd)
	{
		try {
			con = DriverManager.getConnection(dburl, login, pwd);
			DriverManager.setLogWriter(pw);
		} catch (SQLException e) {
			System.out.println("No Connection found " + e.toString());
		}
	}

	void closeConnection()
	{
		System.out.println();
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("No Connection found " + e.toString());
		}
	}

	void insert(String table, Map<String, Object> values) throws Exception
	{
		String paramNames = "";
		String paramValues = "";
		String params = "";
		for (Iterator<Map.Entry<String, Object>> iterator = values.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<String, Object> type = iterator.next();

			Object value = type.getValue();
			String quote = "";
			if (value instanceof String) {
				quote = "'";

			}
			paramNames = paramNames + type.getKey() + ",";
			paramValues = paramValues + quote + value + quote + ",";

		}
		paramNames = "(" + paramNames.substring(0, paramNames.lastIndexOf(",")) + ")";
		paramValues = "(" + paramValues.substring(0, paramValues.lastIndexOf(",")) + ")";
		params = paramNames + " values " + paramValues;

		String sql = "insert into " + table + " " + params + "";
		System.out.println("sql = " + sql);
		stmt = con.createStatement();
		stmt.executeUpdate(sql);
		System.out.println("inserted record into [table =" + table + ", values =" + values + "]");

	}

	void execute(String sql) throws Exception
	{
		stmt = con.createStatement();
		stmt.executeUpdate(sql);
		System.out.println("executed [" + sql + "]");
		con.commit();
	}

	/**
	 * 
	 * @param q
	 */
	void query(String q)
	{
		try {

			stmt = con.createStatement();

			dm = con.getMetaData();

			System.out.println("Driver Information");
			System.out.println("\tDriver Name: " + dm.getDriverName());
			System.out.println("\tDriver Version: " + dm.getDriverVersion());
			System.out.println("\nDatabase Information ");
			System.out.println("\tDatabase Name: " + dm.getDatabaseProductName());
			System.out.println("\tDatabase Version: " + dm.getDatabaseProductVersion());
			System.out.println("Avalilable Catalogs ");

			rs = dm.getCatalogs();
			while (rs.next()) {
				System.out.println("\tTable: " + rs.getString(1));
			}
			rs = stmt.executeQuery(q);

			ResultSetMetaData rsmd = rs.getMetaData();
			int colLength = rsmd.getColumnCount();
			System.out.println("Result :");
			while (rs.next()) {
				for (int i = 1; i <= colLength; i++) {
					System.out.print(rsmd.getColumnLabel(i) + "\t");
					System.out.println(rs.getString(i));
				}
				System.out.println("------------------------------------");
			}
			rs.close();

		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.toString());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception
	{
		jdbcConnection test = new jdbcConnection();
		// <property name="driverClassName" value="oracle.jdbc.driver.OracleDriver"/>
		// <property name="url" value="jdbc:oracle:thin:@db003.india.xyz.com:1521:NSTCDV01"/>
		// <property name="username" value="CSG_OWNER"/>
		// <property name="password" value="CSG_OWNER"/>
		// <property name="maxIdle" value="2"/>
		// <property name="minIdle" value="0"/>
		// <property name="maxActive" value="2"/>
		// MS-Access test
		test.loadDriver("oracle.jdbc.driver.OracleDriver");
		String url = "jdbc:oracle:thin:@costa.lsc.net:1521:examplerep1";

		test.getConnection(url, "webrepuser", "ninjahamster");
		// test.query("SELECT * from article");

		// String table = "article";
		/*
		 * for (int i = 0; i < 1000000; i++) { Map<String, Object> values = new HashMap<String, Object>(); values.put("ArticleID", "abcabcabc" + i); values.put("ArticleType", "typetypetype" + i);
		 * 
		 * test.insert(table, values);
		 * 
		 * }
		 */
		/*
		 * //ORacle test test.loadDriver("oracle.jdbc.driver.OracleDriver"); test.getConnection("jdbc:oracle:thin:@db003.india.xyz.com:1521:NSTCDV01","CSG_OWNER","CSG_OWNER");
		 * test.query("select * from c_censusdata");
		 */

		// String query_temp ="INSERT INTO C_ACCOUNTVERSION ( ACCOUNTVERSIONID, ACCOUNTID, YEAR, CREATEDBY, CREATEDDATE,"
		// + "MODIFIEDBY, LASTMODIFIED, DESCR, ACCOUNTVERSION, ISSETUPCOMPLETE,"
		// + "VERSION_NUMBER ) VALUES ("
		// + "1, 1,  TO_Date( '11/10/2004 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'company',  TO_Date( '12/12/2004 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM')"
		// + ", 'company',  TO_Date( '05/15/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'ADD NEW CLASS'"
		// + ", 2, 'Y', 1)";
		test.closeConnection();

	}
}
