package search.example.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.texen.util.FileUtil;

public class HttpUtil {
	private static Log logger = LogFactory.getLog(HttpUtil.class.getName());

	public static String addParameter(String url, String name, String value)
	{
		if (url == null) {
			return null;
		}

		if (url.indexOf(StringConstants.QUESTION) == -1) {
			url += StringConstants.QUESTION;
		}

		if (!url.endsWith(StringConstants.QUESTION) && !url.endsWith(StringConstants.AMPERSAND)) {

			url += StringConstants.AMPERSAND;
		}
		String enValue = null;
		try {
			enValue = URLEncoder.encode(value, StringConstants.UTF8);
		} catch (UnsupportedEncodingException e) {
			logger.error(e, e);
		}
		return url + name + StringConstants.EQUAL + enValue;
	}

	public static String requestURL(String location) throws IOException
	{

		HttpMethod method = null;

		try {
			if (location == null) {
				return "";
			} else if (!location.startsWith(StringConstants.HTTP_WITH_SLASH) && !location.startsWith(StringConstants.HTTPS_WITH_SLASH)) {

				location = StringConstants.HTTP_WITH_SLASH + location;
			}

			HttpClient client = new HttpClient();

			method = new GetMethod(location);

			method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

			method.addRequestHeader("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

			method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

			client.executeMethod(method);

			return new String(method.getResponseBody());
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

	public class Body {

		public Body(String content, String contentType, String charset) {
			_content = content;
			_contentType = contentType;
			_charset = charset;
		}

		public String getContent()
		{
			return _content;
		}

		public String getContentType()
		{
			return _contentType;
		}

		public String getCharset()
		{
			return _charset;
		}

		private String _content;
		private String _contentType;
		private String _charset;

	}
}
