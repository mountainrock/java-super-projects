package com.bri8.supermag.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.PMF;

@Component
public class SecurityFilter implements Filter {

	private static Log logger = LogFactory.getLog(SecurityFilter.class);
	String publicPathAr[] = { "/user/login", "/user/register", "/service", "/","/static/*" };
	

	public void init(FilterConfig arg0) throws ServletException {
		logger.info(SecurityFilter.class + " initialized, URL publicPaths : " + publicPathAr);
	}
	
	/**
	 * <pre>
	 * 1. redirect to login for protected paths if user not logged in
	 * 2.
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String friendlyURL = request.getRequestURI();
		Object user = request.getSession(true).getAttribute("user");
		boolean isPrivate = checkPrivateAccess(friendlyURL);

		if (user == null && isPrivate) {
			request.setAttribute("message", "Please login to access this page");
			response.sendRedirect("/user/login");
		}

		chain.doFilter(req, res);
	}

	private boolean checkPrivateAccess(String friendlyURL) {
		boolean isPrivate = true;
		for (String path : publicPathAr) {
			if(!path.contains("*") && path.contains(friendlyURL)){
				isPrivate = false;
			}else if(path.contains("*") && friendlyURL.startsWith(StringUtils.substringBefore(path, "*"))){ //handle /static/*
				isPrivate = false;
			}
		}
		return isPrivate;
	}



	public void destroy() {
	}

}
