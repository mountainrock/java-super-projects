package parser;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;

public class XOMParser {
	public static void main(String[] args) throws Exception
	{
		Builder b = new Builder();
		String path = "D:\\ProjectRelated\\iweek\\layouts.xml";
		// test1();
		Document d1 = b.build(new File(path));
		Set portletsForIweek = new HashSet();
		Elements childElements = d1.getRootElement().getChildElements("layout");
		for (int i = 0; i < childElements.size() - 1; i++) {
			Element element = (Element) childElements.get(i);
			Element typeSettings = element.getFirstChildElement("type-settings");
			Element friendlyUrl = element.getFirstChildElement("friendly-url");
			if (friendlyUrl.getChildCount() <= 0) {
				System.out.println("No friendly URL ...skipping");
				continue;
			}
			Text freindlyURLNode = (Text) friendlyUrl.getChild(0);
			System.out.println(freindlyURLNode.getValue());

			Text textSetting = (Text) typeSettings.getChild(0);
			String strValueSetting = textSetting.getValue();
			// System.out.println(strValueSetting);
			String lines[] = strValueSetting.split("\n");
			for (int j = 0; j < lines.length; j++) {
				String line = lines[j];
				if (line.indexOf("column-") >= 0) {
					System.out.print(line.substring(0, 9));
					String value = line.substring(9);
					// System.out.println(value);
					String values[] = value.split(",");
					List list = new ArrayList();
					for (int k = 0; k < values.length; k++) {
						String splitValue = values[k];
						splitValue = splitValue.substring(0, splitValue.indexOf("_"));

						list.add(splitValue);
						portletsForIweek.add(splitValue);
					}
					System.out.println(list);
				}

			}
			System.out.println("*********************************\r\n");

		}
		System.out.println(portletsForIweek);
		System.out.println(portletsForIweek.size());
		System.out.println(childElements.size());
	}

	private static void test1() throws ParsingException, ValidityException, IOException
	{
		Builder b = new Builder();
		Document d1 = b.build(new File("D:\\cmp\\workspace\\cmp\\trunk\\web2\\src\\webapp\\WEB-INF\\context\\applicationContext-hibernate.xml"));
		System.out.println(d1);
		Element child = new Element("child");
		d1.getRootElement().appendChild(child);
		System.out.println(d1.toXML());

		BigInteger low = BigInteger.ONE;
		BigInteger high = BigInteger.ONE;

		Element root = new Element("Fibonacci_Numbers");
		for (int i = 1; i <= 10; i++) {
			Element fibonacci = new Element("fibonacci");
			fibonacci.addAttribute(new Attribute("hi", "hello"));
			fibonacci.appendChild(low.toString());
			root.appendChild(fibonacci);

			BigInteger temp = high;
			high = high.add(low);
			low = temp;
		}
		Document doc = new Document(root);

		System.out.println(doc.toXML());
	}

}
