package miscellaneous;

/**
 * 
 */


public class StringTest {
	int i = 0;

	
	public static void main(String[] args)
	{
		int length = 80;
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buf.append(new Character((char) (i + 65)));
		}
		System.out.println(buf.toString());

	}

}
