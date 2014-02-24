package com.bri8.supermag.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.Magazine;
import com.bri8.supermag.model.User;
import com.bri8.supermag.service.MagazineService;

@Controller("magazineController")
public class MagazineController extends BaseController{
	@Autowired MagazineService magazineService;
	
	@RequestMapping(value = { "/magazine/showAdd" }, method = RequestMethod.GET)
	protected ModelAndView showAddMagazine() throws Exception {
		return getDefaultModelAndView("magazine/showAdd");
	}
	
	@RequestMapping(value = { "/magazine/create" }, method = RequestMethod.POST)
	public ModelAndView create(Magazine magazine, HttpServletRequest request) {
		
		User user = (User) request.getSession().getAttribute("user");
		magazine.setUserId(user.getUserId());
		magazineService.createMagazine(magazine);
		
		ModelAndView mv = getDefaultModelAndView("magazine/showAdd");
		if(magazine!=null && magazine.getMagazineId()!=null){
			mv.addObject("message", "magazine created sucessfully : "+ magazine.getMagazineId());
			mv.addObject("magazine",magazine);
		}else{
			mv.addObject("error", "Failed to create magazine!!");
		}
		
		return mv;
	}
	
	@RequestMapping(value = { "/magazine/list" }, method = RequestMethod.GET)
	protected ModelAndView list(HttpServletRequest request) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		List<Magazine> magazines = magazineService.listMagazine(user.getUserId());
		ModelAndView mv = getDefaultModelAndView("magazine/list");
		mv.addObject("magazines", magazines);
		
		return mv;
	}
	
}
