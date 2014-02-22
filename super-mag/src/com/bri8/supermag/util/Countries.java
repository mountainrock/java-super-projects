package com.bri8.supermag.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The countries are populated from  countries data set.
 * 
 */
public class Countries {
	private static Map<String, String> _countries = new LinkedHashMap<String, String>();

	public static Map<String, String> getCountries() {
		return _countries;
	}

	public static void setCountries(Map<String, String> countries) {
		_countries = countries;
	}

}
