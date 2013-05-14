package designPatterns.j2ee;

/**
 * Thrown by <code>ServiceLocator</code> on failure. It wraps a lower-level exception.
 * 
 * @see ServiceLocator
 * 
 * @pattern ServiceLocator (role=exceptionClass)
 * 
 * @generatedBy CodePro at 12/4/07 7:13 PM
 * 
 * @author Sandeep.Maloth
 * 
 * @version $Revision$
 */
public class ServiceLocatorException extends Exception {

	/**
	 * The wrapped exception, or null if none.
	 */
	private Exception exception;

	/**
	 * Constructs an instance wrapping another exception, and with a detail message.
	 * 
	 * @param message the detail message
	 * @param exception the wrapped exception
	 */
	public ServiceLocatorException(String message, Exception exception) {
		super(message);
		this.exception = exception;
	}

	/**
	 * Constructs an instance with a detail message.
	 * 
	 * @param message the detail message
	 */
	public ServiceLocatorException(String message) {
		this(message, null);
	}

	/**
	 * Constructs an instance wrapping another exception.
	 * 
	 * @param exception the wrapped exception
	 */
	public ServiceLocatorException(Exception exception) {
		this(null, exception);
	}

	/**
	 * Gets the wrapped exception.
	 * 
	 * @return the wrapped exception
	 */
	public Exception getException()
	{
		return exception;
	}

	/**
	 * Retrieves (recursively) the root cause exception.
	 * 
	 * @return the root cause exception
	 */
	public Exception getRootCause()
	{
		if (exception instanceof ServiceLocatorException) {
			return ((ServiceLocatorException) exception).getRootCause();
		}
		return exception == null ? this : exception;
	}

	
	public String toString()
	{
		if (exception instanceof ServiceLocatorException) {
			return ((ServiceLocatorException) exception).toString();
		}
		return exception == null ? super.toString() : exception.toString();
	}
}
