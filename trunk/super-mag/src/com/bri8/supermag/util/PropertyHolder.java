package com.bri8.supermag.util;

import java.util.ArrayList;
import java.util.List;

import com.bri8.supermag.model.PropertyItem;

//singleton
public class PropertyHolder {
	private static PropertyHolder _instance = new PropertyHolder();

	private PropertyHolder() {
	}

	List<PropertyItem> languages = new ArrayList<PropertyItem>();
	List<PropertyItem> categories = new ArrayList<PropertyItem>();
	List<PropertyItem> frequency = new ArrayList<PropertyItem>();
	List<PropertyItem> currencies = new ArrayList<PropertyItem>();

	public static PropertyHolder getInstance() {
		return _instance;
	}

	public List<PropertyItem> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<PropertyItem> currencies) {
		this.currencies = currencies;
	}

	public List<PropertyItem> getLanguages() {
		return languages;
	}

	public void setLanguages(List<PropertyItem> languages) {
		this.languages = languages;
	}

	public List<PropertyItem> getCategories() {
		return categories;
	}

	public void setCategories(List<PropertyItem> categories) {
		this.categories = categories;
	}

	public List<PropertyItem> getFrequency() {
		return frequency;
	}

	public void setFrequency(List<PropertyItem> frequency) {
		this.frequency = frequency;
	}

}
