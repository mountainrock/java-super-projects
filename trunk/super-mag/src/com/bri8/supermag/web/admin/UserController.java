package com.bri8.supermag.web.admin;

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

@Controller("userAdmin")
public class UserController {

	@Autowired
	UserDAO userDAO;
	List<User> users;
	@Autowired
	ValidateUser validateUser;

	@RequestMapping(value = { "/user/admin" }, method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView show(@RequestParam(value = "password", required = false) String password, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("user-admin");
		if (validateUser.validate(ValidateUser.USER_ADMIN, password, request.getSession(true))) {
			mv.addObject("users", userDAO.readAll());
		} else {
			mv.addObject("error", "Invalid password");
		}
		return mv;
	}

	@RequestMapping(value = { "/user/all" }, method = RequestMethod.GET)
	public ModelAndView showAll() {
		ModelAndView mv = new ModelAndView("text");
		mv.addObject("text", userDAO.readAll());
		return mv;
	}

	@RequestMapping(value = { "/user/{id}" }, method = RequestMethod.GET)
	public ModelAndView read(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("user");
		mv.addObject("user", userDAO.read(id));
		return mv;
	}

	@RequestMapping(value = { "/user/add" }, method = RequestMethod.POST)
	public ModelAndView add(User user) {

		user.setDate(new Date());
		userDAO.create(user);
		users = null;
		ModelAndView mv = new ModelAndView("forward:/user/admin");
		mv.addObject("message", "Added!!");
		return mv;
	}

	@RequestMapping(value = { "/user/edit" }, method = RequestMethod.POST)
	public ModelAndView update(User user) {
		userDAO.update(user);
		users = null;
		ModelAndView mv = new ModelAndView("redirect:/user/admin");
		mv.addObject("message", "Updated!!");
		mv.addObject("users", userDAO.readAll());
		return mv;
	}

}
