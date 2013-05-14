/**
 * 
 */
package designPatterns;

/**
 * 
 * Note:A singleton cannot be extended, hence behaves like a final object ??
 * @author Sandeep.Maloth
 * 
 */
public class Singleton {
	static Singleton thisObj = new Singleton();

	private Singleton() {
		System.out.println("I am private");
	}

	static Singleton getIntance()
	{
		return thisObj;
	}

	public String toString()
	{

		return "i am singleton...very lonely :-( " + this.hashCode();
	}

}

/*
 * class a extends Singleton { a() { System.out.println("Cant extend a singleton??"); }
 * 
 * }
 */