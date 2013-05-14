package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.DateTools;

public class GetterUtil {

	public static DateFormat DATE_YYYYMMDD_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static DateFormat DATE_DDMMYYYY_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	public static DateFormat DATE_MONTH_DD_YYYY_FORMAT = new SimpleDateFormat("MMMMM dd, yyyy");

	public static final boolean DEFAULT_BOOLEAN = false;

	public static final boolean[] DEFAULT_BOOLEAN_VALUES = new boolean[0];

	public static final double DEFAULT_DOUBLE = 0.0;

	public static final double[] DEFAULT_DOUBLE_VALUES = new double[0];

	public static final float DEFAULT_FLOAT = 0;

	public static final float[] DEFAULT_FLOAT_VALUES = new float[0];

	public static final int DEFAULT_INTEGER = 0;

	public static final int[] DEFAULT_INTEGER_VALUES = new int[0];

	public static final long DEFAULT_LONG = 0;

	public static final long[] DEFAULT_LONG_VALUES = new long[0];

	public static final short DEFAULT_SHORT = 0;

	public static final short[] DEFAULT_SHORT_VALUES = new short[0];

	public static final String DEFAULT_STRING = Constants.BLANK;

	public static String[] BOOLEANS = { "true", "t", "y", "on", "1" };

	public static final String[] SPECIAL = new String[] { "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~", "*", "?", ":", "\\" };

	public static String escape(String text)
	{
		for (int i = SPECIAL.length - 1; i >= 0; i--) {
			text = StringUtils.replace(text, SPECIAL[i], Constants.BACK_SLASH + SPECIAL[i]);
		}

		return text;
	}

	public static boolean getBoolean(String value)
	{
		return getBoolean(value, DEFAULT_BOOLEAN);
	}

	public static boolean getBoolean(String value, boolean defaultValue)
	{
		return get(value, defaultValue);
	}

	public static boolean[] getBooleanValues(String[] values)
	{
		return getBooleanValues(values, DEFAULT_BOOLEAN_VALUES);
	}

	public static boolean[] getBooleanValues(String[] values, boolean[] defaultValue)
	{

		if (values == null) {
			return defaultValue;
		}

		boolean[] booleanValues = new boolean[values.length];

		for (int i = 0; i < values.length; i++) {
			booleanValues[i] = getBoolean(values[i]);
		}

		return booleanValues;
	}

	public static Date getDate(String value, DateFormat df)
	{
		return getDate(value, df, new Date());
	}

	public static Date getDate(String value, DateFormat df, Date defaultValue)
	{
		return get(value, df, defaultValue);
	}

	public static double getDouble(String value)
	{
		return getDouble(value, DEFAULT_DOUBLE);
	}

	public static double getDouble(String value, double defaultValue)
	{
		return get(value, defaultValue);
	}

	public static double[] getDoubleValues(String[] values)
	{
		return getDoubleValues(values, DEFAULT_DOUBLE_VALUES);
	}

	public static double[] getDoubleValues(String[] values, double[] defaultValue)
	{

		if (values == null) {
			return defaultValue;
		}

		double[] doubleValues = new double[values.length];

		for (int i = 0; i < values.length; i++) {
			doubleValues[i] = getDouble(values[i]);
		}

		return doubleValues;
	}

	public static float getFloat(String value)
	{
		return getFloat(value, DEFAULT_FLOAT);
	}

	public static float getFloat(String value, float defaultValue)
	{
		return get(value, defaultValue);
	}

	public static float[] getFloatValues(String[] values)
	{
		return getFloatValues(values, DEFAULT_FLOAT_VALUES);
	}

	public static float[] getFloatValues(String[] values, float[] defaultValue)
	{

		if (values == null) {
			return defaultValue;
		}

		float[] floatValues = new float[values.length];

		for (int i = 0; i < values.length; i++) {
			floatValues[i] = getFloat(values[i]);
		}

		return floatValues;
	}

	public static int getInteger(String value)
	{
		return getInteger(value, DEFAULT_INTEGER);
	}

	public static int getInteger(String value, int defaultValue)
	{
		return get(value, defaultValue);
	}

