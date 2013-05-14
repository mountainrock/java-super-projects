package utils;

import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtil {
	private static VelocityEngine ve = null;
	static {
		try {
			ve = new VelocityEngine();
			ve.init();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String applyTemplate(String templateLocation, VelocityContext context)
	{

		StringWriter writer = new StringWriter();
		Template t;

		try {
			t = ve.getTemplate(templateLocation);
			t.merge(context, writer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return writer.toString();
	}
}
