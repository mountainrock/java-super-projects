package com.bri8.supermag.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.MagazineIssues;
import com.bri8.supermag.model.User;
import com.bri8.supermag.util.PropertyHolder;

@Controller("adminController")
public class AdminController extends BaseController {

	@RequestMapping(value = { "/admin/dashboard" }, method = RequestMethod.GET)
	protected ModelAndView showPublisherDashboard(HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/admin" );
			return null;
		}
		ModelAndView mv = getDefaultModelAndView("admin/admin-dashboard");
		loadMagazines(user, mv);

		return mv;
	}

	@RequestMapping(value = { "/admin/magazines" }, method = RequestMethod.GET)
	protected ModelAndView showMagazines(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/admin" );
			return null;
		}
		ModelAndView mv = getDefaultModelAndView("admin/magazines");
		loadMagazines(user, mv);
		return mv;
	}
	
	@RequestMapping(value = { "/admin/properties" }, method = RequestMethod.GET)
	protected ModelAndView showCategories(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/admin" );
			return null;
		}
		ModelAndView mv = getDefaultModelAndView("admin/admin-properties");
		mv.addObject("categories",PropertyHolder.getInstance().getCategories());
		mv.addObject("languages",PropertyHolder.getInstance().getLanguages());
		mv.addObject("currencies",PropertyHolder.getInstance().getCurrencies());
		mv.addObject("frequencies",PropertyHolder.getInstance().getFrequency());
		return mv;
	}

	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
	protected ModelAndView showUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/admin" );
			return null;
		}
		ModelAndView mv = getDefaultModelAndView("admin/admin-users");
		mv.addObject("subscribers",userService.listUsersByType("subscriber"));
		mv.addObject("publishers",userService.listUsersByType("publisher"));
		mv.addObject("admins",userService.listUsersByType("admin"));
		return mv;
	}
	
	private void loadMagazines(User user, ModelAndView mv) {
		List<MagazineIssues> magazines = magazineService.listMagazineIssuesAll();
		mv.addObject("magazines", magazines);
	}


}
