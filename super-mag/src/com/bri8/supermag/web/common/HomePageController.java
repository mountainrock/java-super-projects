package com.bri8.supermag.web.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.util.Constants;
import com.bri8.supermag.web.BaseController;

@Controller
public class HomePageController extends BaseController{

	@RequestMapping(value = { "/", "/index", "/home" }, method = RequestMethod.GET)
	protected ModelAndView show() throws Exception {
		return getDefaultModelAndView("index");
	}
	
}
