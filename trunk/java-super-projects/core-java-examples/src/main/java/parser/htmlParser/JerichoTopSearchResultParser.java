package parser.htmlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utils.RegexUtil;

public class JerichoTopSearchResultParser {

	private static Log logger = LogFactory.getLog(JerichoTopSearchResultParser.class);
	public static final String OUT_FOLDER = "c:/junk/site-download/";
	public static String imagesHost = "images.google.com";
	public static String imagesPath = "/images?q=KEYWORD&btnG=Search&hl=en&gbv=2&tbs=isch%3A1&sa=2&start=0";
	public static String imageFilterPattern = "/imgres";
	public static String imageFetchRequestParameter = "imgurl";

	public static String scholarHost = "scholar.google.co.uk";
	public static String scholarPath = "/scholar?start=0&hl=en&q=KEYWORD&btnG=Search&as_sdt=2000";
	public static String scholarFilterPattern = "/imgres";
	public static String scholarFetchRequestParameter = "imgurl";

	public static void main(String[] args) throws Exception {

		String host = scholarHost;// imagesHost
		String path = scholarPath; // imagesPath
		String filterPattern = "";
		String fetchRequestParameter = "";
		String keyword = "god";

		path = path.replaceAll("KEYWORD", keyword);
		Map<String, Map<String, String>> urls = new JerichoTopSearchResultParser().doIt(host, path,  fetchRequestParameter);
		Map<String, Map<String, String>> urlsFiltered = new HashMap<String, Map<String, String>>();
		
		//do any filters here
		for (Map.Entry<String, Map<String, String>> entry : urls.entrySet()) {
			String anchor = entry.getKey();
			Map<String, String> attributes = entry.getValue();
			if (anchor.contains(".google.") || !anchor.startsWith("http:")) {
				continue;
			}
			urlsFiltered.put(anchor, attributes);
		}

		logger.info(urlsFiltered);
	}

	Map<String, Map<String, String>> doIt(String hostStr, String path, String fetchRequestParameter) throws Exception {
		HttpClient client = getHttpClient(hostStr);
		GetMethod getMethod = new GetMethod(path);
		System.out.println("downloading " + hostStr + path);
		try {
			client.executeMethod(getMethod);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("failed downloading " + path);
		}
		Source source = new Source(getMethod.getResponseBodyAsStream());
		source.fullSequentialParse();

		Map<String, Map<String, String>> anchors = getAnchors(source);
		Map<String, Map<String, String>> parameters = new HashMap<String, Map<String, String>>();

		for (Map.Entry<String, Map<String, String>> entry : anchors.entrySet()) {
			String anchor = entry.getKey();
			Map<String, String> attributes = entry.getValue();
			try {
				if (fetchRequestParameter != null && fetchRequestParameter.length() > 0) {
					String anchorTemp = anchor.substring(anchor.indexOf(fetchRequestParameter) + fetchRequestParameter.length() + 1);
					anchorTemp = anchorTemp.substring(0, anchorTemp.indexOf('&'));
					parameters.put(anchorTemp, attributes);
				} else {
					parameters.put(anchor, attributes);
				}
			} catch (Exception e) {
				logger.warn(e, e);// continue
			}

		}
		return parameters;
	}

	private HttpClient getHttpClient(String hostStr) {
		HttpClient client = new HttpClient();
		HostConfiguration hostConfiguration = new HostConfiguration();
		HttpHost host = new HttpHost(hostStr);
		hostConfiguration.setHost(host);

		client.setHostConfiguration(hostConfiguration);
		return client;
	}

	Map<String, Map<String, String>> getAnchors(Source source) {
		List<Element> elements = source.getAllElements(HTMLElementName.A);
		Map<String, Map<String, String>> anchors = new HashMap<String, Map<String, String>>();
		for (Element element : elements) {
			StartTag tag = element.getStartTag();
			Attribute hrefAttribute = tag.getAttributes().get("href");
			if (hrefAttribute == null || hrefAttribute.getTextExtractor() == null) {
				continue;
			}
			String href = hrefAttribute.getValue();
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("text", element.getTextExtractor().toString());
			anchors.put(href, attributes);
		}
		return anchors;
	}

}
