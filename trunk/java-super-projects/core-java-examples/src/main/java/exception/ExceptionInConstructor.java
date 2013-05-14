/**
 * 
 */
package exception;


public class ExceptionInConstructor {
	public ExceptionInConstructor() throws Exception {
		throw new RuntimeException("this is a test");
	}

	
	public static void main(String[] args)
	{
		// i think its not good have some logic in constructor.If the methods throw up then how do we deal it.
		// a default set of computed state can be added.??
		try {
			new ExceptionInConstructor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
