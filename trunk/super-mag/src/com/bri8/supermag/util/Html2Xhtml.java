package com.bri8.supermag.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.Node;
import org.w3c.tidy.Tidy;

public class Html2Xhtml {
	private static Log logger = LogFactory.getLog(Html2Xhtml.class);

	public Map<String, String> convert(String url) {
		BufferedInputStream in = null;
		Map<String, String> result = new HashMap<String, String>();

		Tidy tidy = getTidy();
		OutputStream out = new ByteArrayOutputStream();
		StringWriter error = new StringWriter();

		try {
			tidy.setErrout(new PrintWriter(error, true));
			if (!url.toLowerCase().contains("http:")) {
				url = Constants.HTTP_WITH_SLASH + url;
			}
			URL u = new URL(url);
			in = new BufferedInputStream(u.openStream());
			tidy.parse(in, out);
			result.put("error", error.toString());
			result.put("result", out.toString());
		} catch (Exception e) {
			logger.error(e, e);
			result.put("error", e.toString());
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				logger.error(e, e);
			}
		}

		return result;
	}

	public Map<String, String> convert(InputStream in) {
		Map<String, String> result = new HashMap<String, String>();

		Tidy tidy = getTidy();
		OutputStream out = new ByteArrayOutputStream();
		StringWriter error = new StringWriter();

		try {
			tidy.setErrout(new PrintWriter(error, true));
			in = new BufferedInputStream(in);
			Node node = tidy.parse(in, out);
			result.put("error", error.toString());
			result.put("result", out.toString());
		} catch (Exception e) {
			logger.error(e, e);
			result.put("error", e.toString());
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				logger.error(e, e);
			}
		}

		return result;
	}

	private Tidy getTidy() {
		Tidy tidy = new Tidy();
		tidy.setEscapeCdata(false);// leave cdata untouched
		tidy.setIndentCdata(true);// indent the CData
		tidy.setQuoteAmpersand(false);
		tidy.setXHTML(true);
		tidy.setXmlTags(false);
		return tidy;
	}

	public static void main(String[] args) throws FileNotFoundException {
		/*
		 * Parameters are: URL of HTML file Filename of output file Filename of error file
		 */
		String outputFileName = "src/main/resources/result.xhtml";
		args = new String[] { "http://yahoo.com", outputFileName, "error.out" };
		String url = "http://www.google.co.uk/";
		Html2Xhtml t = new Html2Xhtml();

		Map<String, String> result = t.convert(url);

		System.out.println(result);
	}
}