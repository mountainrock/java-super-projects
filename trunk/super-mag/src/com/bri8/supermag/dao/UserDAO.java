package com.bri8.supermag.dao;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.bri8.supermag.model.User;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Component
public class UserDAO {

	private static Log logger = LogFactory.getLog(UserDAO.class);

	public void create(User user) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(user);
		} finally {
			pm.close();
		}
	}

	public void update(User user) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			User storedUser = read(user.getId(), pm);
			storedUser.setName(user.getName());
			storedUser.setPassword(user.getPassword());
			// pm.makePersistent(quote);
		} finally {
			pm.close();
		}
	}

	private User read(Long id, PersistenceManager pm) {
		Key k = KeyFactory.createKey(User.class.getSimpleName(), id);
		User u = pm.getObjectById(User.class, k);
		return u;
	}

	public User read(Long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return read(id, pm);
		} finally {
			pm.close();
		}
	}

	public void delete(Long id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(read(id, pm));
		} finally {
			pm.close();
		}
	}

	public User readByName(String name) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			javax.jdo.Query q = pm.newQuery("select from " + User.class.getName() + " where name=='"+name+"' order by date desc");
			List<User> entries = (List<User>) q.execute();
			logger.debug(entries);
			return entries.get(0);
		} finally {
			pm.close();
		}
	}
	public List<User> readAll() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			javax.jdo.Query q = pm.newQuery("select from " + User.class.getName() + " order by date desc");
			List<User> entries = (List<User>) q.execute();
			logger.debug(entries);
			return entries;
		} finally {
			pm.close();
		}
	}
}
