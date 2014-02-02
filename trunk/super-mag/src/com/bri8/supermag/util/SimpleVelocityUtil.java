package com.bri8.supermag.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * Utility for evaluating velocity templates using standard velocityEngine and not the liferay provided wrapper.
 * 
 */
public class SimpleVelocityUtil {

	private SimpleVelocityUtil() { // singleton
		try {
			velocityEngine = new VelocityEngine();
			velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
			velocityEngine.setProperty("runtime.log.logsystem.log4j.logger", "velocity");

			velocityEngine.init();
		} catch (Exception e) {
			throw new RuntimeException(e.toString(), e);
		}
	}

	public String evaluate(String template, Map<String, Object> variables) {

		VelocityContext velocityContext = new VelocityContext();
		loadCustomVariables(variables, velocityContext);

		StringWriter output = new StringWriter();
		try {
			velocityEngine.evaluate(velocityContext, output, SimpleVelocityUtil.class.getName(), template);
		} catch (Exception e) {
			throw new RuntimeException("Failed to evaluate template", e);
		}

		return output.toString();
	}

	private void loadCustomVariables(Map<String, Object> variables, VelocityContext velocityContext) {
		if (variables != null) {
			Iterator<Map.Entry<String, Object>> itr = variables.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, Object> entry = itr.next();
				String key = entry.getKey();
				Object value = entry.getValue();

				velocityContext.put(key, value);
			}
		}
	}

	private VelocityEngine velocityEngine;
	private static SimpleVelocityUtil _instance = new SimpleVelocityUtil();

	public static SimpleVelocityUtil getInstance() {
		return _instance;
	}
}