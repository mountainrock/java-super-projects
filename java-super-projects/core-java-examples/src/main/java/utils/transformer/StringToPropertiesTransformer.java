package utils.transformer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a String containing name/value pairs (standard Properties format, UTF-8 encodings assumed) into a Properties object.
 */
public class StringToPropertiesTransformer  implements Transformer{
	private static final StringToPropertiesTransformer _instance = new StringToPropertiesTransformer();

	public static StringToPropertiesTransformer getInstance()
	{
		return _instance;
	}

	private boolean throwDecodingExceptions;

	/**
	 * Convert a 'properties format' string into it's corresponding Properties object.
	 * @param subject a String (or an Object whose toString() method will be called) that contains name/value pairs in text format that is suitable for loading directly into a Properties object.
	 * @return a non-null, but possibly empty Properties object.
	 */
	public Object transform(Object subject)
	{
		return toProperties(subject == null ? null : subject.toString());
	}

	/**
	 * Convert a 'properties format' string into it's corresponding Properties object.
	 * @param subject a String that contains name/value pairs in text format that is suitable for loading directly into a Properties object.
	 * @return a non-null, but possibly empty Properties object.
	 */
	public Properties toProperties(String subject)
	{
		Properties result = new Properties();
		loadPropertiesFromString(result, subject);
		return result;
	}

	/**
	 * Update the given Properties object with values derived from name/value pairs stored in given message string.
	 * @param msg a, possibly, null string, containing name/value pairs in standard java Properties format.
	 * @param properties a non-null Properties object to be updated
	 * @throws RuntimeException if there is an error converting the string and if throwDecodingExceptions is true.
	 * @throws IllegalArgumentException if the given Properties object is null.
	 * 
	 */
	public void loadPropertiesFromString(Properties properties, String msg)
	{
		if (msg == null) {
			return;
		}
		if (properties == null) {
			throw new IllegalArgumentException("You must provide a non-null Properties object");
		}

		try {
			ByteArrayInputStream is = null;
			is = new ByteArrayInputStream(msg.getBytes("UTF-8"));
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isThrowDecodingExceptions()
	{
		return throwDecodingExceptions;
	}

	/**
	 * Determine whether the exceptions should be thrown in the event of a decoding exception.
	 * @param throwDecodingExceptions true, if you wish exceptions to be thrown and further processing abandoned, should any conversion error occur.
	 */
	public void setThrowDecodingExceptions(boolean throwDecodingExceptions)
	{
		this.throwDecodingExceptions = throwDecodingExceptions;
	}

}
