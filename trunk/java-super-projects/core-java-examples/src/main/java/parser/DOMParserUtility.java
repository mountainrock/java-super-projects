package parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*import com.lowagie.text.Chunk;
 import com.lowagie.text.Font;
 */
/**
 * Just a util class to print the tags and do some basic common things on a DOM
 * 
 */
public class DOMParserUtility {
	static List tagsAndAttributes = new ArrayList();

	public static List getTagsAndAttributesAsString(NodeList nodeList)
	{

		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.TEXT_NODE)
				continue;
			String datatype = "String";
			if (node.hasChildNodes()) {
				if (node.getChildNodes().getLength() > 1)
					datatype = "Complex";
			}
			String tagAndAttribute = node.getNodeName() + "||" + getAttributesAsString(node) + "|" + "|" + "|" + datatype + "||";

			if (!datatype.equals("Complex")) {
				final Node firstChild = node.getFirstChild();
				if (firstChild != null)
					tagAndAttribute = tagAndAttribute + firstChild.getNodeValue();
			}

			tagAndAttribute = tagAndAttribute + "|||";

			log(tagAndAttribute, false);
			tagsAndAttributes.add(tagAndAttribute);
			getTagsAndAttributesAsString(node.getChildNodes());
		}

		return tagsAndAttributes;

	}

	private static void log(String str, boolean isError)
	{
		System.out.println(str);
	}

	private static String getAttributesAsString(Node node)
	{
		NamedNodeMap atr = node.getAttributes();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < atr.getLength(); i++) {
			stringBuffer.append(atr.item(i)).append(atr.getLength() > 1 ? "," : "");
		}
		return stringBuffer.toString();
	}

	/**
	 * Parses an XML file and returns a DOM document. If validating is true, the contents is validated against the DTD specified in the file.
	 */
	public static Document parseXmlFile(InputStream is, boolean validating) throws Exception
	{
		// Create a builder factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(validating);
		Document doc = factory.newDocumentBuilder().parse(is);
		return doc;
	}

	public static void main(String[] args) throws Exception
	{
		/*
		 * final String dirPath = "D:\\OSS\\Docs\\SampleXMLs\\XMLSync's\\Sathyam\\"; com.lowagie.text.Document itextRtfDocument = null;
		 * 
		 * String[] files = new String[1];//IOUtility.getFilesInDirectory(dirPath, ".xml"); files[0]="Service Network.xml"; String header[] = { "XML Tag", "Purpose", "Attributes", "Mandatory",
		 * "Cardinality", "Data Type", "Format", "Sample data", "Validations", "OSS DB Destination(If persisted)", "Comments" };
		 * 
		 * for (int i = 0; i < files.length; i++) { String fileName = files[i]; String filePath = dirPath + fileName; InputStream is = new FileInputStream(filePath); Document doc = parseXmlFile(is,
		 * false);
		 * 
		 * List listOfTagsAndDetails = getTagsAndAttributesAsString(doc.getDocumentElement().getChildNodes());
		 * 
		 * try { if (itextRtfDocument == null) itextRtfDocument = RtfTOCandCellborders.openRTFDocument(null); } catch (FileNotFoundException e) { e.printStackTrace(); } itextRtfDocument.add(new
		 * Chunk(fileName,new Font(Font.BOLD))); itextRtfDocument.add(RtfTOCandCellborders.createTableForTokens(listOfTagsAndDetails, "\\|", header)); itextRtfDocument.newPage();
		 * 
		 * }
		 * 
		 * itextRtfDocument.close();
		 */

	}

}
