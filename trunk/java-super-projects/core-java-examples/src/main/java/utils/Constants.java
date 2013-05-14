package utils;

public interface Constants {

	String AMPERSAND = "&";
	String BACK_SLASH = "\\";
	String BLANK = "";
	String CLOSE_BRACKET = "]";
	String CLOSE_CURLY_BRACE = "}";
	String CLOSE_PARENTHESIS = ")";
	String COLON = ":";
	String COMMA = ",";
	String COMMA_AND_SPACE = ", ";
	String DASH = "-";
	String DOUBLE_APOSTROPHE = "''";
	String DOUBLE_CLOSE_BRACKET = "]]";
	String DOUBLE_OPEN_BRACKET = "[[";
	String DOUBLE_SLASH = "//";
	String EQUAL = "=";
	String GREATER_THAN = ">";
	String GREATER_THAN_OR_EQUAL = ">=";
	String FALSE = "false";
	String FORWARD_SLASH = "/";
	String FOUR_SPACES = "    ";
	String IS_NOT_NULL = "IS NOT NULL";
	String IS_NULL = "IS NULL";
	String LESS_THAN = "<";
	String LESS_THAN_OR_EQUAL = "<=";
	String LIKE = "LIKE";
	String MINUS = "-";
	String NBSP = "&nbsp;";
	String NEW_LINE = System.getProperty("line.separator");;
	String NOT_EQUAL = "!=";
	String NOT_LIKE = "NOT LIKE";
	String NULL = "null";
	String OPEN_BRACKET = "[";
	String OPEN_CURLY_BRACE = "{";
	String OPEN_PARENTHESIS = "(";
	String PERCENT = "%";
	String PERIOD = ".";
	String PIPE = "|";
	String PLUS = "+";
	String POUND = "#";
	String QUESTION = "?";
	String QUOTE = "\"";
	String RETURN = "\r";
	String RETURN_NEW_LINE = "\r\n";
	String SEMICOLON = ";";
	String SLASH = FORWARD_SLASH;
	String SPACE = " ";
	String STAR = "*";
	String TAB = "\t";
	String TILDE = "~";
	String TRUE = "true";
	String UNDERLINE = "_";
	String UTF8 = "UTF-8";
	
	//time
	int SECOND = 1000;

	//lucene
	String LUCENE_DIR = "lucene.dir";
	String LUCENE_STORE_TYPE = "lucene.store.type";
	String LUCENE_ANALYZER = "lucene.analyzer";
	char[] LUCENE_SANITIZE_CHARS = { ':' };
	boolean SORTABLE = true;
	int SORTABLE_FIELD_MAX_SIZE = 20;
	
	//http
	String MODIFIED = "modified";
	String TEXT_XML = "text/xml";
	String HTTP_WITH_SLASH = "http://";
	String HTTPS_WITH_SLASH = "https://";
	
	//spring
	String SPRING_CONFIG_LOCATIONS[] = { "applicationContext.xml" };
	
	//system properties
	String SYSTEM_PROPERTY_TEMP_DIR="java.io.tmpdir";
	String TEMP_DIR=SYSTEM_PROPERTY_TEMP_DIR;
}