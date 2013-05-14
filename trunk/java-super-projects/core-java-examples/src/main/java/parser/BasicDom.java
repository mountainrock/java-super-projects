package parser;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class BasicDom {

	public static void main(String[] args) throws Exception
	{
		String sampleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<stocks>" + "<url>" + "<site>secure.icicidirect.com</site>" + "<path>/trading/equity/trading_stock_quote.asp</path>"
				+ "</url>" + "<shares>" + "<share><name>Wipro</name><code>WIPRO</code></share>" + "<share><name>i-gate</name><code>MASSYS</code></share>" + "</shares></stocks>";
		System.out.println(sampleXML);
		InputStream is = new StringBufferInputStream(sampleXML);
		// final String filePath = "D:\\CSGROOT\\CSG\\1.0\\Application\\CM\\build\\WEB-INF\\config\\CA-1.xml";
		// InputStream is= new FileInputStream(filePath);
		Document doc = parseXmlFile(is, false);

		System.out.println(doc.getElementsByTagName("site").item(0).getFirstChild().getNodeValue());
		System.out.println(doc.getElementsByTagName("path").item(0).getFirstChild().getNodeValue());

		final NodeList shareList = doc.getElementsByTagName("share");
		for (int i = 0; i < shareList.getLength(); i++) {
			System.out.println(shareList.item(i).getFirstChild().getFirstChild().getNodeValue());
			System.out.println(shareList.item(i).getLastChild().getFirstChild().getNodeValue());

		}
	}

	// Parses an XML file and returns a DOM document.
	// If validating is true, the contents is validated against the DTD
	// specified in the file.
	public static Document parseXmlFile(InputStream is, boolean validating) throws Exception
	{
		// Create a builder factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validating);

		// Create the builder and parse the file

		Document doc = factory.newDocumentBuilder().parse(is);
		return doc;
	}
}
