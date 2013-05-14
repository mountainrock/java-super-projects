/**
 * 
 */
package parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import IO.IOUtility;


public class SAXDemo {

	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, FactoryConfigurationError, IOException
	{

		// <xml><item><name>abc</name></item></xml>
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		MyHandler handler = new MyHandler();
		String str = IOUtility.readSystemInputLineByLine();
		InputStream is = new StringBufferInputStream(str);
		parser.parse(is, handler);
		System.out.println(handler.getItem());

	}

}

class MyHandler extends DefaultHandler {
	Item item = null;

	public void startDocument()
	{
		// System.out.println("Start document event called");
		item = new Item();
	}

	// sta
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException
	{
		log(qName);
		// Get the number of attribute
		int length = attrs.getLength();

		// Process each attribute
		for (int i = 0; i < length; i++) {
			// Get names and values for each attribute
			String name = attrs.getQName(i);
			String value = attrs.getValue(i);
			log(name);
			log(value);
			// The following methods are valid only if the parser is namespace-aware

			// The uri of the attribute's namespace
			String nsUri = attrs.getURI(i);

			// This is the name without the prefix
			String lName = attrs.getLocalName(i);
		}
		/*
		 * if (qName.equalsIgnoreCase("NAME")) { log(qName+attrs); item.setName(attrs.getValue(0)); } else if (qName.equalsIgnoreCase("QUANTITY")) { item.setQty(new Long(attrs.getValue(0))); } else if
		 * (qName.equalsIgnoreCase("PRICE")) { item.setPrice(new Double(attrs.getValue(0))); }
		 */

	}

	public Item getItem()
	{
		return item;
	}

	public void characters(char buf[], int offset, int len) throws SAXException
	{
		String s = new String(buf, offset, len);
		if (!s.trim().equals(""))
			log("CHARS:   " + s);
	}

	private void log(String s)
	{
		System.out.println(s);
	}
}

class Item {
	String name;
	Long qty;
	Double price;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Double getPrice()
	{
		return price;
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public Long getQty()
	{
		return qty;
	}

	public void setQty(Long qty)
	{
		this.qty = qty;
	}

	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
}