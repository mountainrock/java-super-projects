package parser.pdf;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Pdf2ImageUtil {

	/**
	 * Extract image from PDF Document
	 */
	public List<BufferedImage> pdfToImage(InputStream in) {
		logger.debug("Parsing image from PDF file ....");
		List<BufferedImage> result=new ArrayList<BufferedImage>();
		try {
			PDDocument document = PDDocument.load(in);
			List<PDPage> pages = document.getDocumentCatalog().getAllPages();

			// for each page
			for (int i = 0; i < pages.size(); i++) {
				PDPage singlePage = pages.get(i);
				BufferedImage buffImage = singlePage.convertToImage();
				result.add(buffImage);
			}

		} catch (Exception e) {
			throw new RuntimeException("An exception occured in parsing the PDF Document.", e);
		} finally {
			
		}
		return result;
	}

	private Pdf2ImageUtil() {
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static synchronized Pdf2ImageUtil getInstance() {
		return _instance;
	}

	private static Log logger = LogFactory.getLog(Pdf2ImageUtil.class.getName());
	private static Pdf2ImageUtil _instance = new Pdf2ImageUtil();
	
}