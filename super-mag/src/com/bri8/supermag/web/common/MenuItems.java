package com.bri8.supermag.web.common;

import java.util.Map;
import java.util.TreeMap;

public class MenuItems {

	public Map<String,String> publisherMenuItems= new TreeMap<String,String>();
	
	{
		publisherMenuItems.put("Dashboard","/user/dashboard");
		publisherMenuItems.put("ManageMagazine","/magazine/list");
	}

	public Map<String, String> getPublisherMenuItems() {
		return publisherMenuItems;
	}

	
}
