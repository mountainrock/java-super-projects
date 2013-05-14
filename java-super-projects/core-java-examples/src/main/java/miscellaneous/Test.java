package miscellaneous;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */


public class Test {
	int t;

	String r;

	Object o;

	static String[] alphabets = new String[] { "a", "b", "c", "d", "E", "F", "g", "h", "i", "g", "h", "i", "J", "h", "k", "l", "m", "N", "o", "p", "Q", "r", "s", "T", "u", "v", "w", "x", "Y", "Z" };

	// first Random value
	static List<String> l = new ArrayList<String>();
	static List<String> duplicates = new ArrayList<String>();

	static void method()
	{
		int firstRandom = (int) (Math.floor(Math.random() * 50 + 55));
		// second Random Value
		int secondRandom = (int) (Math.floor(Math.random() * 50 + firstRandom));
		// userId
		String newUserId = "";

		// Iteration
		for (int i = 0; i < 3; i++) {
			int subIndex = (int) (Math.floor(Math.random() * 15 + i));
			newUserId += alphabets[subIndex];
		}// we need to form a word with alphabets using random numbers
		// with word, append the random number
		newUserId += secondRandom;
		if (!l.contains(newUserId)) {
			l.add(newUserId);
			System.out.println(newUserId);
		} else {
			duplicates.add(newUserId);
			System.out.println("Duplicate " + newUserId);
		}

	}

	
	public static void main(String[] args)
	{
		int i = 100;
		while (i-- > 0) {
			System.out.println("hello".hashCode());
			System.out.println(new String("hello").hashCode());
		}
		/*
		 * String str="010 \\ 4";
		 * 
		 * 
		 * 
		 * System.out.println(010 / 4); System.out.println(010 - 4); int i = 5; boolean flag = i < 5; System.out.println(flag); System.out.println("i=" + (flag ? 9 : 9.9));
		 * 
		 * method();
		 * 
		 * System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)); String s = "this is a test";
		 * 
		 * int firstRandom = (int) (Math.floor(Math.random() * 50 + 55));
		 * 
		 * int i = 0; while (i++ < 10000) { String newUserId= getPassword(8); if(!l.contains(newUserId)) { l.add(newUserId); System.out.println(newUserId); } else { duplicates.add(newUserId);
		 * System.out.println("Duplicate "+ newUserId); } //method(); }
		 */
	}

	public static String getPassword(int n)
	{
		char[] pw = new char[n];
		int c = 'A';
		int r1 = 0;
		for (int i = 0; i < n; i++) {
			r1 = (int) (Math.random() * 3);
			switch (r1) {
			case 0:
				c = '0' + (int) (Math.random() * 10);
				break;
			case 1:
				c = 'a' + (int) (Math.random() * 26);
				break;
			case 2:
				c = 'A' + (int) (Math.random() * 26);
				break;
			}
			pw[i] = (char) c;
		}
		return new String(pw);
	}
}
