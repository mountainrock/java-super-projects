package convertor;

import java.io.*;

import org.apache.poi.hssf.dev.HSSF;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class WordToPDF {
	public static void main(String arg[]) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/result.pdf"));
		document.open();
		
		document.add(new Paragraph("Test"));
		document.close();
	}
}