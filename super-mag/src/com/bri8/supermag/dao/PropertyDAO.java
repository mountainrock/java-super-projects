package com.bri8.supermag.dao;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.bri8.supermag.model.PropertyItem;

@Repository
public class PropertyDAO extends BaseDAO<PropertyItem> {

	private static Log logger = LogFactory.getLog(PropertyDAO.class);


	public List<PropertyItem> readByGroup(String group) {
		 return read(PropertyItem.class, "group=='"+group+"'", "order by name asc",1000);
	}

}
