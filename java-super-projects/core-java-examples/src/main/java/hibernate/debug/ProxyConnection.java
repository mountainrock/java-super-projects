/*
package hibernate.debug;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

public class ProxyConnection implements Connection
{
	
	Connection delegate;
	
	public ProxyConnection(Connection newDelegate)
	{
		delegate = newDelegate;
	}

	public void clearWarnings() throws SQLException
	{
		delegate.clearWarnings();
	}

	public void close() throws SQLException
	{
		delegate.close();
	}

	public void commit() throws SQLException
	{
		delegate.commit();
	}

	public Statement createStatement() throws SQLException
	{
		return delegate.createStatement();
	}

	public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException
	{
		return delegate.createStatement(arg0, arg1, arg2);
	}

	public Statement createStatement(int arg0, int arg1) throws SQLException
	{
		return delegate.createStatement(arg0, arg1);
	}

	public boolean getAutoCommit() throws SQLException
	{
		return delegate.getAutoCommit();
	}

	public String getCatalog() throws SQLException
	{
		return delegate.getCatalog();
	}

	public int getHoldability() throws SQLException
	{
		return delegate.getHoldability();
	}

	
	public DatabaseMetaData getMetaData() throws SQLException
	{
		return delegate.getMetaData();
	}

	
	public int getTransactionIsolation() throws SQLException
	{
		return delegate.getTransactionIsolation();
	}

	
	public Map getTypeMap() throws SQLException
	{
		return delegate.getTypeMap();
	}

	
	public SQLWarning getWarnings() throws SQLException
	{
		return delegate.getWarnings();
	}

	
	public boolean isClosed() throws SQLException
	{
		return delegate.isClosed();
	}

	
	public boolean isReadOnly() throws SQLException
	{
		return delegate.isReadOnly();
	}

	
	public String nativeSQL(String arg0) throws SQLException
	{
		return delegate.nativeSQL(arg0);
	}

	
	public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException
	{
		return delegate.prepareCall(arg0, arg1, arg2, arg3);
	}

	
	public CallableStatement prepareCall(String arg0, int arg1, int arg2) throws SQLException
	{
		return delegate.prepareCall(arg0, arg1, arg2);
	}

	
	public CallableStatement prepareCall(String arg0) throws SQLException
	{
		return delegate.prepareCall(arg0);
	}

	
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException
	{
		return delegate.prepareStatement(arg0, arg1, arg2, arg3);
	}

	
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2) throws SQLException
	{
		return delegate.prepareStatement(arg0, arg1, arg2);
	}

	
	public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException
	{
		return delegate.prepareStatement(arg0, arg1);
	}

	
	public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException
	{
		return delegate.prepareStatement(arg0, arg1);
	}

	
	public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException
	{
		return delegate.prepareStatement(arg0, arg1);
	}

	
	public PreparedStatement prepareStatement(String arg0) throws SQLException
	{
		PreparedStatement actualStatement = delegate.prepareStatement(arg0);
		return new StatefulProxyStatement(arg0,actualStatement);
	}

	
	public void releaseSavepoint(Savepoint arg0) throws SQLException
	{
		delegate.releaseSavepoint(arg0);
	}

	
	public void rollback() throws SQLException
	{
		delegate.rollback();
	}

	
	public void rollback(Savepoint arg0) throws SQLException
	{
		delegate.rollback(arg0);
	}

	
	public void setAutoCommit(boolean arg0) throws SQLException
	{
		delegate.setAutoCommit(arg0);
	}

	
	public void setCatalog(String arg0) throws SQLException
	{
		delegate.setCatalog(arg0);
	}

	
	public void setHoldability(int arg0) throws SQLException
	{
		delegate.setHoldability(arg0);
	}

	
	public void setReadOnly(boolean arg0) throws SQLException
	{
		delegate.setReadOnly(arg0);
	}

	
	public Savepoint setSavepoint() throws SQLException
	{
		return delegate.setSavepoint();
	}

	
	public Savepoint setSavepoint(String arg0) throws SQLException
	{
		return delegate.setSavepoint(arg0);
	}

	
	public void setTransactionIsolation(int arg0) throws SQLException
	{
		delegate.setTransactionIsolation(arg0);
	}

	
	public void setTypeMap(Map arg0) throws SQLException
	{
		delegate.setTypeMap(arg0);
	}
	
	
	
}
*/