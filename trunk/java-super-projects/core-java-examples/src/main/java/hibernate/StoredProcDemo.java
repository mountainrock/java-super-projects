package hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.jdbc.core.CallableStatementCreator;

public class StoredProcDemo {
	private void callProc()
	{
		CallableStatementCreator callableStmt = new CallableStatementCreator() {
			public CallableStatement createCallableStatement(Connection con) throws SQLException
			{
				CallableStatement cs = con.prepareCall("{ call example_access_pkg.check_user(?, ?) }");
				cs.setInt(1, Types.INTEGER);
				cs.setString(2, "");
				return cs;
			}
		};
		// accessStoredProcedure.getJdbcTemplate().call(callableStmt, null);
	}
}
