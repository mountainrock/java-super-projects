package hibernate;

import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.ThreadLocalSessionContext;

/**
 * Not a test in its own right, but holds common behaviour for hibernate-based tests.
 * 
 * Date: 07-Nov-2007 Time: 14:59:26
 */
public abstract class HibernateTestCase extends TestCase {
	protected SessionFactory sessionFactory;
	protected Session session;

	protected void setUp() throws Exception
	{
		super.setUp();
		sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
		ThreadLocalSessionContext.bind(session);
	}

	protected void tearDown() throws Exception
	{
		ThreadLocalSessionContext.unbind(sessionFactory);
		session.close();
		sessionFactory.close();
		super.tearDown();
	}
}
