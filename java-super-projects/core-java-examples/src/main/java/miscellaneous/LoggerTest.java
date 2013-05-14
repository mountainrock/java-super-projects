package miscellaneous;

import java.util.logging.Logger;

/**
 * 
 */

/**
 * There are several reasons why this is a not a good idea. First off, the logger is not available from a static context. Secondly, every time a new instance is created, we make a new logger
 *@reference [JavaSpecialists] Issue 137 - Creating Loggers DRY-ly
 * @file name : LoggerTest.java
 * @author : Sandeep.Maloth
 * @Creation Date :Dec 29, 2006
 */
public class LoggerTest {

	private static Logger logger = null;
	{
		logger = Logger.getLogger(this.getClass().getName());
	}

	public static void main(String[] args)
	{
		try {
			logger.info("static Hello");
		} catch (NullPointerException e) {
			System.out.println("failed to call Logger from static context");
		}
		LoggerTest m = new LoggerTest();
		m.run();
	}

	public void run()
	{
		logger.info("Hello");
	}

}
