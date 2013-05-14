package velocity.codegenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class PortletCodeGeneratorMain {

	private static final String PORTLET_TITLE = "Editorial Calendar";

	private static final String PORTLET_NAME = "editorialcalendar";

	private static final String PORTLET_CONTROLLER_CLASS = "EditorialCalendarController";

	private static final String PORTLET_PREF_NAMES[] = { "title", "siteId", "urlForCalendar" };

	private static final String PORTLET_PREF_VALUES[] = { "Editorial Calendar", "4", "http://i.cmpnet.com/eet/mediakit/EET_2007_News_Cal.pdf" };

	private static final String PORTLET_SERVICE_ID = "";

	private static final String PORTLET_SERVICE_CLASS = "";

	private static final String PORTLET_SERVICE_DAO_ID = "";

	private static final String PORTLET_WEBSITE = "eetimes";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{

		/* first, get and initialize an engine */
		VelocityEngine ve = new VelocityEngine();
		ve.init();

		VelocityContext context = new VelocityContext();

		Map portlet = new HashMap();
		populatePortletDetails(portlet);

		context.put("portlet", portlet);
		context.put("date", new Date().toString());
		List templateList = new ArrayList();
		templateList.add(ve.getTemplate("config/portletTemplate.vm"));
		templateList.add(ve.getTemplate("config/springBeanPortletTemplate.vm"));
		templateList.add(ve.getTemplate("config/liferayPortletTemplate.vm"));
		templateList.add(ve.getTemplate("config/portletWebTemplate.vm"));
		templateList.add(ve.getTemplate("config/portletControllerTemplate.vm"));

		String result = new CodeGeneratorImpl().applyTemplates(templateList, context);
		System.out.println(result);

	}

	private static void populatePortletDetails(Map portlet)
	{
		portlet.put("name", PORTLET_NAME);
		portlet.put("title", PORTLET_TITLE);
		portlet.put("shortTitle", PORTLET_TITLE);
		portlet.put("controllerClass", PORTLET_CONTROLLER_CLASS);
		portlet.put("serviceDAO", PORTLET_SERVICE_DAO_ID);
		portlet.put("serviceId", PORTLET_SERVICE_ID);
		portlet.put("serviceClass", PORTLET_SERVICE_CLASS);
		portlet.put("website", PORTLET_WEBSITE);
		List prefList = new ArrayList();

		for (int i = 0; i < PORTLET_PREF_NAMES.length; i++) {
			String name = PORTLET_PREF_NAMES[i];
			String value = PORTLET_PREF_VALUES[i];
			Map nameValue = new HashMap();
			nameValue.put("name", name);
			nameValue.put("value", value);

			prefList.add(nameValue);
		}

		portlet.put("prefList", prefList);
	}

}
