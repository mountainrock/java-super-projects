package com.bri8.supermag.web;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bri8.supermag.service.MagazineService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class MagazineIssueUploadServlet extends HttpServlet {
	private final static Logger logger = Logger.getLogger(MagazineIssueUploadServlet.class.getName());
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		MagazineService magazineService = (MagazineService)springContext.getBean("magazineService");
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		logger.info("inside upload servlet blobs : " + blobs + " , params : "+ req.getParameterMap());
		BlobKey blobKey = blobs.get("issueFile");
		String magazineId = req.getParameter("magazineId");
		String issueId = req.getParameter("issueId");
		magazineService.updateIssuePdfBlobKey(Long.parseLong(issueId), blobKey.getKeyString());
		
		logger.info("Blob key : "+ blobKey.getKeyString() +", magazineId :"+ magazineId+ ", issueId :"+ issueId);
		res.sendRedirect("/magazine/list");
	}
}
