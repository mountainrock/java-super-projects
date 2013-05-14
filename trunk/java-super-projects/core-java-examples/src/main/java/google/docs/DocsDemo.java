package google.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utils.GoogleDocService;

import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.util.AuthenticationException;

public class DocsDemo {

	private static Log logger = LogFactory.getLog(DocsDemo.class);

	public static void main(String[] args) throws Exception {
		if(args.length<2){
			System.err.println("Usage: DocsDemo infile outfile eg: DocsDemo c:/test.doc c:/out.pdf");
		}
		String outFilepath = args[1];
		FileOutputStream out = new FileOutputStream(new File(outFilepath));
		File file = new File(args[0]);
		FileInputStream in = new FileInputStream(file);
		String outFormat = "pdf";
		String inFormat =  new MimetypesFileTypeMap().getContentType(file);
		new DocsDemo().transform("mysimpleconversion@gmail.com", "sandeepmaloth", file.getName(), in, out, inFormat, outFormat);
	}

	public void transform(String user, String pass, String title, InputStream in, OutputStream out, String inFormat, String downloadFormat) throws Exception {
		GoogleDocService docService = getDocService(user, pass);
		DocumentListEntry dle = docService.upload(in, inFormat, title + " - " + System.currentTimeMillis());
		docService.downloadPresentation(dle.getResourceId(), out, downloadFormat);
	}

	private GoogleDocService getDocService(String user, String pass) throws DocumentListException, AuthenticationException {
		GoogleDocService docService = new GoogleDocService("pdf-services", GoogleDocService.DEFAULT_AUTH_PROTOCOL, GoogleDocService.DEFAULT_AUTH_HOST, GoogleDocService.DEFAULT_PROTOCOL,
				GoogleDocService.DEFAULT_HOST);
		// documentList.turnOnLogging();
		docService.login(user, pass);
		return docService;
	}
}
