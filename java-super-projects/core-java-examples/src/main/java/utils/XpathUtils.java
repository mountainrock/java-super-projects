package utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XpathUtils {

	public static NodeList getNodeList(Document doc, String xpathExpression) {
		try {
			XPathExpression expr = buildXpathExpression(xpathExpression); //TODO: can we cache expression?
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			return result != null ? (NodeList) result : null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static XPathExpression buildXpathExpression(String xpathExpression) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			return xpath.compile(xpathExpression);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Document buildXMLDocument(String xmlResourcePath) {
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			return builder.parse(xmlResourcePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
