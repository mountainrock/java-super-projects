package com.bri8.supermag.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.User;
import com.bri8.supermag.service.MagazineService;

@Controller("adminController")
public class AdminController extends BaseController {
	@Autowired MagazineService magazineService;

	@RequestMapping(value = { "/admin/dashboard" }, method = RequestMethod.GET)
	protected ModelAndView showPublisherDashboard(HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/admin" );
			return null;
		}
		ModelAndView mv = getDefaultModelAndView("admin/admin-dashboard");

		return mv;
	}
	
	@RequestMapping(value = { "/admin/magazines" }, method = RequestMethod.GET)
	protected ModelAndView showMagazines() throws Exception {
		return getDefaultModelAndView("admin/magazines");
	}



}
