/*
package hibernate.debug;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ProxyDataSource implements DataSource
{
	DataSource delegate;

	public void setDelegate(DataSource newDelegate)
	{
		delegate = newDelegate;
	}

	public Connection getConnection() throws SQLException
	{
		Connection actualConnection = delegate.getConnection();
		return new ProxyConnection(actualConnection);
	}

	public Connection getConnection(String arg0, String arg1) throws SQLException
	{
		return delegate.getConnection(arg0, arg1);
	}

	public int getLoginTimeout() throws SQLException
	{
		return delegate.getLoginTimeout();
	}

	public PrintWriter getLogWriter() throws SQLException
	{
		return delegate.getLogWriter();
	}

	public void setLoginTimeout(int arg0) throws SQLException
	{
		delegate.setLoginTimeout(arg0);
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException
	{
		delegate.setLogWriter(arg0);
	}
	
	
}
*/