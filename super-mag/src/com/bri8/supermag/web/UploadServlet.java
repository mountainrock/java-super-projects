package com.bri8.supermag.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bri8.supermag.model.IssuePage;
import com.bri8.supermag.service.MagazineService;
import com.bri8.supermag.util.WebConstants;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class UploadServlet extends HttpServlet {

	private final static Logger log = Logger.getLogger(MagazineIssueImageUploadServlet.class.getName());

	private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder().initialRetryDelayMillis(10).retryMaxAttempts(10).totalRetryPeriodMillis(15000).build());
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private ImagesService imagesService = ImagesServiceFactory.getImagesService();
	private String bucketName = WebConstants.BUCKETNAME;

	/** Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	private MagazineService magazineService;

	@Override
	public void init() throws ServletException {
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		magazineService = (MagazineService) springContext.getBean("magazineService");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		ServletFileUpload upload;
		FileItemIterator iterator;
		FileItemStream item;
		InputStream stream = null;
		String magazineId = null;
		String issueId = null;
		try {
			upload = new ServletFileUpload();
			res.setContentType("text/plain");

			iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				item = iterator.next();
				stream = item.openStream();

				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					log.warning("Form field: " + fieldName);
					if ("magazineId".equals(fieldName)) {
						magazineId = IOUtils.toString(stream);
					} else if ("issueId".equals(fieldName)) {
						issueId = IOUtils.toString(stream);
					}
				}
			}

			iterator = upload.getItemIterator(req);
			Integer pageNumber = 1;
			while (iterator.hasNext()) {
				item = iterator.next();

				if (item.isFormField()) {
					log.warning("Skipping form field: " + item.getFieldName());
				} else {
					stream = item.openStream();
					log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName());
					String fileName = String.format("%s-%s-%s", magazineId, issueId, item.getName());

					GcsFilename gcsfileName = new GcsFilename(bucketName, fileName);
					GcsFileOptions options = new GcsFileOptions.Builder().acl("public-read").mimeType(item.getContentType()).build();
					GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsfileName, options);
					copy(stream, java.nio.channels.Channels.newOutputStream(outputChannel));
					
					log.info(String.format(" Storing magazine issue page( File name : %s, magazineId :%s, issueId :%s)", fileName, magazineId, issueId));

					IssuePage issuePage = new IssuePage();
					issuePage.setIssueId(Long.parseLong(issueId));
					issuePage.setPageNumber(pageNumber++);
					long time = new Date().getTime();
					String fileNameFull = String.format("%s-%s-%s-%s", magazineId, issueId, time, fileName);
					issuePage.setFileName(fileNameFull);

					String fileNameThmb = String.format("thmb-%s-%s-%s-%s", magazineId, issueId, time, fileName);

					issuePage.setFileNameThumbnail(fileNameThmb);

					magazineService.addIssueImageBlobKey(issuePage);
					//saveAndResize(blobKey, res, fileNameFull, fileNameThmb);

					// res.sendRedirect("/");
				}
			}

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
	
	/*public void saveAndResize(BlobKey blobKey, HttpServletResponse res, String fileNameFull, String fileNameThmb) {
		Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
		try {

			//resize and save
			Transform resize = ImagesServiceFactory.makeResize(200, 300);
			Image newImage = imagesService.applyTransform(resize, oldImage);
			byte[] newImageData = newImage.getImageData();
			saveToBlobstore(newImageData, res, fileNameThmb);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}*/


	private void copy(InputStream input, OutputStream output) throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				output.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
		} finally {
			input.close();
			output.close();
		}
	}

}