package com.bri8.supermag.web.filter;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.PMF;

@Component
public class SecurityFilter implements Filter {

	private static Log logger = LogFactory.getLog(SecurityFilter.class);

	//list for publisher/subscriber/admin
	/**
	 * <pre>
	 * 1. redirect to login for protected paths if user not logged in
	 * 2. 
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		String friendlyURL = request.getRequestURI();
		Object user = request.getSession(true).getAttribute("user");
		
		if(user==null && !(friendlyURL.contains("/user/login") && friendlyURL.contains("/user/"))){
			request.setAttribute("message", "Invalid page access");
			response.sendRedirect("/user/login");
		}
		
		chain.doFilter(req, res);
	}

	public void init(FilterConfig arg0) throws ServletException {
		logger.info(SecurityFilter.class + " initialized");
	}

	public void destroy() {
	}

}
