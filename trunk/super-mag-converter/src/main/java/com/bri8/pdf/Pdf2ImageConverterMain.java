package com.bri8.pdf;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class Pdf2ImageConverterMain {

	public static void main(String[] args) {

	}

	protected void doIt() throws Exception{
		HttpClient httpClient = getClient("",8080);
		PostMethod postMethod = new PostMethod("/UPLOADPATH");
		int i = httpClient.executeMethod(postMethod);
		Header uploadUrl = postMethod.getResponseHeader("blobUrlPath");
		
		
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
