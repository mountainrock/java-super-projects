package utils.transformer;

import org.apache.commons.collections.Transformer;

import java.util.Properties;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;

/**
 * Transforms a java.util.Map into a java.util.Properties object.
 * 
 * <p>
 * Null keys and values are not copied to the resulting Properties object.
 * <p>
 * Also, keys and values are converted to their 'string-ified' form (via their own toString() methods).
 */
public class MapToPropertiesTransformer implements Transformer {
	private static final MapToPropertiesTransformer _instance = new MapToPropertiesTransformer();

	public static MapToPropertiesTransformer getInstance()
	{
		return _instance;
	}

	/**
	 * Convert the given Map or Properties object into a Properties object.
	 * @param source the Map or Properties object to be transformed.
	 * @return a non-null Properties object.
	 * @throws ClassCastException if the given source object is not a Map or a Properties object.
	 */
	public Object transform(Object source)
	{
		if (source instanceof Properties) {
			return (Properties) source;
		}
		return toProperties((Map) source);
	}

	/**
	 * Convert the given Map into a Properties object.
	 * @param source a Map object.
	 * @return a non-null, but possibly empty Properties object.
	 */
	public Properties toProperties(Map source)
	{
		Properties p = new Properties();
		copyNonNullProperties(p, source);
		return p;
	}

	private void copyNonNullProperties(Properties dest, Map source)
	{
		Collection entries = Collections.EMPTY_LIST;
		if (source != null) {
			entries = source.entrySet();
		}
		for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			if (entry.getValue() != null) {
				dest.setProperty(entry.getKey().toString(), entry.getValue().toString());
			}
		}
	}

}
