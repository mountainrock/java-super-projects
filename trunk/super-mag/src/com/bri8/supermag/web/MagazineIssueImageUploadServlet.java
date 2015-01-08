package com.bri8.supermag.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
public class MagazineIssueImageUploadServlet extends HttpServlet {
	private final static Logger logger = Logger.getLogger(MagazineIssueImageUploadServlet.class.getName());
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		MagazineService magazineService = (MagazineService)springContext.getBean("magazineService");
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		logger.info("inside upload servlet blobs : " + blobs + " , params : "+ req.getParameterMap());
		String magazineId = req.getParameter("magazineId");
		Long issueId = Long.parseLong(req.getParameter("issueId"));
		Integer pageNumber=0;
		for (Entry<String, List<BlobKey>> entry : blobs.entrySet()) {
			String key = entry.getKey();
			List<BlobKey> blobKeys = entry.getValue();
			for (BlobKey blobKey : blobKeys) {
				logger.info("Blob key : "+ blobKey.getKeyString() +", magazineId :"+ magazineId+ ", issueId :"+ issueId);
				magazineService.addIssueImageBlobKey(issueId, blobKey.getKeyString(), pageNumber++);;
			}
		}
		
	//TODO: update issue images in new entitye MagazineIssueImage
		
		res.setContentType("application/json");
		
		
	}
}
