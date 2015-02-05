package com.bri8.supermag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.User;

@Component
public class UserService {

	@Autowired UserDAO userDao;

	public User readByEmail(String email, String userType) {
		return userDao.readByEmail(email, userType);
	}

	public void create(User user) {
		userDao.create(user);
		
	}
	
}
