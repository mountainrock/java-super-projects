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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bri8.supermag.model.CheckoutItem;
import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.IssuePage;
import com.bri8.supermag.model.Magazine;
import com.bri8.supermag.model.SubscriptionType;
import com.bri8.supermag.model.User;
import com.bri8.supermag.service.MagazineService;
import com.bri8.supermag.service.SubscriberService;
import com.bri8.supermag.util.WebConstants;

@Controller("subscriberController")
public class SubscriberController extends BaseController{
	@Autowired MagazineService magazineService;
	@Autowired SubscriberService subscriberService;

	private static Log logger = LogFactory.getLog(SubscriberController.class);

	@RequestMapping(value = { "/checkout/showAddIssue" }, method = RequestMethod.GET)
	protected ModelAndView showAddIssue(HttpServletRequest request,HttpServletResponse response, @RequestParam("issueId") Long issueId) throws Exception {
		//load issue and magazine details
		Issue issue = magazineService.getIssue(issueId);
		IssuePage issuePage = magazineService.getIssueFrontPageByIssueId(issue);
		Magazine magazine = magazineService.getMagazine(issue.getMagazineId());
		ModelAndView mv = getDefaultModelAndView("checkout/showAdd");
		mv.addObject("issue", issue);
		mv.addObject("issuePage", issuePage);
		mv.addObject("magazine", magazine);
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/subscriber" );
			return null;
		}
		return mv;
	}


	@RequestMapping(value = { "/checkout/addIssueToBasket" }, method = RequestMethod.POST)
	protected void addIssueToBasket( @RequestParam("action") String action , HttpServletRequest request,HttpServletResponse response, CheckoutItem checkoutItem ) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/subscriber" );
		}
		Magazine magazine = magazineService.getMagazine(checkoutItem.getMagazineId());
		//add to basket
		if("Buy Single Issue".equals(action)){
			checkoutItem.setSubscriptionType(SubscriptionType.single.name());
			checkoutItem.setPrice(magazine.getPricePerIssue());
			checkoutItem.setCurrency(magazine.getCurrency());
		}else if("Buy 3 months Subscription".equals(action)){
			checkoutItem.setSubscriptionType(SubscriptionType.months3.name());
			checkoutItem.setPrice(magazine.getIssuePrice3months());
			checkoutItem.setCurrency(magazine.getCurrency());
		}else if("Buy 6 months Subscription".equals(action)){
			checkoutItem.setSubscriptionType(SubscriptionType.months6.name());
			checkoutItem.setPrice(magazine.getIssuePrice6months());
			checkoutItem.setCurrency(magazine.getCurrency());
		}else if("Buy 12 months Subscription".equals(action)){
			checkoutItem.setSubscriptionType(SubscriptionType.months12.name());
			checkoutItem.setPrice(magazine.getIssuePrice12months());
			checkoutItem.setCurrency(magazine.getCurrency());
		}
		subscriberService.addItem(checkoutItem);
		response.sendRedirect("/checkout/list");
	}
	
	@RequestMapping(value = { "/checkout/delete" }, method = RequestMethod.GET)
	protected ModelAndView delete(@RequestParam("itemId") Long checkoutItemId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/subscriber" );
			return null;
		}
		subscriberService.removeItem(checkoutItemId);
		ModelAndView mv = new ModelAndView("redirect:/checkout/list");

		return mv;
	}
	
	@RequestMapping(value = { "/checkout/list" }, method = RequestMethod.GET)
	protected ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception {
		User user = getUser(request);
		if(user==null){
			response.sendRedirect("/user/login/subscriber" );
			return null;
		}
		List<CheckoutItem> items = subscriberService.listCheckoutItems(user.getUserId());
		Float total = 0.0f;
		for (CheckoutItem checkoutItem : items) {
			total  = total + checkoutItem.getPrice();
		}
		ModelAndView mv = getDefaultModelAndView("checkout/list");
		mv.addObject("items", items);
		mv.addObject("total", total);

		return mv;
	}
	
	//TODO: clean up after checkout payment complete


	private User getUser(HttpServletRequest request) {
		return (User)request.getSession(true).getAttribute(WebConstants.HTTP_SESSION_KEY_USER);
	}

}
