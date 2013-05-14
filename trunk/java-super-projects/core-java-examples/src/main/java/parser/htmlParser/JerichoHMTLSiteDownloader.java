package parser.htmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JerichoHMTLSiteDownloader {

	private static Log logger = LogFactory.getLog(JerichoHMTLSiteDownloader.class);
	public static final String OUT_FOLDER = "c:/junk/site-download/";

	public static void main(String[] args) throws Exception {

		String url = "http://google.com";
		new JerichoHMTLSiteDownloader().doIt(url);
	}

	String doIt(String url) throws Exception {
		download(url);
		Source source = new Source(new URL(url));
		source.fullSequentialParse();
		OutputDocument outputDocument = new OutputDocument(source);

		// System.out.println("\nAll text from TABLE (exluding content inside SCRIPT and STYLE elements):");
		downloadAnchors(source, outputDocument);

		String proxified = outputDocument.toString();
		logger.info(proxified);
		return proxified;
	}

	void downloadAnchors(Source source, OutputDocument outputDocument) {
		List<Element> elements = source.getAllElements(HTMLElementName.A);
		for (Element element : elements) {
			Attribute hrefAttribute = element.getStartTag().getAttributes().get("href");
			String href = hrefAttribute.getValue();
			download(href);
		}
	}
	
	void proxyAnchor(Source source, OutputDocument outputDocument) {
		List<Element> elements = source.getAllElements(HTMLElementName.A);
		for (Element element : elements) {
			Attribute hrefAttribute = element.getStartTag().getAttributes().get("href");
			String href = hrefAttribute.getValue();
			String proxyTemp = getProxy(href);
			outputDocument.replace(hrefAttribute.getValueSegment(), proxyTemp);
			logger.info("Proxified " + href + " to " + proxyTemp);
		}
	}

	private String getProxy(String path) {
		String proxyTemp;
		try {
			proxyTemp = "file:///"+OUT_FOLDER + URLEncoder.encode(path, "UTF-8")+".htm";
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return proxyTemp;
	}
	private void download(String path) {
		try {
			String pathTemp = URLEncoder.encode(path)+".htm";
			File file = new File(OUT_FOLDER,pathTemp);
			File fileOut= new File(OUT_FOLDER);
			if(!fileOut.exists()){
				fileOut.mkdirs();
			}
			logger.info("downloading " + path + " to " + file.getAbsolutePath());
			FileUtils.copyURLToFile(new URL(path), file);
			
			//rewrite urls
			InputStream input=new FileInputStream(file);
			Source source = new Source(input);
			source.fullSequentialParse();
			OutputDocument outputDocument = new OutputDocument(source);
			proxyAnchor(source,outputDocument );
			
			FileUtils.writeStringToFile(file, outputDocument.toString());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
