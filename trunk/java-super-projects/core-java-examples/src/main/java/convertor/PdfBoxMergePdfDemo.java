package convertor;

import java.io.IOException;

import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.util.PDFMergerUtility;

public class PdfBoxMergePdfDemo {
	static String resourcePath = "src/main/resources/";

	public static void main(String[] args) {
		String[] inputs = { resourcePath + "sample.pdf", resourcePath + "sample.pdf" };
		String output = resourcePath + "out.pdf";
		merge(inputs, output);
	}

	private static void merge(String[] inputs, String output) {
		PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		for (String name : inputs)
			pdfMergerUtility.addSource(name);

		// get the output file to write to

		pdfMergerUtility.setDestinationFileName(output);

		// merge

		try {
			pdfMergerUtility.mergeDocuments();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
