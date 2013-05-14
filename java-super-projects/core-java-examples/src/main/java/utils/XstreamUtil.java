package utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;

public class XstreamUtil {
	
	@SuppressWarnings("unchecked")
	public static String toXML(Map<String,Class> aliases, Object object){
		XStream xstream = new XStream();
		for (Map.Entry<String, Class> entry : aliases.entrySet()) {
			String key = entry.getKey();
			Class value = entry.getValue();
			xstream.alias(key, value);
		}
		populateCommonAliases(xstream);
		
		return xstream.toXML(object);
	}

	@SuppressWarnings("unchecked")
	public static Object fromXML(Map<String, Class> aliases, String xml){
		XStream xstream = new XStream();
		for (Map.Entry<String, Class> entry : aliases.entrySet()) {
			String key = entry.getKey();
			Class value = entry.getValue();
			xstream.alias(key, value);
		}
		populateCommonAliases(xstream);
		return xstream.fromXML(xml);
	}

	private static void populateCommonAliases(XStream xstream) {
		xstream.alias("nodes", List.class);
		xstream.alias("nodes", Set.class);
		xstream.alias("nodes", Map.class);
	}
}
