package miscellaneous;

public abstract class StaticInAbstractClass {
	int x = 1;

	StaticInAbstractClass() {

	}

	static {
		System.out.println("11111");
	}

	static void m()
	{
		System.out.println("parent m()");
	}

	
	public static void main(String[] args)
	{
		// System.out.println("parent");
		StaticInAbstractClass z = new Z();
		z.m();
		System.out.println(z.x);

	}

}

class Z extends StaticInAbstractClass {
	static {
		System.out.println("2222");
	}
	int x = 2;

	static void m()
	{
		System.out.println("child m()");
	}

	public static void main(String[] args)
	{

	}
}
