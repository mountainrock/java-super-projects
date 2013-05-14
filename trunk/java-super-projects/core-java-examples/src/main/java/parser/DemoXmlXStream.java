package parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.thoughtworks.xstream.XStream;

public class DemoXmlXStream {
	public static void main(String[] args)
	{
		 List<Product> products=new ArrayList<Product>();
		 
		 products.add(new Product("Orange", 123, 23.00,null));
		 products.add(new Product("Orange", 123, 23.00,null));
		Product product = new Product("Apple", 123, 23.00,products);

		// Write to JSON with the Jettison-based implementation
		XStream xstream = new XStream();
		xstream.alias("nodes", ArrayList.class);
		xstream.alias("product", Product.class);

		System.out.println(xstream.toXML(product));

		// Write to JSON with the self-contained JSON driver
		xstream = new XStream();
		xstream.alias("product", Product.class);
		System.out.println(xstream.toXML(product));
		Product fromXML = (Product) xstream.fromXML(xstream.toXML(product));
		System.out.println(fromXML);

	}
}

class Product {
	private String name;
	private long id;
	private double price;
	private List<Product> products;

	public Product(String name, long id, double price,List<Product> products) {
		this.name = name;
		this.id = id;
		this.price = price;
		this.products= products;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	@Override
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

}