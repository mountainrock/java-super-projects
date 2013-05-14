package convertor;

import java.io.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class ImageToPDF {
	public static void main(String arg[]) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/result.pdf"));
		document.open();
		Image image = Image.getInstance("src/main/resources/kishoreKumar.jpg");
		document.add(new Paragraph("Test"));
		document.add(image);
		document.close();
	}
}