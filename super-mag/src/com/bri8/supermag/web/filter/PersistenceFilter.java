package com.bri8.supermag.web.filter;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.PMF;

@Component("persistenceFilter")
public class PersistenceFilter implements Filter {

	private static Log logger = LogFactory.getLog(PersistenceFilter.class);

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		PersistenceManager manager = null;
		try {
			manager = PMF.get().getPersistenceManager();
			chain.doFilter(req, res);
		} finally {
			if (manager != null && !manager.isClosed()) {
				manager.flush();
				manager.close();
				manager = null;
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		logger.info(PersistenceFilter.class + " initialized");
	}

	public void destroy() {
	}

}
