package miscellaneous;

import junit.framework.TestCase;

/**
 * JUnit test case for HandlerFactoryTest
 */

public class HandlerFactoryTest extends TestCase {
	// declare reusable objects to be used across multiple tests
	public HandlerFactoryTest(String name) {
		super(name);
	}

	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(HandlerFactoryTest.class);
	}

	protected void setUp()
	{
		// define reusable objects to be used across multiple tests
	}

	protected void tearDown()
	{
		// clean up after testing (if necessary)
	}

	public void testGetHandler()
	{
		fail("Newly generated method - fix or disable");

		// insert code testing basic functionality
		HandlerFactory var0 = null;
		String[] var1 = null;
		String var2 = null;
		String var3 = null;
		var3 = var0.getHandler(var1, var2);

	}

	public void testMain()
	{
		fail("Newly generated method - fix or disable");

		// insert code testing basic functionality
		String[] var1 = null;
		HandlerFactory.main(var1);

	}
}