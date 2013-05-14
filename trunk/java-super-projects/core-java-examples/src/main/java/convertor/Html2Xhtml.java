package convertor;

import java.net.URL;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.tidy.Tidy;

public class Html2Xhtml {
	

	private static Log logger = LogFactory.getLog(Html2Xhtml.class);
	private String url;
	private String errOutFileName;

	public Html2Xhtml(String url, String outFileName, String errOutFileName) {
		this.url = url;
		this.errOutFileName = errOutFileName;
	}

	public void convert(OutputStream out) {
		BufferedInputStream in=null;

		Tidy tidy = new Tidy();
		tidy.setXmlOut(true);
		tidy.setQuoteAmpersand(false);
		tidy.setXHTML(true);

		try {
			tidy.setErrout(new PrintWriter(new FileWriter(errOutFileName), true));
			URL u= new URL(url);

			// Create input and output streams
			in = new BufferedInputStream(u.openStream());

			// Convert files
			tidy.parse(in, out);


		} catch (IOException e) {
			logger.error(e,e);
		}finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				logger.error(e,e);
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		/*
		 * Parameters are: URL of HTML file Filename of output file Filename of error file
		 */
		String outputFileName = "src/main/resources/result.xhtml";
		args=new String[]{"http://yahoo.com",outputFileName,"error.out"};
		Html2Xhtml t = new Html2Xhtml(args[0], args[1], args[2]);
		FileOutputStream out = new FileOutputStream(outputFileName);
		t.convert(out);
	}
}