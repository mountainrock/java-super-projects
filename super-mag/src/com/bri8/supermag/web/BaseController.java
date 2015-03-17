package com.bri8.supermag.web;

import static com.bri8.supermag.util.WebConstants.HTTP_SESSION_KEY_USER;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.dao.BaseDAO;
import com.bri8.supermag.model.User;
import com.bri8.supermag.service.MagazineService;
import com.bri8.supermag.util.Constants;
import com.bri8.supermag.util.PropertyHolder;
import com.bri8.supermag.web.common.CommonVelocityLayoutView;

public class BaseController {
	@Autowired	protected CommonVelocityLayoutView layoutView;
	@Autowired protected MagazineService magazineService;
	
	private static Log logger = LogFactory.getLog(BaseDAO.class);
	
	protected ModelAndView getDefaultModelAndView( HttpServletRequest request, String viewName) {
		ModelAndView mav = new ModelAndView();
		Device device = DeviceUtils.getCurrentDevice(request);
		logger.debug("device :"+device);
		String prefixMobile=(device.isMobile() ? "/m/":"");
		layoutView.setUrl( prefixMobile + viewName + ".vm");
		layoutView.setLayoutUrl(prefixMobile + Constants.VIEW_LAYOUT_LAYOUT_VM);
		mav.setView(layoutView);
		setDefaults(viewName, mav);
		return mav;
	}


	protected ModelAndView getDefaultModelAndView(String viewName) {
		ModelAndView mav = new ModelAndView();
		layoutView.setUrl(viewName + ".vm");
		layoutView.setLayoutUrl(Constants.VIEW_LAYOUT_LAYOUT_VM);
		mav.setView(layoutView);
		setDefaults(viewName, mav);
		return mav;
	}
	
	protected ModelAndView getDefaultModelAndViewNoLayout(String viewName) {
		ModelAndView mav = new ModelAndView();
		layoutView.setUrl("blank.vm");
		layoutView.setLayoutUrl(viewName + ".vm");
		mav.setView(layoutView);
		
		return mav;
	}
	private void setDefaults(String viewName, ModelAndView mav) {
		mav.addAllObjects(layoutView.getIncludes());
		mav.addObject("title", viewName);
		mav.addObject("propertyHolder",PropertyHolder.getInstance());
	}
	
	protected User getUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(HTTP_SESSION_KEY_USER);
		return user;
	}
}
