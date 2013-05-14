/**
 * 
 */
package regex;


public class PatternEnum {
	String pattern;

	public PatternEnum(Object pat) {
		this.pattern = pat.toString();
	}

	public final static PatternEnum ALPHABET = new PatternEnum("[a-zA-Z]");
	public final static PatternEnum NUMBER = new PatternEnum("[0-9]");
	// spaces, tabs, and the beginning and end points of a line.
	public final static PatternEnum BOUNDARY = new PatternEnum("\\b");

	public final static PatternEnum ALPHA_NUMERIC = new PatternEnum(ALPHABET + "" + NUMBER);
	// 4 backslashes to match a single one slash '\'
	public final static PatternEnum SLASH_BACK = new PatternEnum("[\\\\]");
	// The regex \w matches a word character. In Java , this is written as "\\w".
	public final static PatternEnum WORD = new PatternEnum("[\\w]");
	// a single dollar sign in the replacement text becomes "\\$"
	public final static PatternEnum DOLLAR = new PatternEnum("[\\$]");
	public final static PatternEnum ZERO_OR_MORE = new PatternEnum("*");
	public final static PatternEnum HTML_TAG = new PatternEnum("<[\\\\]*[/]*[\\s]*[^>]+[/]*>");
	// email pattern is \b[A-Z0-9._%-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b
	public final static PatternEnum EMAIL = new PatternEnum("(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*");
	// control characters :{cntrl}
	public final static PatternEnum CONTROL = new PatternEnum("{cntrl}");
	// capitalize s/(([.!?]|\A)\s*)([a-z])/$1\ u$3/g -- FIXME:
	public final static PatternEnum CAPITALIZE = new PatternEnum("(([.!?]|\\A)\\s*)([a-z])/$1\\\\u$3");
	// swap
	public final static PatternEnum SWAP = new PatternEnum("(\\w+)(\\w+)$2$1");

	/*
	 * 
	 * TODO: make a enum of this [a-c] Matches a range of characters. This particular range is a through c or a, b or c
	 * 
	 * [\+x-] Matches the character +, X or -
	 * 
	 * [^ab] Matches any character except a or b
	 * 
	 * [a-z[bc]] Matches any character a to z except for b or c
	 * 
	 * . Matches any character except \n.
	 * 
	 * {n1,n2} Matches between n1 and n2 instances of the previous Pattern, where n1 and n2 are integers
	 * 
	 * {n1,} Matches at least n1 instance of the previous Pattern
	 * 
	 * Matches any number of (including zero) matches
	 * 
	 * ^ \A Anchors the pattern to the beginning of a string
	 * 
	 * $ \Z Anchors the pattern to the end of a string
	 * 
	 * \D Any non-digit
	 * 
	 * \d Any digit
	 * 
	 * \W Not a word character
	 * 
	 * \w A word character
	 * 
	 * \s A white-space
	 * 
	 * \S A non-white space
	 */
	public String getPattern()
	{
		return pattern;
	}

	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}

	public String toString()
	{
		return pattern;
	}
}