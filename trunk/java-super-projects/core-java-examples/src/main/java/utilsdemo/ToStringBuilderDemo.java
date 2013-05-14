/**
 * 
 */
package utilsdemo;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


public class ToStringBuilderDemo extends Master {
	int a = 0;
	boolean b = false;
	Set set = new HashSet();

	
	public static void main(String[] args)
	{
		ToStringBuilderDemo toStringBuilderDemo = new ToStringBuilderDemo();
		toStringBuilderDemo.set.add(new Master());
		System.out.println(toStringBuilderDemo);
		// System.out.println(new Master());
	}

	public String toString()
	{
		return new ToStringBuilder(this).append(a).append(b).append(set).append(super.toString()).toString();
	}
}

class Master {
	String name = "master";

	public String toString()
	{

		return new ToStringBuilder(this).append(name).toString();
	}
}