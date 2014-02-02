package com.bri8.supermag.util;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.UserDAO;
import com.bri8.supermag.model.User;

@Component
public class ValidateUser {
	@Autowired
	UserDAO userDAO;


private static Log logger = LogFactory.getLog(ValidateUser.class);
	public static final String USER_ADMIN = "admin";
	public static final String USER_ADMIN_EMAIL = "adminemail"; 
	public static final String USER_GDOC_CONVERSION = "admingdoc"; 

	public static final String USER_GDOC_CONVERSION_EMAIL = "mysimpleconversion@gmail.com"; 

	public boolean validate(String userName, String password, HttpSession session) {
		if (password == null && session.getAttribute(userName) != null) {
			password = (String) session.getAttribute(userName);
		}
		if (password == null)
			return false;
		User storedUser = userDAO.readByName(userName);
		String storePassword = storedUser.getPassword();
		session.setAttribute(userName, storePassword);
		return (password.equals(storePassword));
	}
	
	public String getPassword(String userName) {
		User storedUser = userDAO.readByName(userName);
		String storePassword = storedUser.getPassword();
		return storePassword;
	}
}
