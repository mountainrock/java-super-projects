package parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import utils.XpathUtils;

/**
 * Refer: http://www.ibm.com/developerworks/library/x-javaxpathapi.html
 * @author sandeep
 *
 */
public class XpathDemo {
	public static void main(String[] args) {

		String xmlResourcePath = "src/main/resources/books.xml";
		Document doc = XpathUtils.buildXMLDocument(xmlResourcePath);

		String xpathExpression = "//book[author='Neal Stephenson']/title/text()";
		NodeList nodes = XpathUtils.getNodeList(doc, xpathExpression);
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println(nodes.item(i).getNodeValue());
		}

	}

}
