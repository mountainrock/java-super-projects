package com.san.jshoutbox.model.ebay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.bri8.supermag.util.SimpleVelocityUtil;

import junit.framework.TestCase;

public class EbayUrlTest extends TestCase {

	private static Log logger = LogFactory.getLog(EbayUrlTest.class);

	@Test
	public void testToFullUrl() {
		String ebay = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=${appname}&GLOBAL-ID=EBAY-US&keywords=${keywords}&paginationInput.entriesPerPage=30";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("keywords", "blah");
		variables.put("appname", "blahappname");
		String result = SimpleVelocityUtil.getInstance().evaluate(ebay, variables);
		logger.info(result);
	}

}
