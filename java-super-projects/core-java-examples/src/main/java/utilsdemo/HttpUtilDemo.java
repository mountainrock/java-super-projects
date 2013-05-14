package utilsdemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.http.HttpUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import utils.Constants;

public class HttpUtilDemo {
	private static Log logger = LogFactory.getLog(HttpUtilDemo.class.getName());

	public static String addParameter(String url, String name, String value)
	{
		if (url == null) {
			throw new IllegalArgumentException("url not set");
		}

		if (url.indexOf(Constants.QUESTION) == -1) {
			url += Constants.QUESTION;
		}

		if (!url.endsWith(Constants.QUESTION) && !url.endsWith(Constants.AMPERSAND)) {

			url += Constants.AMPERSAND;
		}
		String enValue = null;
		try {
			enValue = URLEncoder.encode(value, Constants.UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.error(e, e);
		}
		return url + name + Constants.EQUAL + enValue;
	}

	public static String requestURL(String location)
	{

		HttpMethod method = null;

		try {
			if (location == null) {
				throw new IllegalArgumentException("location not set");
			} else if (!location.startsWith(Constants.HTTP_WITH_SLASH) && !location.startsWith(Constants.HTTPS_WITH_SLASH)) {

				location = Constants.HTTP_WITH_SLASH + location;
			}

			HttpClient client = new HttpClient();
			method = new GetMethod(location);
			method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			method.addRequestHeader("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
			method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

			client.executeMethod(method);

			return new String(method.getResponseBody());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (method != null) {
					method.releaseConnection();
				}
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}

	/**
	 * Returns the HTTP request headers as a formatted string.
	 */
	@SuppressWarnings("unchecked")
	public static String getHeaders(final HttpServletRequest request)
	{
		final String headers;
		final StringBuilder builder;
		builder = new StringBuilder();

		final Enumeration<String> names;
		names = request.getHeaderNames();

		while (names.hasMoreElements()) {
			final String name;
			name = names.nextElement();

			builder.append(name).append(": ");

			final Enumeration<String> values;
			values = request.getHeaders(name);

			if (values.hasMoreElements()) {
				final String value;
				value = values.nextElement();

				builder.append(value);
			}

			while (values.hasMoreElements()) {
				final String value;
				value = values.nextElement();

				builder.append(", ").append(value);
			}

			builder.append(System.getProperty("line.separator"));
		}

		headers = builder.toString();

		return headers;
	}

	/**
	 * Returns all the attributes available in the request as a formatted string.
	 */
	public static String getAttributes(HttpServletRequest httpRequest)
	{
		Enumeration<String> attributeNames = httpRequest.getAttributeNames();
		final StringBuilder builder = new StringBuilder();
		while (attributeNames.hasMoreElements()) {
			String name = attributeNames.nextElement();
			Object value = httpRequest.getAttribute(name);
			if (!(value instanceof ApplicationContext)) {
				builder.append(name).append(": ").append(ToStringBuilder.reflectionToString(value)).append(Constants.RETURN_NEW_LINE);
			}

		}
		return builder.toString();
	}

	/**
	 * Returns the HTTP request parameters as a formatted string.
	 */
	@SuppressWarnings("unchecked")
	public static String getParameters(final HttpServletRequest request)
	{
		final String parameters;

		final StringBuilder builder;
		builder = new StringBuilder();

		final Iterator<Entry<String, String[]>> iterator;
		iterator = request.getParameterMap().entrySet().iterator();

		while (iterator.hasNext()) {
			final Entry<String, String[]> entry;
			entry = iterator.next();

			final String name;
			name = entry.getKey();

			builder.append(name).append(": ");

			final String[] values;
			values = entry.getValue();

			if (values.length > 0) {
				final String value;
				value = values[0];

				builder.append(value);

				for (int index = 1, length = values.length; index < length; ++index) {
					builder.append(", ").append(value);
				}
			}

			builder.append(System.getProperty("line.separator"));
		}

		parameters = builder.toString();

		return parameters;
	}
	public byte[] getURL(String fullURL) {
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(fullURL);
		byte[] resp;
		try {
			int executeMethod = client.executeMethod(getMethod);
			resp = getMethod.getResponseBody();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return resp;
	}
}
