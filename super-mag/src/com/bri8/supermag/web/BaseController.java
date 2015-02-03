package com.bri8.supermag.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.dao.BaseDAO;
import com.bri8.supermag.util.Constants;
import com.bri8.supermag.web.common.CommonVelocityLayoutView;
import com.bri8.supermag.web.common.MenuItems;

public class BaseController {
	@Autowired	protected CommonVelocityLayoutView layoutView;

	private static Log logger = LogFactory.getLog(BaseDAO.class);
	MenuItems menuItems = new MenuItems();
	
	protected ModelAndView getDefaultModelAndView( HttpServletRequest request, String viewName) {
		ModelAndView mav = new ModelAndView();
		Device device = DeviceUtils.getCurrentDevice(request);
		logger.debug("device :"+device);
		String prefixMobile=(device.isMobile() ? "/m/":"");
		layoutView.setUrl( prefixMobile + viewName + ".vm");
		layoutView.setLayoutUrl(prefixMobile + Constants.VIEW_LAYOUT_LAYOUT_VM);
		mav.setView(layoutView);
		mav.addAllObjects(layoutView.getIncludes());
		mav.addObject("title", viewName);
		mav.addObject("menuItems", menuItems);
		return mav;
	}
	
	
	protected ModelAndView getDefaultModelAndView(String viewName) {
		ModelAndView mav = new ModelAndView();
		layoutView.setUrl(viewName + ".vm");
		layoutView.setLayoutUrl(Constants.VIEW_LAYOUT_LAYOUT_VM);
		mav.setView(layoutView);
		mav.addAllObjects(layoutView.getIncludes());
		mav.addObject("title", viewName);
		mav.addObject("menuItems", menuItems);
		return mav;
	}
	
	protected ModelAndView getDefaultModelAndViewNoLayout(String viewName) {
		ModelAndView mav = new ModelAndView();
		layoutView.setUrl("blank.vm");
		layoutView.setLayoutUrl(viewName + ".vm");
		mav.setView(layoutView);
		mav.addAllObjects(layoutView.getIncludes());
		mav.addObject("title", viewName);
		mav.addObject("menuItems", menuItems);
		
		return mav;
	}
}
