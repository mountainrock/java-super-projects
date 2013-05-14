package utils;

import java.io.File;

/**
 * This utility has methods not available in apache-commons-lang.
 */
public class StringUtil {

	/**
	 * Creates lesser objects than string "+" operator
	 */
	public static String join(String... strings)
	{
		StringBuilder sb = new StringBuilder();
		for (String string : strings) {
			if (string != null)
				sb.append(string);
		}
		return sb.toString();
	}

	/**
	 * Creates lesser objects than string "+" operator
	 * <p>
	 * Generates string in the format msg[key1=val1,key2=val2...]
	 * </p>
	 * 
	 * @param msg -The message
	 * @param keyOrVal - varargs of alternating key,values
	 */
	public static String joinParams(String msg, String... keyOrVal)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(msg).append("[");
		boolean toggle = true;
		for (String string : keyOrVal) {
			if (string != null)
				sb.append(string);
			sb.append(toggle ? "=" : ", ");
			toggle = !toggle;
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * A path can have \ or /. This can be replaced by the seperator.
	 * @return
	 */
	public static String sanitizeFilePath(String path, String seperator)
	{
		path = path.replaceAll("\\\\", seperator);
		path = path.replaceAll("/", seperator);
		return path;

	}
}
