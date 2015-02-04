package org.test.struts2;

import com.opensymphony.xwork2.ActionSupport;

public class WelcomeUserAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6255423464022275346L;

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// all struts logic here
	public String execute() {

		return "SUCCESS";

	}

}
