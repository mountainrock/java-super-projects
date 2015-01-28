package com.bri8.supermag.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.CheckoutItem;
import com.bri8.supermag.model.User;
import com.bri8.supermag.service.MagazineService;
import com.bri8.supermag.service.SubscriberService;

@Controller("subscriberController")
public class SubscriberController extends BaseController{
	@Autowired MagazineService magazineService;
	@Autowired SubscriberService subscriberService;

	private static Log logger = LogFactory.getLog(SubscriberController.class);

	@RequestMapping(value = { "/checkout/showAdd" }, method = RequestMethod.GET)
	protected ModelAndView showAddMagazine() throws Exception {
		return getDefaultModelAndView("checkout/showAdd");
	}


	@RequestMapping(value = { "/checkout/addIssueToBasket" }, method = RequestMethod.POST)
	protected ModelAndView addIssueToBasket(HttpServletRequest request,HttpServletResponse response, CheckoutItem checkoutItem ) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login" );
			return null;
		}
		//add to basket
		subscriberService.addItem(checkoutItem);
		return getDefaultModelAndView("checkout/list");
	}
	
	@RequestMapping(value = { "/checkout/list" }, method = RequestMethod.GET)
	protected ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login" );
			return null;
		}
		List<CheckoutItem> items = subscriberService.listCheckoutItems(user.getUserId());
		ModelAndView mv = getDefaultModelAndView("checkout/list");
		mv.addObject("items", items);

		return mv;
	}


	private User getUser(HttpServletRequest request) {
		return (User)request.getSession(true).getAttribute("user");
	}

}
