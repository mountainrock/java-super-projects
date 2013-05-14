package velocity;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import utils.VelocityUtil;
import utils.XpathUtils;

public class Maven1ToMaven2Convertor {

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.err.println("Invalid argument. Please enter maven.xml path");
		}
		String xmlResourcePath = args[0];
		Document doc = XpathUtils.buildXMLDocument(xmlResourcePath);
		String xpathExpression = "//dependency";
		NodeList dependencies = XpathUtils.getNodeList(doc, xpathExpression);
		VelocityContext context = new VelocityContext();
		populateDependencies(dependencies, context);

		populateModuleAttributes(doc, context);
		
		String templateLocation="src/main/resources/templates/maven1Tomaven2Convert.vm";
		String result = VelocityUtil.applyTemplate(templateLocation, context);
		System.out.println(result);
		
	}

	private static void populateModuleAttributes(Document doc, VelocityContext context) {
		String xpathExpressionModule = "//module";
		NodeList modules = XpathUtils.getNodeList(doc, xpathExpressionModule);
		NamedNodeMap moduleAttributes = modules.item(0).getAttributes();
		
		context.put("groupId", moduleAttributes.getNamedItem("groupId").getNodeValue());
		context.put("artifactId", moduleAttributes.getNamedItem("artifactId").getNodeValue());
		context.put("name", moduleAttributes.getNamedItem("artifactId").getNodeValue());
		context.put("description", moduleAttributes.getNamedItem("artifactId").getNodeValue());
		context.put("version", moduleAttributes.getNamedItem("version").getNodeValue());
	}

	private static void populateDependencies(NodeList dependencies, VelocityContext context) {
		List attributeList = new ArrayList();

		for (int i = 0; i < dependencies.getLength(); i++) {
			attributeList.add(dependencies.item(i).getAttributes());
		}
		context.put("attributes", attributeList);
	}

}
