package JDBC;

import org.apache.log4j.helpers.LogLog;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Performs JDBC / Database operations dynamically with column name / value pairs
 * in Map form.
 */
public class JDBCMap
{
  Connection conn;
  String tableName;
  public JDBCMap(Connection conn, String tableName) {
    this.conn = conn;
    this.tableName = tableName;
  }

  /**
   * Insert a row into the database.
   *
   * <p>The given Map is a set of column-name / column-value pairs for to be inserted
   * in the given tableName.
   *
   * @param columnNameValuePairs a Map whose keys correspond to column names,
   * and whose values correspond to the column values to be inserted.
   * @return a non-negative number that represents the number of rows inserted.
   * This value is typically 1.
   * @throws java.sql.SQLException in the event that an error occurs.
   */
  public int insertMapToDatabase(Map columnNameValuePairs) throws SQLException
  {
    String sqlString = buildSqlString(columnNameValuePairs);
//    System.out.println(sql.toString());
    PreparedStatement pstmt = conn.prepareStatement(sqlString);
    try {
      int colNo = 1;
      for (Iterator iterator = getNonNullEntrySetIterator(columnNameValuePairs); iterator.hasNext();) {
        Map.Entry entry = (Map.Entry) iterator.next();
        String colName = entry.getKey().toString();
        Object colValue = entry.getValue();
        // todo type conversion from string to ???
        try {
          pstmt.setObject(colNo, colValue);
        } catch (SQLException e) {
          LogLog.error("Error setting object value: "+tableName+"."+colName+"="+colValue, e);
        } finally {
          colNo++;
        }
      }
      int rows = pstmt.executeUpdate();
      if (rows <=0) {
        LogLog.error(rows + " rows inserted by "+sqlString);
      }
      return rows;
    } finally {
      try { pstmt.close(); }
      catch (SQLException e) { LogLog.error("Error closing pstmt", e); }
    }
  }

  String buildSqlString(Map columnNameValuePairs)
  {
    StringBuffer sql = new StringBuffer("insert into " + tableName + "(");
    StringBuffer valueString = new StringBuffer();
    for (Iterator iterator = getNonNullEntrySetIterator(columnNameValuePairs); iterator.hasNext();) {
      Map.Entry entry = (Map.Entry) iterator.next();
      String colName = entry.getKey().toString();
//      Object colValue = entry.getValue();
      if (valueString.length() > 0) {
        sql.append(',');
        valueString.append(',');
      }
      sql.append(colName);
      valueString.append('?');
    }
    sql.append(") VALUES (").append(valueString).append(')');
    String sqlString = sql.toString();
    return sqlString;
  }

  private Iterator getNonNullEntrySetIterator(final Map logMessageProperties)
  {
    return new Iterator()
    {
      Iterator iterator = logMessageProperties.entrySet().iterator();
      Map.Entry nextVal;

      public boolean hasNext()
      {
        if (nextVal != null) {
          return true;
        }
        while (iterator.hasNext()) {
          nextVal = (Map.Entry) iterator.next();
          if (nextVal.getValue() != null) {
            return true;
          }
        }
        return false;
      }

      public Object next()
      {
        if (nextVal == null) throw new NoSuchElementException();
        Map.Entry result = nextVal;
        nextVal = null;
        return result;
      }

      public void remove()
      {
        throw new UnsupportedOperationException("remove not supported");
      }
    };
  }

}
