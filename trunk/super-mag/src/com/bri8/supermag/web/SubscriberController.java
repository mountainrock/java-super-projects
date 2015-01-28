package com.bri8.supermag.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.dto.CheckoutItem;
import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.IssuePage;
import com.bri8.supermag.model.IssueStatus;
import com.bri8.supermag.model.Magazine;
import com.bri8.supermag.model.MagazineIssues;
import com.bri8.supermag.model.User;
import com.bri8.supermag.service.MagazineService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@Controller("subscriberController")
public class SubscriberController extends BaseController{
	@Autowired MagazineService magazineService;

	private static Log logger = LogFactory.getLog(SubscriberController.class);

	@RequestMapping(value = { "/checkout/showAdd" }, method = RequestMethod.GET)
	protected ModelAndView showAddMagazine() throws Exception {
		return getDefaultModelAndView("checkout/showAdd");
	}


	@RequestMapping(value = { "/checkout/list" }, method = RequestMethod.GET)
	protected ModelAndView list(HttpServletRequest request) throws Exception {
		List<CheckoutItem> basket = (List<CheckoutItem>) request.getSession().getAttribute("basket");
		ModelAndView mv = getDefaultModelAndView("checkout/list");
		mv.addObject("basket", basket);

		return mv;
	}

}
