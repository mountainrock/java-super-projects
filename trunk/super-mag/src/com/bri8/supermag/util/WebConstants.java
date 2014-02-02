package com.bri8.supermag.util;


public interface WebConstants {
	int DEFAULT_PAGE_SIZE = 20;
	Integer DEFAULT_START_PAGE = 1;
	String STATUS_STOPPED = "status:\"Suspended\" status:\"Terminated\" status:\"Withdrawn\" status:\"Stopped\"";
	String STATUS_ONGOING = "status:\"Not yet recruiting\" status:\"Recruiting\" status:\"Enrolling by invitation\" status:\"Active, not recruiting\" status:\"Ongoing\"";
	String STATUS_COMPLETED = "status:\"Completed\"";
	String STATUS_ALL_ADV[] = { "\"Suspended\"", " \"Terminated\"", " \"Withdrawn\"" ,"\"Stopped\"", " \"Not yet recruiting\"",
			" \"Recruiting\"" , "\"Enrolling by invitation\"" , "\"Active, not recruiting\"", " \"Ongoing\" ", "\"Completed\"" };

	String DEFAULT_QUERY = STATUS_ONGOING + Constants.SPACE + STATUS_STOPPED + Constants.SPACE + Constants.SPACE + STATUS_COMPLETED;
	String DEFAULT_QUERY_ADV = DEFAULT_QUERY;

	/** View constants */
	String VIEW_KEY_XSLT_PATH = "trial.xsl.path";
	String VIEW_KEY_XML = "trial.xml";

	/** Request params */
	String REQUEST_PARAM_SHOW_XML = "xml";
	String REQUEST_PARAM_CLEAR_XSL_CACHE = "clear-xsl-cache";
	String REQUEST_PARAM_SEARCH_TYPE = "type";

	/** Search params */
	String DESCENDING = "DESC";
	String DEFAULT_SORT_FIELD = "date_isrctn_assigned";
	String SEARCH_TYPE_ADVANCED = "adv";
	String SORT_SUFFIX = "_sort";

	String BEAN_PREFIX_FOR_FIELD_MAPPING = "fieldMapping_";
	String BEAN_OTHER_REGISTERS = "otherRegisters";
	String BEAN_PREFIX_FOR_STATUS_VALUES = "statusValues";
	String OTHER_REGISTER = "9999";
}
