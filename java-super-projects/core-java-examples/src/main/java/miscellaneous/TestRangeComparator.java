package miscellaneous;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Thic class compares two string for a range check. </br> Supports string comparisons of type AA001, A-01, 01, A.01. </br> <b>Note:</b> The string comparison is made between the alphabetic prefix if
 * one is available.If the prefix matches then the numeric part of the string is substracted and used as the comparison value(if available).If a alphabetic prefix is not found, the the difference of
 * the numeris part is sent as comparison value.
 * 
 * @file name : TestRangeComparator.java
 * @author : sandeep.maloth
 * @Creation Date :Nov 21, 2006
 */
public class TestRangeComparator implements Comparator {

	String STRING_PATTERN = "([a-zA-Z]+)";

	String NUMERIC_PATTERN = "([0-9]+)";

	/**
	 * This method is used to check two string objects.
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object arg0, Object arg1)
	{
		if (arg0 == null && arg1 == null)
			return 0;
		if (arg0 == null && arg1 != null)
			return -1;
		if (arg0 != null && arg1 == null)
			return 1;

		String s1 = (String) arg0;
		s1 = s1.toUpperCase();

		String s2 = (String) arg1;
		s2 = s2.toUpperCase();

		String s1Found = find(s1, STRING_PATTERN);
		// System.out.println(s1Found);
		String s2Found = find(s2, STRING_PATTERN);
		// System.out.println(s2Found);
		if ((s1Found != null && s2Found != null) && !s1Found.equals(s2Found)) {// if alphabetic part is not same. Return negative value to say that they are not matching.
			return -1;
		}
		String s1NumFound = find(s1, NUMERIC_PATTERN);
		// System.out.println(s1NumFound);
		String s2NumFound = find(s2, NUMERIC_PATTERN);
		// System.out.println(s2NumFound);

		int compareValue = -1;

		if (s1NumFound != null && s1NumFound != null) {
			int s1Int = Integer.parseInt(s1NumFound);
			int s2Int = Integer.parseInt(s2NumFound);

			compareValue = (s2Int - s1Int);
		}
		return compareValue;
	}

	/**
	 * Returns the found string for a pattern. Returns null if not found.
	 * 
	 * @param source
	 * @param findPattern
	 */
	public static String find(String source, Object findPattern)
	{
		StringBuffer buf = new StringBuffer(source);
		Pattern p = Pattern.compile(findPattern.toString());
		Matcher m = p.matcher(buf);
		int i = 1;
		while (m.find()) {
			return m.group(1);

		}
		return null;
	}

	
	public static void main(String[] args)
	{
		TestRangeComparator testRangeComparator = new TestRangeComparator();
		String s1 = "P01";
		String s2 = "P4";
		System.out.println("Custom comparison gives :" + testRangeComparator.compare(s1, s2));
		System.out.println("String comparison would give :" + s1.compareToIgnoreCase(s2));

	}

}
