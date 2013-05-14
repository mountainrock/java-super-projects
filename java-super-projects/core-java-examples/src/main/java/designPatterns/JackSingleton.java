package designPatterns;

public class JackSingleton {
	
	public static void main(String[] args) throws Exception, IllegalAccessException
	{
		// try to jack singleton using reflection
		// System.out.println(Singleton.class.newInstance()); // gives IllegalAccessException

		// using constructor.Note a declared constructor gives u a private constructor as well.
		Singleton.class.getDeclaredConstructor(new Class[] {}).setAccessible(true);
		System.out.println(Singleton.class.getDeclaredConstructor(new Class[] {}).isAccessible());

	}
}
