package ajax.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class DemoXStream {
	public static void main(String[] args)
	{

		Product product = new Product("Apple", 123, 23.00);

		// Write to JSON with the Jettison-based implementation
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.alias("product", Product.class);

		System.out.println(xstream.toXML(product));

		// Write to JSON with the self-contained JSON driver
		xstream = new XStream(new JsonHierarchicalStreamDriver());
		xstream.alias("product", Product.class);
		System.out.println(xstream.toXML(product));

	}
}
