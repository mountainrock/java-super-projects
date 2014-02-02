package com.bri8.supermag.dao;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bri8.supermag.model.ShoutEntry;
import com.bri8.supermag.util.DateUtil;

public class ShoutDAO {

	private static Log logger = LogFactory.getLog(ShoutDAO.class);

	public void shout(String shouter, String content, String ip) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// ... do stuff with pm ...
			ShoutEntry s = new ShoutEntry();
			s.setShouter(shouter);
			s.setContent(content);
			s.setDate(new Date());
			s.setIp(ip);
			pm.makePersistent(s);
		} finally {
			pm.close();
		}
	}

	public List<ShoutEntry> readAll() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			javax.jdo.Query q = pm.newQuery("select from " + ShoutEntry.class.getName() + " order by date desc");
			List<ShoutEntry> entries = (List<ShoutEntry>) q.execute();
			logger.debug(entries);
			return entries;
		} finally {
			pm.close();
		}
	}
}
