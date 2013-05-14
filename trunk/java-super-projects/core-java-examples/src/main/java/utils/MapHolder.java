package utils;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Holds a map. Wrapper which is useful during spring configuration.
 * 
 */
public class MapHolder {

	private Map map;

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

	public String get(String field)
	{
		return (String) map.get(field);
	}
}
