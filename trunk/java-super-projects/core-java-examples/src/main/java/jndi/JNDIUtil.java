package jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class JNDIUtil {

	/**
	 * Get initial JNDI context for weblogic
	 * 
	 * @param url Weblogic URL.
	 * @exception NamingException if problem occurs with JNDI context interface
	 */
	public static InitialContext getInitialContextForOracle(String url) throws NamingException
	{
		String JNDI_FACTORY = "oracle.j2ee.rmi.RMIInitialContextFactory";
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_PRINCIPAL, "oc4jadmin");
		env.put(Context.SECURITY_CREDENTIALS, "admin");
		env.put(Context.URL_PKG_PREFIXES, "oracle.oc4j.naming.url");
		InitialContext initialContext = new InitialContext(env);

		return initialContext;
	}

	// /**
	// * Get initial JNDI context for weblogic
	// *
	// * @param url
	// * Weblogic URL.
	// * @exception NamingException
	// * if problem occurs with JNDI context interface
	// */
	// public static InitialContext getInitialContextForWeblogic(String url) throws NamingException
	// {
	// String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
	// Hashtable env = new Hashtable();
	// env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
	// env.put(Context.PROVIDER_URL, url);
	// env.put("weblogic.jndi.createIntermediateContexts", "true");
	// return new InitialContext(env);
	// }

	// public static void main(String[] args) throws NamingException, JMSException
	// {
	// InitialContext ic = getInitialContextForOracle("ormi://localhost");//getInitialContextForWeblogic("t3://sandeep-maloth:7001");
	// final String tcfJNDIName = "company.poc.integration.aq.POCTCF";
	// final String topicJNDIName = "java:comp/resource/aqojmsRP/Topics/pp2_coreIdTopic";
	//
	// //Object tcf = ic.lookup(tcfJNDIName);
	// Object topic = ic.lookup(topicJNDIName);
	//
	// System.out.println(topic);
	//
	// }

	public static void main(String[] args)
	{
		System.getenv();
	}
}
