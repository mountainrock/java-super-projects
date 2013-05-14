/**
 * 
 */
package springJMSDemo;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;


public class JmsDemoTest {
	private static Logger logger = Logger.getLogger(JmsDemoTest.class);

	ClassPathXmlApplicationContext classPathXmlApplicationContext;

	DefaultMessageListenerContainer listenerContainer;

	JmsDemo jmsDemo;

	String springConfig = "spring-applicationContext.xml";

	public void setUp()
	{
		classPathXmlApplicationContext = new ClassPathXmlApplicationContext(springConfig);
		jmsDemo = (JmsDemo) classPathXmlApplicationContext.getBean("jmsDemo");
		listenerContainer = (DefaultMessageListenerContainer) classPathXmlApplicationContext.getBean("listenerContainer");

		final boolean mlContainerActive = listenerContainer.isActive();
		if (!mlContainerActive) {
			System.out.println("Message listener container is not active....Trying to Start now...");
			listenerContainer.start();
		}
	}

	public void testSendTextMessage()
	{
		jmsDemo.sendMessage("Example text Message");
		logger.info("sent msg ........");
	}

	public void testSendObjectMessage()
	{
		jmsDemo.sendMessage(new Serializable() {
			public String toString()
			{
				return "this is a object";
			}
		});
		logger.info("sent obj msg ........");
	}

	public void tearDown()
	{

		listenerContainer.destroy();
	}

	public static void main(String[] args)
	{
		ClassPathXmlApplicationContext classPathXmlApplicationContext;
		DefaultMessageListenerContainer listenerContainer;
		JmsDemo jmsDemo;
		String springConfig = "spring-applicationContext.xml";

		classPathXmlApplicationContext = new ClassPathXmlApplicationContext(springConfig);
		jmsDemo = (JmsDemo) classPathXmlApplicationContext.getBean("jmsDemo");
		listenerContainer = (DefaultMessageListenerContainer) classPathXmlApplicationContext.getBean("listenerContainer");

		final boolean mlContainerActive = listenerContainer.isActive();
		if (!mlContainerActive) {
			System.out.println("Message listener container is not active....Trying to Start now...");
			listenerContainer.start();
		}
		jmsDemo.sendMessage(new Serializable() {
			public String toString()
			{
				return "this is a object";
			}
		});
		logger.info("sent obj msg ........");
	}
}
