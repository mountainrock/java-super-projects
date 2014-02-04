package com.bri8.supermag.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.util.Constants;
import com.bri8.supermag.web.view.CommonVelocityLayoutView;

public class BaseController {
	@Autowired	protected CommonVelocityLayoutView layoutView;
	
	protected ModelAndView getDefaultModelAndView(String viewName) {
		ModelAndView mav = new ModelAndView();
		layoutView.setUrl(viewName + ".vm");
		layoutView.setLayoutUrl(Constants.VIEW_LAYOUT_LAYOUT_VM);
		mav.setView(layoutView);
		mav.addAllObjects(layoutView.getIncludes());
		mav.addObject("title", viewName);
		return mav;
	}
}
