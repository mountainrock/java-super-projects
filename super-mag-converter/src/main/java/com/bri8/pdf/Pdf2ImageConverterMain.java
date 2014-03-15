package com.bri8.pdf;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Pdf2ImageConverterMain {

	private static Log logger = LogFactory.getLog(Pdf2ImageConverterMain.class);

	public static void main(String[] args) throws Exception {
		Pdf2ImageConverterMain converter = new Pdf2ImageConverterMain();
		converter.doIt();
	}

	protected void doIt() throws Exception {
		HttpClient httpClient = getClient("localhost", 8888);
		String[] uploadUrlsArray = getBlobstoreUrls(httpClient, 2);
		for (int j = 0; j < uploadUrlsArray.length; j++) {
			logger.info(" blobstore url :: "+uploadUrlsArray[j]);
		}

	}

	private String[] getBlobstoreUrls(HttpClient httpClient, int numberOfUrls) throws IOException, HttpException {
		GetMethod blobstoreUrlsGetRequest = new GetMethod("/magazine/getIssueImageUploadUrl");
		blobstoreUrlsGetRequest.setQueryString(new NameValuePair[] { 
			    new NameValuePair("numberOfUrls", numberOfUrls+"") 
			});
		
		blobstoreUrlsGetRequest.getParams().setIntParameter("numberOfUrls", 5);

		int i = httpClient.executeMethod(blobstoreUrlsGetRequest);
		logger.info(blobstoreUrlsGetRequest.getResponseBodyAsString());
		Header uploadUrls = blobstoreUrlsGetRequest.getResponseHeader("uploadUrls");
		String uploadUrlStrs = uploadUrls.getValue();

		String[] uploadUrlsArray = StringUtils.split(uploadUrlStrs, "<br/>");
		return uploadUrlsArray;
	}

	HttpClient getClient(String host, int port) {
		HttpClient client = new HttpClient();
		HostConfiguration hostConfiguration = new HostConfiguration();
		if (port > 0)
			hostConfiguration.setHost(host, port);
		else
			hostConfiguration.setHost(host);
		client.setHostConfiguration(hostConfiguration);
		return client;
	}
}
