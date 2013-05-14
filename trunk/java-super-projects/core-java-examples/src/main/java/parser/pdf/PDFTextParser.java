package parser.pdf;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFTextParser {
	private static Log logger = LogFactory.getLog(PDFTextParser.class.getName());
	private static PDFTextParser _instance = new PDFTextParser();

	private PDFTextParser() {
	}

	public static synchronized PDFTextParser getInstance()
	{
		return _instance;
	}

	/**
	 * Extract text from PDF Document
	 */
	public String pdfToText(String fileName)
	{
		logger.debug("Parsing text from PDF file " + fileName + "....");
		File f = new File(fileName);

		if (!f.isFile()) {
			throw new RuntimeException("File " + fileName + " does not exist.");
		}

		PDDocument pdDoc = null;
		COSDocument cosDoc = null;
		String parsedText;
		try {
			PDFParser parser = new PDFParser(new FileInputStream(f),new RandomAccessBuffer());
			PDFTextStripper pdfStripper = new PDFTextStripper();

			parser.parse();
			cosDoc = parser.getDocument();
			pdDoc = new PDDocument(cosDoc);
			parsedText = pdfStripper.getText(pdDoc);
		} catch (Exception e) {
			throw new RuntimeException("An exception occured in parsing the PDF Document.", e);
		} finally {
			try {
				if (cosDoc != null)
					cosDoc.close();
				if (pdDoc != null)
					pdDoc.close();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}
		logger.debug("Done.");
		return parsedText;
	}

	public static void main(String[] args) {
		String pdfToText = PDFTextParser.getInstance().pdfToText("src/main/resources/sample.pdf");
		System.out.println(pdfToText);
	}
}