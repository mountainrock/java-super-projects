package utils.transformer;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.Transformer;
import org.apache.log4j.helpers.LogLog;

/**
 * Transforms a 'bean' into a Properties object that contains properties which correspond to the given bean's 'properties'. All property values are converted into string form by the properties
 * toString() methods.
 * 
 * <p>
 * The given bean class is introspected at run-time.
 */
public class BeanToPropertiesTransformer  implements Transformer{
	private static final BeanToPropertiesTransformer instance = new BeanToPropertiesTransformer();

	public static BeanToPropertiesTransformer getInstance()
	{
		return instance;
	}

	public Object transform(Object bean)
	{
		return toProperties(bean);
	}

	public Properties toProperties(Object bean)
	{
		if (bean instanceof Properties) {
			return (Properties) bean;
		}
		if (bean instanceof Map) {
			Properties p = MapToPropertiesTransformer.getInstance().toProperties((Map) bean);
			return p;
		}

		Properties map = new Properties();
		if (bean != null)
			try {
				BeanInfo bi = Introspector.getBeanInfo(bean.getClass(), Object.class);
				PropertyDescriptor properties[] = bi.getPropertyDescriptors();

				for (int i = 0; i < properties.length; i++) {
					PropertyDescriptor pd = properties[i];
					String propertyValue = getPropertyValue(bean, pd);
					if (propertyValue != null) {
						map.put(pd.getName(), propertyValue);
					}
				}
			} catch (IntrospectionException e) {
			}
		return map;
	}

	private String getPropertyValue(Object bean, PropertyDescriptor pd)
	{
		Object result = null;
		try {
			result = pd.getReadMethod().invoke(bean, null);
		} catch (Exception e) {
			LogLog.error("Error reading " + pd.getName(), e);
		}
		return result == null ? null : result.toString();
	}

}
