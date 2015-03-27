package com.bri8.supermag.web;

import java.io.IOException;
import java.util.Date;
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
import com.bri8.supermag.util.WebConstants;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.labs.repackaged.com.google.common.io.ByteBuffers;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

@SuppressWarnings("serial")
public class MagazineIssueImageUploadServlet extends HttpServlet {
	private final static Logger logger = Logger.getLogger(MagazineIssueImageUploadServlet.class.getName());
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private ImagesService imagesService = ImagesServiceFactory.getImagesService();
	GcsService gcsService = GcsServiceFactory.createGcsService();
	/** Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		MagazineService magazineService = (MagazineService) springContext.getBean("magazineService");
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		Map<String, List<BlobInfo>> blobInfos = blobstoreService.getBlobInfos(req);

		logger.info("inside upload servlet blobs : " + blobs + " , params : " + req.getParameterMap());
		String magazineId = req.getParameter("magazineId");
		Long issueId = Long.parseLong(req.getParameter("issueId"));
		Integer pageNumber = 1;
		for (Entry<String, List<BlobKey>> entry : blobs.entrySet()) {
			String key = entry.getKey();
			List<BlobInfo> blobInfoList = blobInfos.get(key);
			List<BlobKey> blobKeys = entry.getValue();
			BlobInfo blobInfo = blobInfoList.get(0);
			BlobKey blobKey = blobKeys.get(0);
			logger.info(String.format(" Storing magazine issue page( File name : %s, blob key : %s, magazineId :%s, issueId :%s)", blobInfo.getFilename(), blobKey.getKeyString(), magazineId, issueId));

			IssuePage issuePage = new IssuePage();
			issuePage.setIssueId(issueId);
			issuePage.setPageNumber(pageNumber++);
			long time = new Date().getTime();
			String fileNameFull = String.format("%s-%s-%s-%s", magazineId, issueId, time, blobInfo.getFilename());
			issuePage.setFileName(fileNameFull);

			String fileNameThmb = String.format("thmb-%s-%s-%s-%s", magazineId, issueId, time, blobInfo.getFilename());

			issuePage.setFileNameThumbnail(fileNameThmb);

			magazineService.addIssueImageBlobKey(issuePage);
			saveAndResize(blobKey, res, fileNameFull, fileNameThmb);
			
			//cleanup blobstore image
			blobstoreService.delete(blobKey);

		}

		// TODO: update issue images in new entitye MagazineIssueImage

		res.setContentType("application/json");

	}

	public void saveAndResize(BlobKey blobKey, HttpServletResponse res, String fileNameFull, String fileNameThmb) {
		Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
		try {
			byte[] imageData = blobstoreService.fetchData(blobKey, 0, BlobstoreService.MAX_BLOB_FETCH_SIZE-1);
			//save to GCS
			saveToBlobstore(new Blob(imageData), res, fileNameFull);

			//resize and save
			Transform resize = ImagesServiceFactory.makeResize(150, 200);
			Image newImage = imagesService.applyTransform(resize, oldImage);
			byte[] newImageData = newImage.getImageData();
			saveToBlobstore(new Blob(newImageData), res, fileNameThmb);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void saveToBlobstore(Blob imageData, HttpServletResponse res, String name) throws Exception {
		if (null == imageData)
			return;

		GcsFilename filename = new GcsFilename(WebConstants.BUCKETNAME, name);
		GcsFileOptions options = new GcsFileOptions.Builder().mimeType("image/jpg").acl("public-read").build();
		GcsOutputChannel writeChannel = gcsService.createOrReplace(filename, options);
		try {
			// Write data from photo
			writeChannel.write(ByteBuffers.wrap(imageData.getBytes()));
		} finally {
			writeChannel.close();
			res.setStatus(HttpServletResponse.SC_CREATED);
			res.setContentType("text/plain");
		}

		res.getWriter().println("Done writing...");
	}

}
