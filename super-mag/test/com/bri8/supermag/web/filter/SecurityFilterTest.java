package com.bri8.supermag.web.filter;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.struts.mock.MockHttpServletRequest;
import org.apache.struts.mock.MockHttpServletResponse;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;

public class SecurityFilterTest {

	@Test
	public void testDoFilter() throws IOException, ServletException {
		SecurityFilter securityFilter = new SecurityFilter();
		MockHttpServletRequest req = new MockHttpServletRequest(){
			@Override
			public String getRequestURI() {
				return "/user/login";
			}
		};
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain chain = new MockFilterChain();
		

		securityFilter.doFilter(req, res, chain);
	}

}
