/*package hibernate.debug;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatefulProxyStatement implements PreparedStatement
{
	PreparedStatement delegate;
	String sql;
	List parameters = new ArrayList();
	
	public StatefulProxyStatement(String newSQL,PreparedStatement newDelegate)
	{
		sql = newSQL;
		delegate = newDelegate;
	}

	
	public void addBatch() throws SQLException
	{
		delegate.addBatch();
	}

	
	public void addBatch(String arg0) throws SQLException
	{
		delegate.addBatch(arg0);
	}

	
	public void cancel() throws SQLException
	{
		delegate.cancel();
	}

	
	public void clearBatch() throws SQLException
	{
		delegate.clearBatch();
	}

	
	public void clearParameters() throws SQLException
	{
		delegate.clearParameters();
	}

	
	public void clearWarnings() throws SQLException
	{
		delegate.clearWarnings();
	}

	
	public void close() throws SQLException
	{
		delegate.close();
	}

	
	public boolean execute() throws SQLException
	{
		return delegate.execute();
	}

	
	public boolean execute(String arg0, int arg1) throws SQLException
	{
		return delegate.execute(arg0, arg1);
	}

	
	public boolean execute(String arg0, int[] arg1) throws SQLException
	{
		return delegate.execute(arg0, arg1);
	}

	
	public boolean execute(String arg0, String[] arg1) throws SQLException
	{
		return delegate.execute(arg0, arg1);
	}

	
	public boolean execute(String arg0) throws SQLException
	{
		return delegate.execute(arg0);
	}

	
	public int[] executeBatch() throws SQLException
	{
		return delegate.executeBatch();
	}

	
	public ResultSet executeQuery() throws SQLException
	{
		ResultSet rs = delegate.executeQuery();
		Throwable callSequence = new Throwable();
		PrintStream logger = LogWriter.getInstance().logger();
		logger.println("SQL ["+sql+"] with parameters "+parameters+" at ");
		callSequence.printStackTrace(logger);
		return rs;
	}

	
	public ResultSet executeQuery(String arg0) throws SQLException
	{
		ResultSet rs = delegate.executeQuery(arg0);
		Throwable callSequence = new Throwable();
		PrintStream logger = LogWriter.getInstance().logger();
		logger.println("SQL ["+sql+"] with parameters "+parameters+" at ");
		callSequence.printStackTrace(logger);
		return rs;
	}

	
	public int executeUpdate() throws SQLException
	{
		int numRowsUpdated = delegate.executeUpdate();
		Throwable callSequence = new Throwable();
		PrintStream logger = LogWriter.getInstance().logger();
		logger.println("SQL ["+sql+"] with parameters "+parameters+" updated ["+numRowsUpdated+"] at ");
		callSequence.printStackTrace(logger);
		return numRowsUpdated;
	}

	
	public int executeUpdate(String arg0, int arg1) throws SQLException
	{
		return delegate.executeUpdate(arg0, arg1);
	}

	
	public int executeUpdate(String arg0, int[] arg1) throws SQLException
	{
		return delegate.executeUpdate(arg0, arg1);
	}

	
	public int executeUpdate(String arg0, String[] arg1) throws SQLException
	{
		return delegate.executeUpdate(arg0, arg1);
	}

	
	public int executeUpdate(String arg0) throws SQLException
	{
		return delegate.executeUpdate(arg0);
	}

	
	public Connection getConnection() throws SQLException
	{
		return delegate.getConnection();
	}

	
	public int getFetchDirection() throws SQLException
	{
		return delegate.getFetchDirection();
	}

	
	public int getFetchSize() throws SQLException
	{
		return delegate.getFetchSize();
	}

	
	public ResultSet getGeneratedKeys() throws SQLException
	{
		return delegate.getGeneratedKeys();
	}

	
	public int getMaxFieldSize() throws SQLException
	{
		return delegate.getMaxFieldSize();
	}

	
	public int getMaxRows() throws SQLException
	{
		return delegate.getMaxRows();
	}

	
	public ResultSetMetaData getMetaData() throws SQLException
	{
		return delegate.getMetaData();
	}

	
	public boolean getMoreResults() throws SQLException
	{
		return delegate.getMoreResults();
	}

	
	public boolean getMoreResults(int arg0) throws SQLException
	{
		return delegate.getMoreResults(arg0);
	}

	
	public ParameterMetaData getParameterMetaData() throws SQLException
	{
		return delegate.getParameterMetaData();
	}

	
	public int getQueryTimeout() throws SQLException
	{
		return delegate.getQueryTimeout();
	}

	
	public ResultSet getResultSet() throws SQLException
	{
		return delegate.getResultSet();
	}

	
	public int getResultSetConcurrency() throws SQLException
	{
		return delegate.getResultSetConcurrency();
	}

	
	public int getResultSetHoldability() throws SQLException
	{
		return delegate.getResultSetHoldability();
	}

	
	public int getResultSetType() throws SQLException
	{
		return delegate.getResultSetType();
	}

	
	public int getUpdateCount() throws SQLException
	{
		return delegate.getUpdateCount();
	}

	
	public SQLWarning getWarnings() throws SQLException
	{
		return delegate.getWarnings();
	}

	
	public void setArray(int arg0, Array arg1) throws SQLException
	{
		delegate.setArray(arg0, arg1);
	}

	
	public void setAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException
	{
		delegate.setAsciiStream(arg0, arg1, arg2);
	}

	
	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException
	{
		parameters.add(arg1);
		delegate.setBigDecimal(arg0, arg1);
	}

	
	public void setBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException
	{
		delegate.setBinaryStream(arg0, arg1, arg2);
	}

	
	public void setBlob(int arg0, Blob arg1) throws SQLException
	{
		delegate.setBlob(arg0, arg1);
	}

	
	public void setBoolean(int arg0, boolean arg1) throws SQLException
	{
		delegate.setBoolean(arg0, arg1);
	}

	
	public void setByte(int arg0, byte arg1) throws SQLException
	{
		delegate.setByte(arg0, arg1);
	}

	
	public void setBytes(int arg0, byte[] arg1) throws SQLException
	{
		delegate.setBytes(arg0, arg1);
	}

	
	public void setCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException
	{
		delegate.setCharacterStream(arg0, arg1, arg2);
	}

	
	public void setClob(int arg0, Clob arg1) throws SQLException
	{
		delegate.setClob(arg0, arg1);
	}

	
	public void setCursorName(String arg0) throws SQLException
	{
		delegate.setCursorName(arg0);
	}

	
	public void setDate(int arg0, Date arg1, Calendar arg2) throws SQLException
	{
		parameters.add(arg2);
		delegate.setDate(arg0, arg1, arg2);
	}

	
	public void setDate(int arg0, Date arg1) throws SQLException
	{
		parameters.add(arg1);
		delegate.setDate(arg0, arg1);
	}

	
	public void setDouble(int arg0, double arg1) throws SQLException
	{
		parameters.add(new Double(arg1));
		delegate.setDouble(arg0, arg1);
	}

	
	public void setEscapeProcessing(boolean arg0) throws SQLException
	{
		delegate.setEscapeProcessing(arg0);
	}

	
	public void setFetchDirection(int arg0) throws SQLException
	{
		delegate.setFetchDirection(arg0);
	}

	
	public void setFetchSize(int arg0) throws SQLException
	{
		delegate.setFetchSize(arg0);
	}

	
	public void setFloat(int arg0, float arg1) throws SQLException
	{
		parameters.add(new Float(arg1));
		delegate.setFloat(arg0, arg1);
	}

	
	public void setInt(int arg0, int arg1) throws SQLException
	{
		parameters.add(new Integer(arg1));
		delegate.setInt(arg0, arg1);
	}

	
	public void setLong(int arg0, long arg1) throws SQLException
	{
		parameters.add(new Long(arg1));
		delegate.setLong(arg0, arg1);
	}

	
	public void setMaxFieldSize(int arg0) throws SQLException
	{
		delegate.setMaxFieldSize(arg0);
	}

	
	public void setMaxRows(int arg0) throws SQLException
	{
		delegate.setMaxRows(arg0);
	}

	
	public void setNull(int arg0, int arg1, String arg2) throws SQLException
	{
		delegate.setNull(arg0, arg1, arg2);
	}

	
	public void setNull(int arg0, int arg1) throws SQLException
	{
		delegate.setNull(arg0, arg1);
	}

	
	public void setObject(int arg0, Object arg1, int arg2, int arg3) throws SQLException
	{
		delegate.setObject(arg0, arg1, arg2, arg3);
	}

	
	public void setObject(int arg0, Object arg1, int arg2) throws SQLException
	{
		delegate.setObject(arg0, arg1, arg2);
	}

	
	public void setObject(int arg0, Object arg1) throws SQLException
	{
		parameters.add(arg1);
		delegate.setObject(arg0, arg1);
	}

	
	public void setQueryTimeout(int arg0) throws SQLException
	{
		delegate.setQueryTimeout(arg0);
	}

	
	public void setRef(int arg0, Ref arg1) throws SQLException
	{
		delegate.setRef(arg0, arg1);
	}

	
	public void setShort(int arg0, short arg1) throws SQLException
	{
		parameters.add(new Short(arg1));
		delegate.setShort(arg0, arg1);
	}

	
	public void setString(int arg0, String arg1) throws SQLException
	{
		parameters.add(arg1);
		delegate.setString(arg0, arg1);
	}

	
	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException
	{
		delegate.setTime(arg0, arg1, arg2);
	}

	
	public void setTime(int arg0, Time arg1) throws SQLException
	{
		delegate.setTime(arg0, arg1);
	}

	
	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2) throws SQLException
	{
		delegate.setTimestamp(arg0, arg1, arg2);
	}

	
	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException
	{
		delegate.setTimestamp(arg0, arg1);
	}

	
	public void setUnicodeStream(int arg0, InputStream arg1, int arg2) throws SQLException
	{
		delegate.setUnicodeStream(arg0, arg1, arg2);
	}

	
	public void setURL(int arg0, URL arg1) throws SQLException
	{
		delegate.setURL(arg0, arg1);
	}
	
	
}
*/