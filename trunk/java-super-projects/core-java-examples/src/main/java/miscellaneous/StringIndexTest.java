package miscellaneous;

/**
 * 
 */


public class StringIndexTest {

	
	public static void main(String[] args)
	{
		String sampleString = "This is${test.param}..8";
		System.out.println(sampleString.indexOf("{"));
		System.out.println(replace(sampleString, "${test.param}", "VAL"));

	}

	static String replace(String str, String pattern, String replace)
	{
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}
}
