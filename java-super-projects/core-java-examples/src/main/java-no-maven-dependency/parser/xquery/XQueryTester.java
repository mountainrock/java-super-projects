package parser.xquery;


import javax.xml.namespace.QName;
import java.util.Properties;

import com.ddtek.xquery3.XQConnection;
import com.ddtek.xquery3.XQException;
import com.ddtek.xquery3.XQExpression;
import com.ddtek.xquery3.XQItemType;
import com.ddtek.xquery3.XQSequence;
import com.ddtek.xquery3.xqj.DDXQDataSource;
/**
 * REf: http://www.ibm.com/developerworks/xml/library/x-xjavaxquery/
 * @author sandeep
 *
 */
public class XQueryTester {

  // Filename for XML document to query
  private String filename;

  // Data Source for querying
  private DDXQDataSource dataSource;
 
  // Connection for querying
  private XQConnection conn;

  public XQueryTester(String filename) {
    this.filename = filename;
  }

  public void init() throws XQException {
    dataSource = new DDXQDataSource();
    conn = dataSource.getConnection();
  }

  public String query(String queryString) throws XQException {
    XQExpression expression = conn.createExpression();
    expression.bindString(new QName("docName"), filename,
      conn.createAtomicType(XQItemType.XQBASETYPE_STRING));
    XQSequence results = expression.executeQuery(queryString);
    return results.getSequenceAsString(new Properties());
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: java ibm.dw.xqj.XQueryTester [XML filename]");
      System.exit(-1);
    }

    try {
      String xmlFilename = args[0];
      XQueryTester tester = new XQueryTester(xmlFilename);
      tester.init();

      final String sep = System.getProperty("line.separator");
      String queryString = 
        "declare variable $docName as xs:string external;" + sep +
        "      for $cd in doc($docName)/CATALOG/CD " +
        "    where $cd/YEAR > 1980 " +
        "      and $cd/COUNTRY = 'USA' " +
        " order by $cd/YEAR " +
        "   return " +
        "<cd><title>{$cd/TITLE/text()}</title>" + 
        " <year>{$cd/YEAR/text()}</year></cd>";
      System.out.println(tester.query(queryString));
    } catch (Exception e) {
      e.printStackTrace(System.err);
      System.err.println(e.getMessage());
    }
  }
}
