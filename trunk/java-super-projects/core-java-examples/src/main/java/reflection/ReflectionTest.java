package reflection;

import java.lang.reflect.Method;

public class ReflectionTest {

	public static void main(String[] args)
	{

		A aObj = new A();

		String[] keyVals = {};

		Class clazz = A.class;

		String methodName = "method" + clazz.getSimpleName();

		Class strType[] = new Class[] { String[].class };

		try {

			Method method = clazz.getDeclaredMethod(methodName, strType);
			method.setAccessible(true);
			method.invoke(aObj, new Object[] { keyVals });

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}

class A {

	static private void methodA(String s[])
	{

		System.out.println("i am in method");

	}

	void methodA2(String[] s)
	{

		System.out.println("i am in method");

	}

}
