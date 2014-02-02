package com.bri8.supermag.web.command;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {

	@RequestMapping(value = { "/", "/index", "/home" }, method = RequestMethod.GET)
	protected ModelAndView show() throws Exception {
		return new ModelAndView("index");
	}
}
