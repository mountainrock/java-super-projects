package hibernate;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class HibernateTest extends TestCase {
	/** The logger. */
	private static Log logger = LogFactory.getLog(HibernateTest.class.getName());
	ClassPathXmlApplicationContext factory;

	@Override
	protected void setUp() throws Exception
	{

		super.setUp();
		ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");
	}

	public void testQuery()
	{
		HibernateTemplate ht = (HibernateTemplate) factory.getBean("hibernateTemplate");
		String hql = "select cd.email, count(*) from ContactDetailsDTO cd group by  cd.email having cd.email like '%contact@unknown.email%'";
		List list = ht.find(hql);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object[] objects = (Object[]) iterator.next();
			logger.info(objects[0] + "\t\t " + objects[1]);
		}

	}
}
