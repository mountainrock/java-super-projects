package com.bri8.supermag.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bri8.supermag.dao.PropertyDAO;
import com.bri8.supermag.model.PropertyItem;

@Component("propertyManagerService")
public class PropertyManagerService {

	@Autowired PropertyDAO propertyDao;

	public List<PropertyItem> readByGroup(String group) {
		return propertyDao.readByGroup(group);
	}
	public void addProperty(PropertyItem propertyItem){
		propertyDao.create(propertyItem);
	}
	
}
