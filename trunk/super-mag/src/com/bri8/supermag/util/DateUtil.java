package com.bri8.supermag.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
	public static final String DATE_DD_MM_YYYY = "dd/MM/yyyy";
	public static final String DATE_YYYY_M_MDD = "yyyyMMdd";

	public static final Map<String, DateFormat> dateFormatCache = new HashMap<String, DateFormat>();

	public static String transformDateFormat(String date, String patternFrom, String patternTo) {
		if (StringUtils.isBlank(date))
			return "";
		Date dateObj = null;

		DateFormat formatFrom = getDateFormat(patternFrom);
		DateFormat formatTo = getDateFormat(patternTo);

		try {
			dateObj = formatFrom.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return formatTo.format(dateObj);
	}

	public static DateFormat getDateFormat(String pattern) {
		if (!dateFormatCache.containsKey(pattern)) {
			dateFormatCache.put(pattern, new SimpleDateFormat(pattern));

		}
		return dateFormatCache.get(pattern);
	}
}
