/**
 * 
 */
package utilsdemo;

/**
 * Usefult to convert a camel case string to _ seperated constant.
 * @author sandeep.maloth
 * 
 */
public class ConstantGenerator {

	public static void main(String[] args)
	{
		String input = "breakingNewsArticleIds,latestNewsArticleIds,popularNewsArticleIds";
		String[] tokens = input.split(",");
		for (int i = 0; i < tokens.length; i++) {
			String string = tokens[i];
			StringBuffer result = convertToConstant(string);
			System.out.println("public static final String " + result + " = \"" + string + "\";");
		}

	}

	private static StringBuffer convertToConstant(String str)
	{

		char chars[] = str.toCharArray();
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c < 97) // it is upper case
			{
				result.append("_");
			}
			result.append((c + "").toUpperCase());

		}
		return result;
	}

}
