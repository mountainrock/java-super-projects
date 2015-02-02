package com.bri8.supermag.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.CheckoutItemDAO;
import com.bri8.supermag.dao.IssueDAO;
import com.bri8.supermag.model.CheckoutItem;
import com.bri8.supermag.model.Issue;

@Component("subscriberService")
public class SubscriberService {

	@Autowired CheckoutItemDAO checkoutItemDAO;
	@Autowired IssueDAO issueDao;

	// checkout services
	public void addItem(CheckoutItem checkoutItem) {
		if (checkoutItem.getIssueName() == null) {
			Issue issue = issueDao.read(checkoutItem.getIssueId(), Issue.class);
			checkoutItem.setIssueName(issue.getIssueName());
			checkoutItem.setCreatedDate(new Date());
		}
		checkoutItemDAO.create(checkoutItem);
	}

	public void removeItem(Long itemId) {
		checkoutItemDAO.delete(itemId, CheckoutItem.class);
	}

	public List<CheckoutItem> listCheckoutItems(Long userId) {
		return checkoutItemDAO.read(CheckoutItem.class, "userId ==" + userId, " order by createdDate DESC");
	}
	// checkout services end

}