	public static int[] getIntegerValues(String[] values)
	{
		return getIntegerValues(values, DEFAULT_INTEGER_VALUES);
	}

	public static int[] getIntegerValues(String[] values, int[] defaultValue)
	{
		if (values == null) {
			return defaultValue;
		}

		int[] intValues = new int[values.length];

		for (int i = 0; i < values.length; i++) {
			intValues[i] = getInteger(values[i]);
		}

		return intValues;
	}

	public static long getLong(String value)
	{
		return getLong(value, DEFAULT_LONG);
	}

	public static long getLong(String value, long defaultValue)
	{
		return get(value, defaultValue);
	}

	public static long[] getLongValues(String[] values)
	{
		return getLongValues(values, DEFAULT_LONG_VALUES);
	}

	public static long[] getLongValues(String[] values, long[] defaultValue)
	{
		if (values == null) {
			return defaultValue;
		}

		long[] longValues = new long[values.length];

		for (int i = 0; i < values.length; i++) {
			longValues[i] = getLong(values[i]);
		}

		return longValues;
	}

	public static short getShort(String value)
	{
		return getShort(value, DEFAULT_SHORT);
	}

	public static short getShort(String value, short defaultValue)
	{
		return get(value, defaultValue);
	}

	public static short[] getShortValues(String[] values)
	{
		return getShortValues(values, DEFAULT_SHORT_VALUES);
	}

	public static short[] getShortValues(String[] values, short[] defaultValue)
	{

		if (values == null) {
			return defaultValue;
		}

		short[] shortValues = new short[values.length];

		for (int i = 0; i < values.length; i++) {
			shortValues[i] = getShort(values[i]);
		}

		return shortValues;
	}

	public static String getString(String value)
	{
		return getString(value, DEFAULT_STRING);
	}

	public static String getString(String value, String defaultValue)
	{
		return get(value, defaultValue);
	}

	public static boolean get(String value, boolean defaultValue)
	{
		if (value != null) {
			try {
				value = value.trim();

				if (value.equalsIgnoreCase(BOOLEANS[0]) || value.equalsIgnoreCase(BOOLEANS[1]) || value.equalsIgnoreCase(BOOLEANS[2]) || value.equalsIgnoreCase(BOOLEANS[3])
						|| value.equalsIgnoreCase(BOOLEANS[4])) {

					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
			}
		}

		return defaultValue;
	}

	public static Date get(String value, DateFormat df, Date defaultValue)
	{
		try {
			Date date = df.parse(value.trim());

			if (date != null) {
				return date;
			}
		} catch (Exception e) {
		}

		return defaultValue;
	}

	public static double get(String value, double defaultValue)
	{
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
		}

		return defaultValue;
	}

	public static float get(String value, float defaultValue)
	{
		try {
			return Float.parseFloat(value);
		} catch (Exception e) {
		}

		return defaultValue;
	}

	public static int get(String value, int defaultValue)
	{
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
		}

		return defaultValue;
	}

	public static long get(String value, long defaultValue)
	{
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
		}

		return defaultValue;
	}

	public static short get(String value, short defaultValue)
	{
		try {
			return Short.parseShort(value);
		} catch (Exception e) {
		}

		return defaultValue;
	}

	public static String get(String value, String defaultValue)
	{
		if (value != null) {
			value = value.trim();
			return value;
		}

		return defaultValue;
	}

	public static String getDefaultFormattedDate(String date)
	{
		if (StringUtils.isBlank(date))
			return "";
		Date dateObj = null;

		try {
			dateObj = DATE_DDMMYYYY_FORMAT.parse(date);
		} catch (ParseException e) {
			try {
				dateObj = DATE_MONTH_DD_YYYY_FORMAT.parse(date);
			} catch (ParseException e1) {
				throw new RuntimeException(e);
			}
		}
		return DATE_YYYYMMDD_FORMAT.format(dateObj);
	}

	public static String getBlankIfNull(Object obj)
	{
		return obj == null ? "" : obj.toString();
	}

	public static String replaceSpace(String str, String replacement)
	{
		return str == null ? "" : str.trim().replaceAll(" ", replacement);
	}

}