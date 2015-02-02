package com.bri8.supermag.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.MagazineIssues;
import com.bri8.supermag.service.MagazineService;

@Controller
public class HomePageController extends BaseController{
	@Autowired MagazineService magazineService;
	
	@RequestMapping(value = { "/", "/index", "/home", "/m/" }, method = RequestMethod.GET)
	protected ModelAndView show(HttpServletRequest request) throws Exception {
		//TODO: filter by magazineGroup
		List<MagazineIssues> magazines = magazineService.listMainPageMagazineIssues("all");
		ModelAndView mv = getDefaultModelAndView(request,"index");
		mv.addObject("magazines", magazines);

		return mv;
	}
}
