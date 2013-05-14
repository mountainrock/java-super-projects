package miscellaneous;

public class Polymorphism {

	
	public static void main(String[] args)
	{
		A a = new B();
		a.method();

	}

}

class A {
	int m = 5;

	void method()
	{
		System.out.println(">>" + m);
	}

}

class B extends A {
	int m = 99;

	void method()
	{
		System.out.println(">>" + m);
	}
}