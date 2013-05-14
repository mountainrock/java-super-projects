package hibernate;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.Type;

public class UniqueKeyGeneratorTest extends HibernateTestCase {
	UniqueKeyGenerator generator;

	protected void setUp() throws Exception
	{
		super.setUp();
		generator = new UniqueKeyGenerator();
	}

	public void testGenerate()
	{
		Properties params = new Properties();
		params.put("QUERY_KEY", "search.domain.UserSavedQuery.SELECT_UNIQUE_KEY");
		Type type = null;
		Dialect dialect = null;
		generator.configure(type, params, dialect);
		Object object = null;
		Serializable generated = generator.generate((SessionImplementor) session, object);

		assertNotNull(generated);
	}

	public void testConfigure()
	{
		Properties params = new Properties();
		params.put("QUERY_KEY", "search.domain.UserSavedQuery.SELECT_UNIQUE_KEY");
		Type type = null;
		Dialect dialect = null;
		generator.configure(type, params, dialect);
	}

}
