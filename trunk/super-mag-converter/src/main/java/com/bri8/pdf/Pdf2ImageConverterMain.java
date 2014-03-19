package com.bri8.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Pdf2ImageConverterMain {

	private static Log logger = LogFactory.getLog(Pdf2ImageConverterMain.class);

	private static final int PORT = 8888;
	private static final String HOST = "localhost";
	private static final String DOWNLOAD_DIR = "src/test/resources/download";

	public static void main(String[] args) throws Exception {
		Pdf2ImageConverterMain converter = new Pdf2ImageConverterMain();
		converter.doIt();
	}

	protected void doIt() throws Exception {
		HttpClient httpClient = getClient(HOST, PORT);

		// 1. get magazine issued ids requiring pdf to image
		String[] magIssuePaths = listIssuesForPdf2Image(httpClient);

		// 2. download pdf
		downloadPdfs(httpClient, magIssuePaths);

		// TODO: 3. convert pdf to image
		for (int i = 0; i < magIssuePaths.length; i++) {
			String issuePath = magIssuePaths[i];
			String fileName = getFileName(issuePath);
			InputStream in = new FileInputStream(new File(fileName));
			logger.info("converting pdf to images ");
			List<BufferedImage> pdfToImages = JpedalPdf2ImageUtil.getInstance().pdfToImage(in);
			for (int j = 0; j < pdfToImages.size(); j++) {
				logger.info("writing image " + j);
				BufferedImage bufferedImage = pdfToImages.get(j);
				ImageIO.write(bufferedImage, "png", new FileOutputStream(new File(DOWNLOAD_DIR + File.separator + issuePath.replace('/', '_') + "_" + j + ".png")));
			}
		}

		// 4. get blobstore urls as many as pdf pager
		String[] uploadUrlsArray = getBlobstoreUrls(httpClient, 2);
		for (int j = 0; j < uploadUrlsArray.length; j++) {
			logger.info(" blobstore url :: " + uploadUrlsArray[j]);
		}

		// 5. TODO: upload image to URLs provided. send magazine and issue Id and page number

	}

	/**
	 * download url [host]/magazine/showIssuePdf/{magazineId}/{issueId}
	 */
	private void downloadPdfs(HttpClient httpClient, String[] magIssuePaths) throws IOException, HttpException, FileNotFoundException {
		for (int i = 0; i < magIssuePaths.length; i++) {
			String issuePath = magIssuePaths[i];
			String path = "/magazine/showIssuePdf/" + issuePath;
			GetMethod listIssuesForPdf2Imag = new GetMethod(path);
			int r = httpClient.executeMethod(listIssuesForPdf2Imag);
			InputStream pdfInput = listIssuesForPdf2Imag.getResponseBodyAsStream();
			String fileName = getFileName(issuePath);
			IOUtils.copy(pdfInput, new FileOutputStream(new File(fileName)));
			logger.info("Fetched magazine\\issue at : " + path + " , fileName :" + fileName);
		}
	}

	private String getFileName(String issuePath) {
		String fileName = DOWNLOAD_DIR + File.separator + issuePath.replace('/', '_') + ".pdf";
		return fileName;
	}

	private String[] listIssuesForPdf2Image(HttpClient httpClient) throws IOException, HttpException {
		GetMethod listIssuesForPdf2Imag = new GetMethod("/magazine/listIssuesForPdf2Image");
		int i = httpClient.executeMethod(listIssuesForPdf2Imag);
		String resultList = listIssuesForPdf2Imag.getResponseBodyAsString();
		logger.info("Fetched magazine\\issue : \r\n" + resultList);
		String[] magazineIssues = StringUtils.split(resultList, "\r\n");
		return magazineIssues;
	}

	private String[] getBlobstoreUrls(HttpClient httpClient, int numberOfUrls) throws IOException, HttpException {
		GetMethod blobstoreUrlsGetRequest = new GetMethod("/magazine/getIssueImageUploadUrl");
		blobstoreUrlsGetRequest.setQueryString(new NameValuePair[] { new NameValuePair("numberOfUrls", numberOfUrls + "") });

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
