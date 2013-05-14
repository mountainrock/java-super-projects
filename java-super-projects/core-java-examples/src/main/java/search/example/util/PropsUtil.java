package search.example.util;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropsUtil {

	static PropertiesConfiguration config = null;
	private static Log logger = LogFactory.getLog(PropsUtil.class.getName());

	static {
		try {
			config = new PropertiesConfiguration("search.properties");
			FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
			reloadingStrategy.setRefreshDelay(1000 * 60 * 5);// 5 mins

			config.setReloadingStrategy(reloadingStrategy);
			logger.info("search properties loaded");
		} catch (ConfigurationException e) {
			logger.error(e, e);
		}
	}

	public static List getList(String key)
	{
		logger.debug("[" + key + "=" + config.getProperty(key) + "]");
		return (List) config.getList(key);
	}

	public static String get(String key)
	{
		return (String) config.getProperty(key);
	}
	public static int getInteger(String key)
	{
		return  config.getInt(key);
	}
}