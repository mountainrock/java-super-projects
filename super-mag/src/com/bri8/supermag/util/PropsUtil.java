package com.bri8.supermag.util;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropsUtil {

	static PropertiesConfiguration config = null;
	private static Log logger = LogFactory.getLog(PropsUtil.class.getName());

	public static void init(String propFile) {
		try {
			if (config != null) {
				logger.warn("Properties file already initialized  from - " + config.getBasePath());
				return;
			}
			config = new PropertiesConfiguration(propFile);
			logger.info("Loading properties file " + config.getBasePath());

			FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
			reloadingStrategy.setRefreshDelay(1000 * 60 * 5);// 5 mins

			config.setReloadingStrategy(reloadingStrategy);
			logger.debug("Loading the following properties :");
			print();
			logger.info("Search properties loaded..");
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	private static void print() {
		for (Iterator<String> keys = config.getKeys(); keys.hasNext();) {
			String key = keys.next();
			logger.debug(key + " = " + config.getProperty(key));
		}
	}

	public static List<String> getList(String key) {
		logger.debug("[" + key + "=" + config.getProperty(key) + "]");
		return (List<String>) config.getList(key);
	}

	public static String get(String key) {
		return (String) config.getProperty(key);
	}

	public static void set(String key, String value) {
		try {
			config.setProperty(key, value);
			config.save(config.getBasePath());
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static void clear(String key) {
		try {
			config.clearProperty(key);
			config.save(config.getBasePath());
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

}