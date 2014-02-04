package com.bri8.supermag.web.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.util.Constants;

/**
 * Simple controller that takes as inputs a LayoutView and a content pagename and puts them together
 */
@Controller("staticPageViewController")
public class StaticPageViewController {
	
	private static final String TITLE_PREFIX = "title.";
	private String pageParam = "page";
	@Autowired
	CommonVelocityLayoutView layoutView;

	private static Log logger = LogFactory.getLog(StaticPageViewController.class);

	@RequestMapping(value = "/static", method = RequestMethod.GET)
	public ModelAndView handleRequest(@RequestParam("page") String page) throws Exception {
		String propName = TITLE_PREFIX + page;
		logger.info("Looking for title: " + propName);// TODO:

		ModelAndView mav = getStaticModelAndView(page);
		return mav;
	}

	protected ModelAndView getStaticModelAndView(String page) {
		return getModelAndView(page);
	}

	private ModelAndView getModelAndView(String page) {
		ModelAndView mav = new ModelAndView();

		layoutView.setUrl(page);
		layoutView.setLayoutUrl(Constants.VIEW_LAYOUT_LAYOUT_VM);

		mav.setView(layoutView);
		mav.addAllObjects(layoutView.getIncludes());
		mav.addObject("title", "TODO:");
		return mav;
	}

	public String getPageParam() {
		return pageParam;
	}

	public void setPageParam(String pageVar) {
		this.pageParam = pageVar;
	}

}
