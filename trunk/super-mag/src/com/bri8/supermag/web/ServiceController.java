package com.bri8.supermag.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServiceController extends BaseController{
	
	@RequestMapping(value = { "/service/main" }, method = RequestMethod.GET)
	public ModelAndView showServieMainPage()
	{
		return getDefaultModelAndView("service/service_main");//vm path
	}

}
