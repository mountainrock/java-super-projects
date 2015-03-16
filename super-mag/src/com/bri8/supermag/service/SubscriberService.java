package com.bri8.supermag.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.CheckoutItemDAO;
import com.bri8.supermag.dao.IssueDAO;
import com.bri8.supermag.dao.PurchaseDAO;
import com.bri8.supermag.model.CheckoutItem;
import com.bri8.supermag.model.Issue;
import com.bri8.supermag.model.Purchase;

@Component("subscriberService")
public class SubscriberService {

	@Autowired CheckoutItemDAO checkoutItemDAO;
	@Autowired IssueDAO issueDao;
	@Autowired PurchaseDAO purchaseDAO;

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
	
	public List<Purchase> checkoutConfirm(Long userId){
		List<CheckoutItem> checkoutItems = listCheckoutItems(userId);
		List<Purchase> purchases = new ArrayList<Purchase>(); 
		for (CheckoutItem checkoutItem : checkoutItems) {
			Purchase purchase = new Purchase();
			purchase.setIssueId(checkoutItem.getIssueId());
			purchase.setCurrency(checkoutItem.getCurrency());
			purchase.setIssueName(checkoutItem.getIssueName());
			purchase.setMagazineId(checkoutItem.getMagazineId());
			purchase.setPrice(checkoutItem.getPrice());
			purchase.setSubscriptionType(checkoutItem.getSubscriptionType());
			purchase.setUserId(userId);
			purchase.setCreatedDate(new Date());
			
			purchaseDAO.create(purchase);
			purchases.add(purchase);
		}
		return purchases;
		
	}
	// checkout services end

	public List<Purchase> listPurchases(Long userId) {
		return purchaseDAO.read(Purchase.class, "userId ==" + userId, " order by createdDate DESC");
	}
}
