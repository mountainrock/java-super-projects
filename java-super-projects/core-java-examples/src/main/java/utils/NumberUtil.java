package utils;

public class NumberUtil {
	public static final String CLEANUP_NUMBER_PATTERN = "[[A-za-z ,]]";
	public Double sanitizePrice(String nseCurrentPrice) {
		nseCurrentPrice = RegexUtil.getInstance().findAndReplace(nseCurrentPrice, CLEANUP_NUMBER_PATTERN,"");

		return new Double(nseCurrentPrice.trim());
	}
	
	private static NumberUtil _instance;

	private NumberUtil() { //singleton
	}

	public static synchronized NumberUtil getInstance() {
		if (_instance == null)
			_instance = new NumberUtil();
		return _instance;
	}
}
