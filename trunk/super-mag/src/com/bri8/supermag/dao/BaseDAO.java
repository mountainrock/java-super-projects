package com.bri8.supermag.dao;

import static com.bri8.supermag.util.Constants.SPACE;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class BaseDAO<T> {

	private static Log logger = LogFactory.getLog(BaseDAO.class);

	public T create(T entity) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(entity);
		} finally {
			pm.flush();
			pm.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public T update(T entity, Class clasz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(entity);
		} finally {
			pm.flush();
			pm.close();
		}
		return entity;

	}

	/**
	 * Use this to avoid * obj is managed by a different Object Manager
	 */
	@SuppressWarnings("unchecked")
	public T update(T entity, Long id, Class clasz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		T entityToUpdate = read(id, clasz, pm);
		try {
			BeanUtils.copyProperties(entityToUpdate, entity);
			pm.makePersistent(entityToUpdate);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pm.flush();
			pm.close();
		}
		return entityToUpdate;

	}

	@SuppressWarnings("unchecked")
	public T delete(Long id, Class clasz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		T entity = read(id, clasz, pm);
		try {
			pm.deletePersistent(entity);
		} finally {
			pm.flush();
			pm.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> read(Class clasz, String orderBy, int count) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(String.format("select from %s %s", clasz.getName() , orderBy));
		q.setRange(0, count);
		List<T> entries = (List<T>) q.execute();
		logger.debug(entries);
		return entries;
	}

	@SuppressWarnings("unchecked")
	public List<T> read(Class clasz, String where, String orderBy, int count) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(String.format("select from %s where %s %s" , clasz.getName(), where, orderBy));
		q.setRange(0, count);
		List<T> entries = (List<T>) q.execute();
		logger.debug(entries);
		return entries;
	}

	@SuppressWarnings("unchecked")
	public List<T> read(Class clasz, String where, List<String> contains, String orderBy, int count) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery("select from " + clasz.getName() + " where " + where + SPACE + orderBy);
		q.setRange(0, count);
		List<T> entries = (List<T>) q.execute(contains);
		logger.debug(entries);
		return entries;
	}

	@SuppressWarnings("unchecked")
	public List<T> read(Class clasz, String where, String orderBy) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery("select from " + clasz.getName() + SPACE + " where " + where + SPACE + orderBy);
		List<T> entries = (List<T>) q.execute();
		logger.debug(entries);
		return entries;
	}

	@SuppressWarnings("unchecked")
	public T read(Long id, Class clasz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		T object = read(id, clasz, pm);
		return object;
	}

	@SuppressWarnings("unchecked")
	public T read(Long id, Class clasz, PersistenceManager pm) {
		Key k = KeyFactory.createKey(clasz.getSimpleName(), id);
		T q = (T) pm.getObjectById(clasz, k);
		return q;
	}

}
