/**
 * 
 */
package IO;

import org.apache.commons.lang.StringUtils;


public class ParsingUtility {
	public static void main(String[] args)
	{
		String testSTring = "this si sa test string\r\nsdfasdfs";
		String[] splitStrings = StringUtils.split(testSTring, "\r\n");
		for (int i = 0; i < splitStrings.length; i++) {
			String string = splitStrings[i];
			System.out.println(string);

		}

	}
}
