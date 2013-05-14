package reflection;

/**
 * A simple utility exception to simplify catching and dealing with reflection exceptions thrown by Reflect.
 * 
 * @author Maurice Nicholson
 */
public class ReflectException extends Exception {

	/**
	 * Cnstructor
	 */
	public ReflectException(String message) {
		super(message);
	}

	/**
	 * Constructor
	 */
	public ReflectException(String message, Throwable throwable) {
		super(message + ": " + throwable);
	}
}
