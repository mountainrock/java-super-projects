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


}
