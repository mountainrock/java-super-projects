package search.example.srch.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SearchContext {

	Map<String, Object> params = new HashMap<String, Object>();

	public Map<String, Object> getParams()
	{
		return params;
	}

	public void setParams(Map<String, Object> params)
	{
		this.params = params;
	}

	public void addParam(String key, String value)
	{
		params.put(key, value);
	}

	public Object getParam(String key)
	{
		return params.get(key);
	}

	public String getParamAsString(String key)
	{
		return (String) params.get(key);
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(params);
	}
}
