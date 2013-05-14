package spring;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;


public class SpringHibernateJunitTest extends AbstractTransactionalDataSourceSpringContextTests {
	Object alertsService;

	// ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-test.xml");
	@Override
	protected String[] getConfigLocations()
	{
		setDependencyCheck(false);
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath:applicationContext-test.xml" };
	}

	@Override
	protected void onSetUp() throws Exception
	{
		super.onSetUp();
		alertsService = (Object) getApplicationContext().getBean("articleAlertsService1");
	}

	public void testGetUserSavedQuery()

	{
		// do stuff
	}

	public void testSaveOrUpdateUserSavedQuery()
	{

		setDefaultRollback(true);
		// do stuff.
	}

}
