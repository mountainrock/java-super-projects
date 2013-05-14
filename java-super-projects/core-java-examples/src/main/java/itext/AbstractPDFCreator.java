/**
 * 
 */
package itext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;


public class AbstractPDFCreator {

	public void addNewLine(Document doc) throws DocumentException
	{
		doc.add(newLine());
	}

	/**
	 * 
	 * @return
	 */
	public Paragraph newLine()
	{
		return new Paragraph(Chunk.NEWLINE);
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	public Document createDocument(String path) throws FileNotFoundException, DocumentException
	{
		FileOutputStream fileOutputStream = new FileOutputStream(path);
		// step 1: creation of a document-object
		Document document = new Document();
		// step 2:
		// we create a writer that listens to the document

		PdfWriter.getInstance(document, fileOutputStream);

		// step 3: we open the document
		document.open();
		return document;
	}

	protected static Paragraph getParagraphForText(String textContent, int alignment, int fontStyle, float size)
	{
		Paragraph newParagraph = new Paragraph();
		newParagraph.setAlignment(alignment);
		Chunk chunk = new Chunk(textContent);
		Font font = new Font();
		font.setStyle(fontStyle);
		font.setSize(size);
		chunk.setFont(font);

		newParagraph.add(chunk);
		return newParagraph;
	}
}
