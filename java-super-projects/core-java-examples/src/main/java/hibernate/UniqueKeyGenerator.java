/**
 * 
 */
package hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.engine.query.sql.NativeSQLQueryReturn;
import org.hibernate.engine.query.sql.NativeSQLQueryScalarReturn;
import org.hibernate.engine.query.sql.NativeSQLQuerySpecification;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.hibernate.util.PropertiesHelper;

/**
 * A hibernate unique identity generator.
 * <p>
 * Sample Usage in hbm: <hibernate-mapping><class><id>
 * <p>
 * <generator class="search.domain.hibernate.BMCUniqueKeyGenerator">
 * <p>
 * <param name="QUERY_KEY">SELECT_UNIQUE_KEY</param>
 * <p>
 * </generator>
 * <p>
 * Define a SQL query for unique key as <sql-query name="SELECT_UNIQUE_KEY">
 * @author sandeep.maloth
 * 
 */
public class UniqueKeyGenerator implements IdentifierGenerator, Configurable {

	private static final String QUERY_KEY = "QUERY_KEY";

	/** The logger. */
	private static Log logger = LogFactory.getLog(UniqueKeyGenerator.class.getName());

	private String queryKey;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.SessionImplementor, java.lang.Object)
	 */
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException
	{
		Query namedSQLQuery = session.getNamedSQLQuery(queryKey);

		List<?> l = namedSQLQuery.list();
		Long result = null;
		if (!l.isEmpty()) {
			result = (Long) l.get(0);
			if (logger.isInfoEnabled()) {
				logger.info("Read unique key using : " + namedSQLQuery.getQueryString() + " : " + result);
			}
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.hibernate.id.Configurable#configure(org.hibernate.type.Type, java.util.Properties, org.hibernate.dialect.Dialect)
	 */
	public void configure(Type type, Properties params, Dialect dialect) throws MappingException
	{
		this.queryKey = params.getProperty(QUERY_KEY);
		if (queryKey == null) {
			throw new MappingException(QUERY_KEY + " is not set as generator param.");
		}
	}

}
