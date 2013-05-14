package miscellaneous;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */


public class ListToArray {

	
	public static void main(String[] args)
	{
		List list = new ArrayList();
		list.add(new MyClass());

		MyClass myClass[] = new MyClass[list.size()];
		list.toArray(new MyClass[list.size()]);
		System.out.println(myClass.length);
	}

}

class MyClass {

}
