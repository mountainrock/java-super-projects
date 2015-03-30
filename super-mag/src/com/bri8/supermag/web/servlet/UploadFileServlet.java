package com.bri8.supermag.web.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
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
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private StorageService storage = new StorageService();
	private static int BUFFER_SIZE = 1024 * 1024 * 10;
	public static final String BUCKET_NAME = WebConstants.BUCKETNAME;
	private final GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
	private ImagesService imagesService = ImagesServiceFactory.getImagesService();
	private final static Logger log = Logger.getLogger(UploadFileServlet.class.getName());
	private MagazineService magazineService;

	@Override
	public void init() throws ServletException {
		ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		magazineService = (MagazineService) springContext.getBean("magazineService");
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {

		response.setContentType("text/plain");

		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iterator;
		String magazineId = null;
		String issueId = null;
		try {
			iterator = upload.getItemIterator(req);
			int pageNumber = 1;
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					
					if ("magazineId".equals(fieldName)) {
						magazineId = IOUtils.toString(stream);
						log.warning(String.format("Form field: %s = %s ",fieldName, magazineId));
					} else if ("issueId".equals(fieldName)) {
						issueId = IOUtils.toString(stream);
						log.warning(String.format("Form field: %s = %s ",fieldName, issueId));
					}
				} else {
					log.info("Saving images ");
					String fileName = String.format("%s-%s-%s-%s", magazineId, issueId, new Date().getTime(), item.getName());
					String mime = item.getContentType();

					GcsFileOptions options = new GcsFileOptions.Builder().acl("public_read").mimeType(mime).build();
					GcsFilename filename = new GcsFilename(BUCKET_NAME, fileName);
					GcsOutputChannel outputChannel = gcsService.createOrReplace(filename, options);

					// Writing the file to input stream
					InputStream is = new BufferedInputStream(item.openStream());
					// Copying InputStream to GcsOutputChannel
					Image oldImage = null;
					try {
						log.info("Saving image : " + filename);
						OutputStream newOutputStream = Channels.newOutputStream(outputChannel);
						oldImage = copy(is, newOutputStream);
					} finally {
						outputChannel.close();
						is.close();
					}

					String fileNameThmbStr = String.format("%s-%s-%s-thmb-%s", magazineId, issueId, new Date().getTime(), item.getName());
					GcsFilename fileNameThmb = new GcsFilename(BUCKET_NAME, fileNameThmbStr);
					GcsOutputChannel outputChannelThmb = gcsService.createOrReplace(fileNameThmb, options);

					Transform resize = ImagesServiceFactory.makeResize(150, 200, true);
					Image newImage = imagesService.applyTransform(resize, oldImage);
					byte[] newImageData = newImage.getImageData();
					try {
						log.info("Saving thmb image : " + fileNameThmbStr);
						OutputStream newOutputStream = Channels.newOutputStream(outputChannelThmb);
						newOutputStream.write(newImageData);
					} finally {
						outputChannelThmb.close();
						newImage = null;
					}

					log.info(String.format(" Storing magazine issue page( File name : %s, magazineId :%s, issueId :%s)", fileName, magazineId, issueId));

					IssuePage issuePage = new IssuePage();
					issuePage.setIssueId(Long.parseLong(issueId));
					issuePage.setPageNumber(pageNumber++);
					issuePage.setFileName(fileName);
					issuePage.setUpdatedDate(new Date());
					issuePage.setFileNameThumbnail(fileNameThmbStr);
					magazineService.addIssueImageBlobKey(issuePage);
					// resp.getWriter().println("File uploading done");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		response.sendRedirect(String.format("/magazine/showUploadIssue/%s/%s", magazineId, issueId));
	}

	private Image copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytesRead = input.read(buffer);
		while (bytesRead != -1) {
			output.write(buffer, 0, bytesRead);
			baos.write(buffer, 0, bytesRead);
			bytesRead = input.read(buffer);
		}
		Image image = ImagesServiceFactory.makeImage(baos.toByteArray());
		return image;
	}
}