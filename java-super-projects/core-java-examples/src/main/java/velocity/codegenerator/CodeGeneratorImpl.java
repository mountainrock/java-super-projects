/**
 * 
 */
package velocity.codegenerator;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;


public class CodeGeneratorImpl implements IPortletCodeGenerator {
	public String applyTemplates(List templateList, VelocityContext context) throws Exception
	{
		VelocityEngine ve = new VelocityEngine();
		ve.init();

		StringBuffer output = new StringBuffer();
		for (Iterator iter = templateList.iterator(); iter.hasNext();) {
			Template t = (Template) iter.next();
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			output.append("\r\n********************" + t.getName().toUpperCase() + " BEGINS **********\r\n");
			output.append(writer.toString());
			output.append("\r\n********************" + t.getName().toUpperCase() + " ENDS **********\r\n");
		}

		return output.toString();
	}

}
