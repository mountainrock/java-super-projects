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

	
	private void loadMagazines(User user, ModelAndView mv) {
		List<MagazineIssues> magazines = magazineService.listMagazineIssues(user.getUserId());
		mv.addObject("magazines", magazines);
	}


}
