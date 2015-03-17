package com.bri8.supermag.web;

import static com.bri8.supermag.util.WebConstants.HTTP_SESSION_KEY_USER;
import static com.bri8.supermag.util.WebConstants.USER_TYPE_ADMIN;
import static com.bri8.supermag.util.WebConstants.USER_TYPE_PUBLISHER;
import static com.bri8.supermag.util.WebConstants.USER_TYPE_SUBSCRIBER;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.User;
import com.bri8.supermag.util.Constants;

@Controller("userController")
public class UserController extends BaseController {
	@RequestMapping(value = { "/user/login/{userType}" }, method = RequestMethod.GET)
	protected ModelAndView showLogin(@PathVariable("userType") String userType) throws Exception {
		String viewPath = getLoginView(userType);
		return getDefaultModelAndView(viewPath);
	}


	@RequestMapping(value = { "/user/doLogin" }, method = RequestMethod.POST)
	public ModelAndView login(User user, HttpServletRequest request, HttpServletResponse response) {
		String userType = user.getUserType();
		User loggedUser = userService.readByEmail(user.getEmail(), userType);
		String viewPath = getLoginView(userType);
		ModelAndView mv = getDefaultModelAndView(viewPath);
		if (loggedUser != null && loggedUser.getPassword().equals(loggedUser.getPassword())) {
			mv.addObject("message", "logged in!!");
			request.getSession(true).setAttribute(HTTP_SESSION_KEY_USER, loggedUser);
			try {
				String redirect = getRedirctionPostLoginPath(userType);
				response.sendRedirect(redirect);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			request.removeAttribute("user");
			invalidateSession(request);

			mv.addObject("error", "Invalid user details entered!!");
		}

		return mv;
	}


	private String getRedirctionPostLoginPath(String userType) {
		String redirect=null;
		if (USER_TYPE_SUBSCRIBER.equals(userType)) {
			redirect = "/subscriber/dashboard";
		} else if (USER_TYPE_PUBLISHER.equals(userType)) {
			redirect = "/publisher/dashboard";
		}else if (USER_TYPE_ADMIN.equals(userType)) {
			redirect = "/admin/dashboard";
		}
		return redirect;
	}

	@RequestMapping(value = { "/user/register/{userType}" }, method = RequestMethod.GET)
	protected ModelAndView showRegister(@PathVariable("userType") String userType) throws Exception {
		String viewPath = Constants.BLANK;
		if (USER_TYPE_SUBSCRIBER.equals(userType)) {
			viewPath = "user/register";
		} else if (USER_TYPE_PUBLISHER.equals(userType)) {
			viewPath = "user/publisherRegister";
		}else if (USER_TYPE_ADMIN.equals(userType)) {
			viewPath = "admin/admin-register";
		}
		return getDefaultModelAndView(viewPath);
	}

	@RequestMapping(value = { "/user/create" }, method = RequestMethod.POST)
	public ModelAndView add(User user, HttpServletRequest request) {
		user.setCreatedDate(new Date());

		String ipAddress = request.getHeader("X-FORWARDED-FOR"); // is client behind something?
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		user.setIpAddress(ipAddress);
		userService.create(user);
		String viewPath = getLoginView(user.getUserType());
		ModelAndView mv = getDefaultModelAndView(viewPath);
		mv.addObject("message", "Added!");
		return mv;
	}


	@RequestMapping(value = { "/user/logout/{userType}" }, method = RequestMethod.GET)
	protected ModelAndView logout(@PathVariable("userType") String userType, HttpServletRequest request) throws Exception {
		invalidateSession(request);
		String loginView = getLoginView(userType);
		return getDefaultModelAndView(loginView);
	}

	private String getLoginView(String userType) {
		String viewPath = Constants.BLANK;
		if (USER_TYPE_SUBSCRIBER.equals(userType)) {
			viewPath = "user/login";
		} else if (USER_TYPE_PUBLISHER.equals(userType)) {
			viewPath = "user/publisherLogin";
		} else if (USER_TYPE_ADMIN.equals(userType)) {
			viewPath = "user/adminLogin";
		}else{ //default
			viewPath = "user/login";
		}
		return viewPath;
	}
	private void invalidateSession(HttpServletRequest request) {
		if (request.getSession() != null)
			request.getSession().invalidate();
	}
}
