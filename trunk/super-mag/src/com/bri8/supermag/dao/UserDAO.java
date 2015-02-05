package com.bri8.supermag.dao;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.bri8.supermag.model.User;

@Repository
public class UserDAO extends BaseDAO<User> {

	private static Log logger = LogFactory.getLog(UserDAO.class);

	public void update(User user) {

		User storedUser = read(user.getUserId(), User.class);
		try {
			BeanUtils.copyProperties(storedUser, user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		super.update(user, User.class);
	}


	public User readByName(String userName) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			javax.jdo.Query q = pm.newQuery(String.format("select from %s where name=='%s'",User.class.getName() , userName));
			List<User> entries = (List<User>) q.execute();
			logger.debug(entries);
			return entries.get(0);
		} finally {
			pm.close();
		}
	}

	public User readByEmail(String email, String userType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			javax.jdo.Query q = pm.newQuery(String.format("select from %s where email=='%s' && userType=='%s'",User.class.getName(),email,userType));
			List<User> entries = (List<User>) q.execute();
			logger.debug(entries);
			return entries.isEmpty() ? null : entries.get(0);
		} finally {
			pm.close();
		}
	}

	public List<User> readAll() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			javax.jdo.Query q = pm.newQuery(String.format("select from %s order by date desc",  User.class.getName() ) );
			List<User> entries = (List<User>) q.execute();
			logger.debug(entries);
			return entries;
		} finally {
			pm.close();
		}
	}
}
