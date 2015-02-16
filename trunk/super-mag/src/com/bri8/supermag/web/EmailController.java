package com.bri8.supermag.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.MailContent;
import com.bri8.supermag.util.SimpleMail;

@Controller
public class EmailController extends BaseController{
	
	@RequestMapping(value = { "/email/showEmail"}, method = RequestMethod.GET)
	protected ModelAndView show( HttpServletRequest request) throws Exception {
		ModelAndView mv = getDefaultModelAndView(request,"common/email");

		return mv;
	}
	
	@RequestMapping(value = { "/email/send"}, method = RequestMethod.POST)
	protected ModelAndView send(@RequestParam(value = "password", required = true) String password, MailContent email, HttpServletRequest request) throws Exception {
		ModelAndView mv = getDefaultModelAndView(request,"common/email");
		if("om".equalsIgnoreCase(password)){
		SimpleMail.get_instance().sendMessageTo(email);
		mv.addObject("email", email);
		mv.addObject("message", "e-mail sent!");
		}else{
			mv.addObject("error", "invalid password!");
		}
		
		return mv;
	}
}
