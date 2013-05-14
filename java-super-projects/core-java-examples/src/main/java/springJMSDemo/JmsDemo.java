/**
 * 
 */
package springJMSDemo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.core.JmsTemplate;

/**
 * Ref: http://java.sun.com/products/jms/docs.html -->download jms1.1 http://static.springframework.org/spring/docs/2.0.x/reference/jms.html#jms-asynchronousMessageReception
 * http://blog.springframework.com/benh/archives/2006/04/09/spring-20s-jms-improvements/
 * 
 * For asynchronous messaging(publish subscriber / Topic connection), use the following libs: JMS1.1.jars Spring2.0+.jar only supports async.
 * 
 * Note: j2ee.jar also has a jms1.0 jar, be careful with the order of lib loading... The jmsTemplate uses jms1.1 for aync com. ...
 * @author Sandeep.Maloth
 * 
 */
public class JmsDemo implements InitializingBean {
	JmsTemplate jmsTemplate = null;
	private static Logger logger = Logger.getLogger(JmsDemo.class);

	public JmsTemplate getJmsTemplate()
	{
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMessage(Object obj)
	{
		jmsTemplate.convertAndSend(obj);
	}

	public void afterPropertiesSet() throws Exception
	{
		if (jmsTemplate == null) {
			logger.error("jmsTemplate not set ");
			throw new IllegalStateException("jmsTemplate not set.");
		}

	}

}
