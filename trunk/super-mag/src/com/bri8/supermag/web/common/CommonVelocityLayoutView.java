package com.bri8.supermag.web.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.context.Context;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

@Component
public class CommonVelocityLayoutView extends VelocityLayoutView {
	public final static String HEADER = "header";
	public final static String FOOTER = "footer";
	public final static String LEFTNAV = "leftnav";
	public final static String RIGHTNAV = "rightnav";
	public final static String SCRIPTSANDSTYLES = "scriptsandstyles";
	public final static String DEFAULT_TITLE = "defaultTitle";

	public final static String OUTLINENAV = "outlineNav";

	private Map<String, String> includes;

	public CommonVelocityLayoutView() {
		super();
		super.setUrl("page");
		includes = new HashMap<String, String>();
	}

	public void setFooter(final String footer) {
		includes.put(FOOTER, footer);
	}

	public void setHeader(final String header) {
		includes.put(HEADER, header);
	}

	public void setLeftNav(final String leftNav) {
		includes.put(LEFTNAV, leftNav);
	}

	public void setRightNav(final String rightNav) {
		includes.put(RIGHTNAV, rightNav);
	}

	public void setScriptsandstyles(final String scriptsandstyles) {
		includes.put(SCRIPTSANDSTYLES, scriptsandstyles);
	}

	public void setDefaultTitle(final String title) {
		includes.put(DEFAULT_TITLE, title);
	}

	public Map<String, String> getIncludes() {
		return Collections.unmodifiableMap(includes);
	}

	public void setOutlineNav(final String outlineNav) {
		includes.put(OUTLINENAV, outlineNav);
	}

	@Override
	protected void doRender(Context context, HttpServletResponse response) throws Exception {
		try {

			super.doRender(context, response);
		} catch (Exception rnfe) {
			StringBuffer logBuf = new StringBuffer().append(getClass().getName()).append(": Handling Velocity resource not found exception:\n").append(rnfe);
			logger.debug(logBuf.toString());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
