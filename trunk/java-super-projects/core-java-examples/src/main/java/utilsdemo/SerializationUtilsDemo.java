/**
 * 
 */
package utilsdemo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;


public class SerializationUtilsDemo {

	
	public static void main(String[] args)
	{
		DustBin dummy = new DustBin();
		dummy.addDirt("dirt 1");
		byte[] b = SerializationUtils.serialize(dummy);
		dummy = (DustBin) SerializationUtils.deserialize(b);

		System.out.println(dummy.getAll());

	}

}

class DustBin implements Serializable {
	Set set = new HashSet();

	void addDirt(Object dirt)
	{
		set.add(dirt);
	}

	Set getAll()
	{
		return set;
	}

}
