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

import com.bri8.supermag.model.IssuePage;
import com.bri8.supermag.service.MagazineService;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.labs.repackaged.com.google.common.io.ByteBuffers;

@SuppressWarnings("serial")
public class MagazineIssueImageUploadServlet extends HttpServlet {
	private final static Logger logger = Logger.getLogger(MagazineIssueImageUploadServlet.class.getName());
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private ImagesService imagesService = ImagesServiceFactory.getImagesService();
	FileService fileService = FileServiceFactory.getFileService();


	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		MagazineService magazineService = (MagazineService) springContext.getBean("magazineService");
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		Map<String, List<BlobInfo>> blobInfos = blobstoreService.getBlobInfos(req);
		
		

		logger.info("inside upload servlet blobs : " + blobs + " , params : " + req.getParameterMap());
		String magazineId = req.getParameter("magazineId");
		Long issueId = Long.parseLong(req.getParameter("issueId"));
		Integer pageNumber = 0;
		for (Entry<String, List<BlobKey>> entry : blobs.entrySet()) {
			String key = entry.getKey();
			List<BlobInfo> blobInfoList = blobInfos.get(key);
			List<BlobKey> blobKeys = entry.getValue();
			BlobInfo blobInfo = blobInfoList.get(0);
			BlobKey blobKey = blobKeys.get(0);
			logger.info(String.format(" Storing magazine issue page( File name : %s, blob key : %s, magazineId :%s, issueId :%s)" ,blobInfo.getFilename(), blobKey.getKeyString(), magazineId, issueId));
			
			IssuePage issuePage= new IssuePage();
			issuePage.setIssueId(issueId);
			issuePage.setBlobKey(blobKey.getKeyString());
			issuePage.setPageNumber(pageNumber++);
			issuePage.setFileName(blobInfo.getFilename());
			
			String blobKeyThumbnail= resize(blobKey);
			issuePage.setBlobKeyThumbnail(blobKeyThumbnail);
			
			magazineService.addIssueImageBlobKey(issuePage);

		}

		// TODO: update issue images in new entitye MagazineIssueImage

		res.setContentType("application/json");

	}

	
	public String resize(BlobKey blobKey) {
		Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
        Transform resize = ImagesServiceFactory.makeResize(200, 300);

        Image newImage = imagesService.applyTransform(resize, oldImage);

        byte[] newImageData = newImage.getImageData();
        try {
			return saveToBlobstore(new Blob(newImageData));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	public String saveToBlobstore(Blob imageData) throws Exception {
		if (null == imageData)
			return null;

		AppEngineFile file = fileService.createNewBlobFile("image/png");
		boolean lock = true;
		FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);

		writeChannel.write(ByteBuffers.wrap(imageData.getBytes()));

		writeChannel.closeFinally();
		return fileService.getBlobKey(file).getKeyString();
	}
}