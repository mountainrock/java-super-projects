/**
 * 
 */
package utilsdemo;

import java.lang.reflect.Field;


public class ToStringGenerator {
	public static void main(String[] args)
	{
		Field[] f = Object.class.getDeclaredFields();
		StringBuffer buf = new StringBuffer("public String toString() {\r\n return new StringBuffer()");
		buf.append(".append(\"(\")");
		for (int i = 0; i < f.length; i++) {
			Field field = f[i];
			buf.append(".append(\"").append(field.getName()).append("=\")").append(".append(" + field.getName()).append(").append(\", \")");

		}
		buf.append(".append(\")\")");
		buf.append(".toString();\r\n}");

		System.out.println(buf);
	}
}
