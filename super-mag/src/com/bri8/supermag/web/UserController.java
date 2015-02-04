package com.bri8.supermag.web;

import static com.bri8.supermag.util.WebConstants.HTTP_SESSION_KEY_USER;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.User;
import com.bri8.supermag.service.UserService;

@Controller("userController")
public class UserController extends BaseController{
	@Autowired UserService userService;
	
	@RequestMapping(value = { "/user/login" }, method = RequestMethod.GET)
	protected ModelAndView showLogin() throws Exception {
		return getDefaultModelAndView("user/login");
	}
	
	@RequestMapping(value = { "/user/publisheLogin" }, method = RequestMethod.GET)
	protected ModelAndView showPublisherLogin() throws Exception {
		return getDefaultModelAndView("user/publisherLogin");
	}
	
	@RequestMapping(value = { "/user/adminLogin" }, method = RequestMethod.GET)
	protected ModelAndView showAdminLogin() throws Exception {
		return getDefaultModelAndView("user/adminLogin");
	}
	
	@RequestMapping(value = { "/user/doLogin" }, method = RequestMethod.POST)
	public ModelAndView login(User user, HttpServletRequest request, HttpServletResponse response) {
		User loggedUser = userService.readByEmail(user.getEmail());
		ModelAndView mv = getDefaultModelAndView("user/login");
		if(loggedUser!=null && loggedUser.getPassword().equals(loggedUser.getPassword())){
			mv.addObject("message", "logged in!!");
			request.getSession(true).setAttribute(HTTP_SESSION_KEY_USER, loggedUser);
			try {
				response.sendRedirect("/user/dashboard");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}else{
			invalidateSession(request);
			
			mv.addObject("error", "Failed log in!!");
		}
		
		return mv;
	}
	
	@RequestMapping(value = { "/user/register" }, method = RequestMethod.GET)
	protected ModelAndView show() throws Exception {
		return getDefaultModelAndView("user/register");
	}
	
	@RequestMapping(value = { "/user/publisherRegister" }, method = RequestMethod.GET)
	protected ModelAndView showPublisherRegister() throws Exception {
		return getDefaultModelAndView("user/publisherRegister");
	}
	
	@RequestMapping(value = { "/user/create" }, method = RequestMethod.POST)
	public ModelAndView add(User user) {
		user.setCreatedDate(new Date());
		userService.create(user);
		ModelAndView mv = getDefaultModelAndView("user/register");
		mv.addObject("message", "Added!!");
		return mv;
	}

	@RequestMapping(value = { "/user/dashboard" }, method = RequestMethod.GET)
	protected ModelAndView showDashboard() throws Exception {
		return getDefaultModelAndView("user/dashboard");
	}

	@RequestMapping(value = { "/user/logout" }, method = RequestMethod.GET)
	protected ModelAndView logout(HttpServletRequest request) throws Exception {
		invalidateSession(request);
		return getDefaultModelAndView("user/login");
	}

	private void invalidateSession(HttpServletRequest request) {
		if(request.getSession()!=null)
			request.getSession().invalidate();
	}
}
