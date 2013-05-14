/**
 * 
 */
package utilsdemo;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class StringUtil {

	
	public static void main(String[] args)
	{
		String inputString = "this is a test";
		System.out.println(StringUtils.capitalise(inputString));
		Set setOfThings = new HashSet();
		setOfThings.add("apple");
		setOfThings.add("table");
		setOfThings.add("orange");
		System.out.println(StringUtils.join(setOfThings.iterator(), " "));

	}

	/**
	 * getFormattedString takes an object and prints all the attributes that have getters and or isXXX methods. Helper method that allows easy dumping of object information as a string
	 * 
	 * @param objectToBeConverted Object to be converted to a formatted string
	 * 
	 * @return String representation of the given object
	 */
	public static String getFormattedString(Object objectToBeConverted)
	{
		String result = "";

		if (objectToBeConverted != null) {
			Class clazz = objectToBeConverted.getClass();
			StringBuffer stringBuffer = new StringBuffer();

			try {
				stringBuffer.append(clazz.getName());
				stringBuffer.append("[Field Values : ");

				String methodName = null;
				Method[] getters = clazz.getDeclaredMethods();

				boolean isFirstTime = true;

				for (int index = 0; index < getters.length; index++) {
					methodName = getters[index].getName();

					if (methodName.startsWith("get") == true || methodName.startsWith("is") == true) {
						if (isFirstTime == false) {
							stringBuffer.append(", ");
						}
						isFirstTime = false;
						stringBuffer.append(methodName.substring(3));
						stringBuffer.append("=" + getters[index].invoke(objectToBeConverted, null));
					}
				}
				stringBuffer.append("]");
			} catch (Exception ex) {
				stringBuffer.append("Error creating formatted string for : " + clazz);
			}
			result = stringBuffer.toString();
		}
		return result;
	}

}
