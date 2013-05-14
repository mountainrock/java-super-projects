/**
 * 
 */
package exception;


public class FinallyDemo {

	
	public static void main(String[] args) throws Exception
	{
		try {
			System.out.println("Demonstrate that finally is never called on system.exit(0)");
			System.exit(1);
			throw new Exception("ex");
		} finally {
			System.out.println("i am in finally");
		}

	}

}
