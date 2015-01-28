package com.bri8.supermag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.CheckoutItemDAO;
import com.bri8.supermag.model.CheckoutItem;

@Component("subscriberService")
public class SubscriberService {

	@Autowired
	CheckoutItemDAO checkoutItemDAO;
	//checkout services
	public void addItem(CheckoutItem checkoutItem) {
		checkoutItemDAO.create(checkoutItem);
	}

	public void removeItem(Long itemId) {
		checkoutItemDAO.delete(itemId, CheckoutItem.class);
	}
	
	public List<CheckoutItem> listCheckoutItems(Long userId) {
		return checkoutItemDAO.read(CheckoutItem.class, "userId ==" + userId, "createdDate");
	}
	//checkout services end

}
