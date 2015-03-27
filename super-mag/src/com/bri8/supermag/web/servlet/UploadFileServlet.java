package com.bri8.supermag.web.servlet;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

			resp.setContentType("text/plain");
			resp.getWriter().println("Now see here your file content, that you have uploaded on storage..");

			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter;
			try {
				iter = upload.getItemIterator(req);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					if(item.isFormField()){
						continue;
					}
					String fileName = item.getName();
					String mime = item.getContentType();
					
					GcsFileOptions options = new GcsFileOptions.Builder().acl("public_read").mimeType(mime).build();
					GcsFilename filename = new GcsFilename(BUCKET_NAME, fileName);
					GcsOutputChannel outputChannel = gcsService.createOrReplace(filename, options);

					// Writing the file to input stream
					InputStream is = new BufferedInputStream(item.openStream());
					// Copying InputStream to GcsOutputChannel
					Image oldImage=null;
					try {
						OutputStream newOutputStream = Channels.newOutputStream(outputChannel);
						oldImage = copy(is, newOutputStream);
					} finally {
						outputChannel.close();
						is.close();
					}
					
					GcsFilename fileNameThmb = new GcsFilename(BUCKET_NAME, "thmb-"+ fileName);
					GcsOutputChannel outputChannelThmb = gcsService.createOrReplace(fileNameThmb, options);
					
					Transform resize = ImagesServiceFactory.makeResize(150, 200, true);
					Image newImage = imagesService.applyTransform(resize, oldImage);
					byte[] newImageData = newImage.getImageData();
					try {
						OutputStream newOutputStream = Channels.newOutputStream(outputChannelThmb);
						newOutputStream.write(newImageData);
					} finally {
						outputChannelThmb.close();
						newImage= null;
					}
					
					resp.getWriter().println("File uploading done");

				}
			} catch (Exception e) {
				e.printStackTrace(resp.getWriter());
				System.out.println("Exception::" + e.getMessage());
				e.printStackTrace();
			}
	}

	private Image copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
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