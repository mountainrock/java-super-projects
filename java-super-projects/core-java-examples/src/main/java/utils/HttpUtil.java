package utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtil {

	private static HttpUtil _instance;
	private Map<String, HttpClient> httpClients = new HashMap<String, HttpClient>();

	private static Log logger = LogFactory.getLog(HttpUtil.class.getName());
	public static final String HOST_GOOGLE_APP_ENGINE = "jshoutbox.appspot.com";

	public static final String[] supportedClients = { HOST_GOOGLE_APP_ENGINE,"localhost" };

	private HttpUtil() {
	}

	public static synchronized HttpUtil getInstance() {
		if (_instance == null) {
			_instance = new HttpUtil();
			for (int i = 0; i < supportedClients.length; i++) {
				String host = supportedClients[i];
				HttpClient client = new HttpClient();
				HostConfiguration hostConfiguration = new HostConfiguration();
				if(host.equals("localhost")){
					hostConfiguration.setHost(host,8888);
				}else{
					hostConfiguration.setHost(host);
				}
				client.setHostConfiguration(hostConfiguration);
				_instance.httpClients.put(host, client);
				// TODO: Deal with http connections timeout...pooling..performance stuff.
			}
		}

		return _instance;
	}

	public String post(String host, String relativePath, Map<String, String> params) {
		PostMethod post = new PostMethod(relativePath);
		logger.info("Posting to : " + host + post.getPath());
		String result = "";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			post.addParameter(new NameValuePair(key, value));
			logger.info("Post param : " + key +" = " +value);
		}
		HttpClient client = httpClients.get(host);
		try {
			int i = client.executeMethod(post);
			result = post.getResponseBodyAsString();
			result = result + "(Status=" + i + ")";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	/**
	 * Fetch a http URL using apache HTTP client.
	 */
	public byte[] getURL(String relativeURL, String host) {
		GetMethod getMethod = new GetMethod(relativeURL);
		logger.info("Fetching [relativeURL=" + relativeURL + ", host=" + host + "]");

		try {
			HttpClient client = httpClients.get(host);
			client.executeMethod(getMethod);
			byte[] responseBody = getMethod.getResponseBody();
			return responseBody;
		} catch (Exception e) {
			logger.error("Failed to get [relativeURL=" + relativeURL + ", host=" + host + "]");
			throw new RuntimeException(e);
		}
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
