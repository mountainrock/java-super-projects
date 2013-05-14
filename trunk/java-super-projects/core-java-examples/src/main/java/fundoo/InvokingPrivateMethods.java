/**
 * 
 */
package fundoo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class InvokingPrivateMethods {

	
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Class clz = Privacy.class;
		Method m = clz.getDeclaredMethod("myMethod", new Class[] {});
		m.setAccessible(true);

		Privacy pr = new Privacy();

		ClassLoader cl = pr.getClass().getClassLoader();
		m.invoke(cl, new Object[] {});

		System.out.println("Bingo a class member with any modifier can be invoked by reflection...");
	}

}

class Privacy {
	static private void myMethod()
	{
		System.out.println("I am a private method and can be called only by 'this'");
	}
}