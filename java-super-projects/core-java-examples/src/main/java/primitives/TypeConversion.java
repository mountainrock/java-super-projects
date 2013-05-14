package primitives;

public class TypeConversion {

	/**
	 * REMEMBER: The mathematical operations of primitives(below int, i.e., byte, short, char) returns a minimum of int.. Upcasting a primitive is done implicitly.Should do explicit down casting..
	 * @param args
	 */
	public static void main(String[] args)
	{

		byte a = 1;
		byte b = 2;

		short as = 1;
		short bs = 2;

		// short c =as+bs; not allowed ...as short add returns int.

		float af = 1;
		float bf = 2;

		// byte c = a+b; not allowed.as byte add returns int.

		float d = af + bf;
		double e = af + bf; // a float can be casted to double...up casting...

		d = (float) e;// should do explicit down casting..

		// long e = af+bf; not allowed

		char ac = 1;
		char ab = 1;

		// char dc = ac+ab; not allowed as the char add returns a int.

		// check the range of a primitive

		int i = (int) Math.pow(2, 32);

		System.out.println(i); // 2 pow 32
		System.out.println(i + 1); // 2 pow 32 + 1 =

	}

}
