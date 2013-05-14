package utils;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil {
	private static ClassPathXmlApplicationContext springCtx;
	private static SpringUtil _instance;
	private static Log logger = LogFactory.getLog(SpringUtil.class.getName());

	public static synchronized SpringUtil getInstance(String[] springConfigurations)
	{
		if (_instance == null) {
			_instance = new SpringUtil();
			logger.info("Loading spring context from path :" + ReflectionToStringBuilder.toString(springConfigurations));
			_instance.springCtx = new ClassPathXmlApplicationContext(springConfigurations);
		}
		return _instance;
	}

	public Object getBean(String name)
	{
		return springCtx.getBean(name);
	}

}
