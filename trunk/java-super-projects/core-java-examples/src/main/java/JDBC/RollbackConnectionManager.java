package JDBC;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Sets connection's auto commit to false so that data is not inserted into DB.
 * 
 */
class RollbackConnectionManager {
	private Connection connection;
	private boolean autoCommit;

	public RollbackConnectionManager(Connection connection) {
		this.connection = connection;
		try {
			autoCommit = connection.getAutoCommit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void setAutoCommitToFalse()
	{
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void rollback()
	{
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void resetAutoCommit()
	{
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}