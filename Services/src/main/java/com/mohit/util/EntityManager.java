package com.mohit.util;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class EntityManager {

	@Autowired
	private SessionFactory session;

	public Query createQuery(String sql) {
		return getSession().createQuery(sql);
	}

	public SessionFactory getSessionFactory() {
		return session;
	}

	public Session getSession() {
		try {
			return session.getCurrentSession();
		} catch (HibernateException e) {
			return session.openSession();
		}

	}

}
