package itext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.field.RtfTOCEntry;
import com.lowagie.text.rtf.field.RtfTableOfContents;
import com.lowagie.text.rtf.table.RtfBorderGroup;
import com.lowagie.text.rtf.table.RtfCell;

/**
 * Generates an RTF document with a Table of Contents and a Table with special Cellborders.
 * 
 * @author Mark Hall
 */
public class RtfTOCandCellborders {

	static String filePath = "d:\\toc.rtf";
	static Document document;

	/**
	 * Creates an RTF document with a TOC and Table with special Cellborders.
	 * 
	 * @param args no arguments needed
	 */
	public static void main(String[] args)
	{

		try {
			System.out.println("Table of contents and Cell borders");
			document = openRTFDocument(null);// default

			Paragraph para = new Paragraph();
			para.add(new RtfTableOfContents("RIGHT CLICK AND HERE AND SELECT \"UPDATE FIELD\" TO UPDATE."));
			document.add(para);

			Paragraph par = new Paragraph("This is some sample content.");
			Chapter chap1 = new Chapter("Chapter 1", 1);
			chap1.add(par);
			Chapter chap2 = new Chapter("Chapter 2", 2);
			chap2.add(par);
			document.add(chap1);
			document.add(chap2);

			for (int i = 0; i < 300; i++) {
				if (i == 158) {
					document.add(new RtfTOCEntry("This is line 158."));
				}
				document.add(new Paragraph("Line " + i));
			}

			document.add(new RtfTOCEntry("Cell border demonstration"));

			Table table = new Table(9);

			RtfCell cellNoBorder = new RtfCell("No border");
			cellNoBorder.setBorders(new RtfBorderGroup());

			table.addCell(cellNoBorder);
			table.addCell(cellNoBorder);
			table.addCell(cellNoBorder);

			document.add(table);
			closeRTFDocument();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		} finally {
			closeRTFDocument();
		}

	}

	private static void closeRTFDocument()
	{
		document.close();
	}

	public static Document openRTFDocument(String filePath2) throws FileNotFoundException
	{
		Document document = new Document();
		if (!(filePath2 == null || filePath2.length() == 0)) {
			filePath = filePath2;
		}
		RtfWriter2 writer2 = RtfWriter2.getInstance(document, new FileOutputStream(filePath));

		writer2.setAutogenerateTOCEntries(true);

		document.open();
		return document;
	}

	public static Table createTableForTokens(List list, String seperatorRegex, String[] headers) throws BadElementException
	{
		Table table = new Table(headers.length);
		table.setAlignment(Table.ALIGN_JUSTIFIED);

		for (int i = 0; i < headers.length; i++) {
			String header = headers[i];
			RtfCell headerCell = new RtfCell(header);
			headerCell.setHorizontalAlignment(RtfCell.ALIGN_JUSTIFIED_ALL);
			headerCell.setHeader(true);
			table.addCell(header);
		}
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String str = (String) iter.next();
			String[] tokens = str.split(seperatorRegex, -1);

			for (int i = 0; i < tokens.length; i++) {
				String string = tokens[i];
				RtfCell cellNoBorder = new RtfCell(string);
				cellNoBorder.setBorders(new RtfBorderGroup());
				cellNoBorder.setRowspan(1);
				table.addCell(cellNoBorder);
			}

		}
		return table;
	}
}