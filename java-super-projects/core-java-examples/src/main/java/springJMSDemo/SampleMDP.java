/*package springJMSDemo;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;


public class SampleMDP implements MessageListener, InitializingBean {

	public void afterPropertiesSet() throws Exception
	{
		// TODO Auto-generated method stub

	}

	private static Logger logger = Logger.getLogger(SampleMDP.class);

	
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 
	public void onMessage(Message message)
	{
		System.out.println("MDP onMessage()... called ");
		if (message instanceof TextMessage) {
			try {
				System.out.println("Received text message :" + ((TextMessage) message).getText());
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		} else if (message instanceof Object) {
			try {
				System.out.println("Received object message :" + ((ObjectMessage) message).getObject());
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}

		} else {
			throw new IllegalArgumentException("Message must be of type TestMessage/ObjectMessage");
		}
	}

}
*/