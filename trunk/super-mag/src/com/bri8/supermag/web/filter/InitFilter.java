package com.bri8.supermag.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bri8.supermag.model.PropertyItem;
import com.bri8.supermag.service.PropertyManagerService;
import com.bri8.supermag.util.PropertyHolder;

@Component("initFilter")
public class InitFilter implements Filter {

	String[] defaultCategories = { "News", "Business", "Education", "Entertainment", "Finance", "Health & Fitness", "Lifestyle", "Sports", "Travel", "Auto", "Men", "Women", "Spirituality",
			"Technology", "Fashion", "Art & Culture", "Architecture & Design", "Bollywood" };
	String[] languages = { "Assamese", "Arabic", "Bengali", "Chinese", "Dogri", "Dutch", "English", "French", "German", "Gujarati", "Hindi", "Italian", "Japanese", "Kannada", "Kashmiri", "Konkani",
			"Korean", "Latin", "Maithili", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Portuguese", "Punjabi", "Sanskrit", "Sindhi", "Spanish", "Tamil", "Telugu", "Urdu" };
	String[] frequencies = { "Daily", "Weekly", "Fortnightly", "Monthly", "Bimonthly", "Quarterly", "Annual", "Not Applicable" };

	private static Log logger = LogFactory.getLog(InitFilter.class);
	PropertyManagerService propertyManagerService;

	public void init(FilterConfig config) throws ServletException {
		logger.info(InitFilter.class + " initializing");

		WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		propertyManagerService = getPropertyManagementService(appContext);
		List<PropertyItem> categories = propertyManagerService.readByGroup("category");
		if (categories == null || categories.isEmpty()) {
			insertDefaultProperties();
		}
		logger.info("Loading properties");
		loadProperties();
	}

	private void loadProperties() {
		PropertyHolder.getInstance().setCategories(propertyManagerService.readByGroup("category"));
		PropertyHolder.getInstance().setLanguages(propertyManagerService.readByGroup("language"));
		PropertyHolder.getInstance().setFrequency(propertyManagerService.readByGroup("frequency"));
	}

	private PropertyManagerService getPropertyManagementService(WebApplicationContext appContext) {
		return (PropertyManagerService) appContext.getBean("propertyManagerService");
	}

	private void insertDefaultProperties() {
		// category
		for (String category : defaultCategories) {
			PropertyItem propertyItem = new PropertyItem("category", category);
			propertyManagerService.addProperty(propertyItem);
		}
		// languages
		for (String language : languages) {
			PropertyItem propertyItem = new PropertyItem("language", language);
			propertyManagerService.addProperty(propertyItem);
		}
		// frequency
		for (String frequency : frequencies) {
			PropertyItem propertyItem = new PropertyItem("frequency", frequency);
			propertyManagerService.addProperty(propertyItem);
		}

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		if ("true".equals(req.getParameter("RESET_PROPERTY_CACHE"))) {
			loadProperties(); // reload
		}
		chain.doFilter(req, res);
	}

	public void destroy() {
	}

}
