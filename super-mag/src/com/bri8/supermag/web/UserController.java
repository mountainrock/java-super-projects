package com.bri8.supermag.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.User;
import com.bri8.supermag.util.ValidateUser;

@Controller("userController")
public class UserController extends BaseController{
	@Autowired UserDAO userDAO;
	
	@RequestMapping(value = { "/user/login" }, method = RequestMethod.GET)
	protected ModelAndView showLogin() throws Exception {
		return getDefaultModelAndView("user/login");
	}
	
	@RequestMapping(value = { "/user/doLogin" }, method = RequestMethod.POST)
	public ModelAndView login(User user, HttpServletRequest request) {
		User loggedUser = userDAO.readByName(user.getName());
		if(loggedUser==null){
			loggedUser = userDAO.readByEmail(user.getEmail());
		}
		ModelAndView mv = getDefaultModelAndView("user/login");
		if(loggedUser!=null && loggedUser.getPassword().equals(loggedUser.getPassword())){
			mv.addObject("message", "logged in!!");
			mv = getDefaultModelAndView("user/dashboard");
			request.getSession(true).setAttribute("user", loggedUser);
		}else{
			mv.addObject("error", "failed log in!!");
		}
		
		return mv;
	}
	
	@RequestMapping(value = { "/user/register" }, method = RequestMethod.GET)
	protected ModelAndView show() throws Exception {
		return getDefaultModelAndView("user/register");
	}
	
	@RequestMapping(value = { "/user/create" }, method = RequestMethod.POST)
	public ModelAndView add(User user) {
		user.setDate(new Date());
		userDAO.create(user);
		ModelAndView mv = getDefaultModelAndView("user/register");
		mv.addObject("message", "Added!!");
		return mv;
	}

	@RequestMapping(value = { "/user/dashboard" }, method = RequestMethod.GET)
	protected ModelAndView showDashboard() throws Exception {
		return getDefaultModelAndView("user/dashboard");
	}

	@RequestMapping(value = { "/user/magazine/showAdd" }, method = RequestMethod.GET)
	protected ModelAndView showAddMagazine() throws Exception {
		return getDefaultModelAndView("user/magazine/showAdd");
	}
	
	@RequestMapping(value = { "/user/logout" }, method = RequestMethod.GET)
	protected ModelAndView logout(HttpServletRequest request) throws Exception {
		request.getSession(true).removeAttribute("user");
		return getDefaultModelAndView("user/login");
	}
}
