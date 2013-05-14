package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RegexUtil {

	/**
	 * Returns the position of the first found pattern. -1 if not found.
	 */
	public int find(String source, String pattern)
	{
		Matcher m = getMatcher(pattern, source);
		int i = 1;
		if (m.find()) {
			logger.debug("\t Found " + i++ + ". " + m.group());
			for (int j = 1; j < m.groupCount(); j++)// group 0 is the pattern itself
			{
				logger.debug("\t\t Group " + j + ". " + m.group(j));
			}
			return m.start();
		}
		return -1;
	}

	/**
	 * Returns all the position's of the found pattern. Empty list if not found.
	 */
	public List<Integer> findAllIndex(String source, String pattern)
	{
		Matcher m = getMatcher(pattern, source);
		List<Integer> list = new ArrayList<Integer>();
		int i = 1;
		while (m.find()) {
			logger.debug("\t Found " + i++ + ". " + m.group());
			for (int j = 1; j < m.groupCount(); j++)// group 0 is the pattern itself
			{
				logger.debug("\t\t Group " + j + ". " + m.group(j));
			}
			list.add(m.start());
		}
		return list;
	}

	/**
	 * Returns all the matched string groups. Empty list if not found.
	 */
	public List<String> findAllString(String source, String pattern) {
		Matcher m = getMatcher(pattern, source);
		List<String> list = new ArrayList<String>();

		if (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				list.add(m.group(i));
			}
		}
		return list;
	}

	/**
	 * Marks all the found pattern with a $ sign
	 */
	public String findAll(String source, String pattern, boolean mark)
	{
		StringBuffer buf = new StringBuffer(source);
		Matcher m = getMatcher(pattern, source);
		int i = 1;
		while (m.find()) {
			logger.debug("\t Found " + i++ + ". " + m.group());
			for (int j = 1; j < m.groupCount(); j++)// group 0 is the pattern itself
			{
				logger.debug("\t\t Group " + j + ". " + m.group(j));
			}

			if (mark) {
				buf.insert(m.start(), MARK_CHAR);
				buf.insert(m.end() + 1, MARK_CHAR);
			}
		}
		return buf.toString();
	}

	/**
	 * Matches the pattern in the source and insert's the 'insert' string at the found position.
	 */
	public StringBuffer matchAndInsert(String source, String pattern, String insert)
	{

		StringBuffer buffer = new StringBuffer(source);
		Matcher m = getMatcher(pattern, source);
		if (m.find()) {
			logger.debug("Inserting at pattern ::" + pattern + " ::contents:::" + insert);
			buffer.insert(m.end(), insert);
		}

		return buffer;
	}

	/**
	 * Find and replace
	 */
	public String findAndReplace(String source, String pattern, String replacePattern)
	{
		Matcher m = getMatcher(pattern, source);

		logger.debug("Replacing pattern ::" + pattern + " :: with contents:::" + replacePattern);

		if (m.find()) {
			return m.replaceAll(replacePattern);
		}

		return source;
	}

	protected Matcher getMatcher(String pattern, String input)
	{
		Pattern cachedPattern = patternCache.get(pattern);
		if (cachedPattern == null) {
			Pattern p = Pattern.compile(pattern);
			patternCache.put(pattern, p);
			cachedPattern = patternCache.get(pattern);
		}
		Matcher matcher = cachedPattern.matcher(input);
		return matcher;
	}

	private RegexUtil() { // singleton
	}

	public static RegexUtil getInstance()
	{
		return _instance;
	}

	public Map<String, Pattern> getPatternCache()
	{
		return patternCache;
	}

	public void setPatternCache(Map<String, Pattern> patternCache)
	{
		this.patternCache = patternCache;
	}

	public final static String MARK_CHAR = "$";
	private static Log logger = LogFactory.getLog(RegexUtil.class);

	private Map<String, Pattern> patternCache = new HashMap<String, Pattern>();

	private static RegexUtil _instance = new RegexUtil();

}
