package parser;

import com.sun.tools.xjc.XJCFacade;
/**
 * Ref:http://publib.boulder.ibm.com/infocenter/wasinfo/v7r0/index.jsp?topic=/com.ibm.websphere.express.doc/info/exp/ae/twbs_jaxbschema2java.html
 * @author Administrator
 *
 */
public class XSD2Java {
	public static void main(String[] args) throws Throwable {
		XJCFacade facade = new XJCFacade();
		String[] params={"D:/Backup/downloads/archive-interchange-xsd-1.1/archivearticle.xsd"};
		facade.main(params);
	}
}
