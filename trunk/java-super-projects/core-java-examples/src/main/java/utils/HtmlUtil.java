package utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HtmlUtil {

	private static Log logger = LogFactory.getLog(HtmlUtil.class);

	public List<String> getAnchors(String url) {
		logger.info("Getting anchors from : " + url);
		List<String> anchors = new ArrayList<String>();
		Source source;
		try {
			source = new Source(new URL(url));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		source.fullSequentialParse();

		List<Element> elements = source.getAllElements(HTMLElementName.A);
		for (Element element : elements) {
			Attribute hrefAttribute = element.getStartTag().getAttributes().get("href");
			String href = hrefAttribute != null ? hrefAttribute.getValue() : null;
			if (href != null)
				anchors.add(href);
		}

		return anchors;
	}

	public String getText(String url) {
		logger.info("Getting anchors from : " + url);
		List<String> anchors = new ArrayList<String>();
		Source source;
		try {
			source = new Source(new URL(url));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		source.fullSequentialParse();

		return source.getTextExtractor().toString();
	}

	private static HtmlUtil _instance = new HtmlUtil();

	private HtmlUtil() { // singleton
	}

	public static HtmlUtil getInstance() {
		return _instance;
	}
}
