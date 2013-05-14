package hibernate.domainsample;

import hibernate.HibernateUtil;
import hibernate.domain.Event;

import java.util.Date;

import org.hibernate.Session;

/**
 * Refer http://docs.jboss.org/hibernate/core/3.3/reference/en/html/tutorial.html
 *<p>
 *To start hsql server run:
 *<p>
 * mvn exec:java -Dexec.mainClass="org.hsqldb.Server" -Dexec.args="-database.0 file:target/data/tutorial"
 * <p>
 * mvn exec:java -Dexec.mainClass="org.hibernate.tutorial.EventManager" -Dexec.args="store"
 * <p>
 * hsql client : http://sourceforge.net/projects/squirrel-sql/
 */
public class EventManager {

	public static void main(String[] args)
	{
		EventManager mgr = new EventManager();

		if (args[0].equals("store")) {
			mgr.createAndStoreEvent("My Event", new Date());
		}

		HibernateUtil.getSessionFactory().close();
	}

	private void createAndStoreEvent(String title, Date theDate)
	{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Event theEvent = new Event();
		theEvent.setTitle(title);
		theEvent.setDate(theDate);
		session.save(theEvent);

		session.getTransaction().commit();
	}

}