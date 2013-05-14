/**
 * 
 */
package itext;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;


public class ListItemTest extends AbstractPDFCreator {

	
	public static void main(String[] args) throws Exception
	{
		ListItemTest listTest = new ListItemTest();
		String path = "d://ListItemTest.pdf";
		listTest.createPDF(path);

	}

	private void createPDF(String path) throws DocumentException, FileNotFoundException
	{
		Document document = createDocument(path);
		// step 4:

		document.newPage();

		document.add(newLine());
		document.add(newLine());
		document.add(bullettedListTitle("Implications"));

		List listOfItems = bulletedList(document);

		// listOfItems.setIndentationLeft(com.lowagie.text.List.ALIGN_CENTER);
		Map aligmentTypes = new HashMap();
		// aligmentTypes.put(new Integer(ListItem.ALIGN_JUSTIFIED),"ListItem.ALIGN_JUSTIFIED");
		aligmentTypes.put(new Integer(ListItem.ALIGN_CENTER), "ListItem.ALIGN_CENTER");
		/*
		 * aligmentTypes.put(new Integer(ListItem.ALIGN_JUSTIFIED_ALL),"ListItem.ALIGN_JUSTIFIED_ALL"); aligmentTypes.put(new Integer(ListItem.ALIGN_MIDDLE),"ListItem.ALIGN_MIDDLE");
		 * aligmentTypes.put(new Integer(ListItem.ALIGN_TOP),"ListItem.ALIGN_TOP"); aligmentTypes.put(new Integer(ListItem.ALIGN_RIGHT),"ListItem.ALIGN_RIGHT");
		 */

		for (int i = 0; i < 100; i++) {
			ListItem listItem1 = new ListItem();
			listItem1.setSpacingBefore(10);
			listItem1
					.add("Testing  fghfghfghfghfghfg hthefghfghfghfghfghfghthe fghfghfghfghfghfg hthefghfghfg hfghfghfghthefghfghfghfghfghfghthefghfghfghfghfghfghthefghfghfghfghfghfghthefghfghfghfghfghfghthefghfghfghfghfghfghthe : "
							+ Math.random());
			// listItem1.setAlignment(ListItem.ALIGN_CENTER);

			listOfItems.add(listItem1);
		}
		/*
		 * for (Iterator iter = aligmentTypes.keySet().iterator() ; iter.hasNext();) { Integer alignTypeCode = (Integer) iter.next(); String typeLabel = aligmentTypes.get(alignTypeCode)+""; ListItem
		 * listItem1 = new ListItem(); listItem1.add("Testing the : "+ Math.random() + typeLabel); listItem1.setAlignment(alignTypeCode.intValue());
		 * 
		 * listOfItems.add(listItem1);
		 * 
		 * }
		 */
		document.add(listOfItems);
		// step 5: we close the document
		document.close();
		System.out.println("created document at " + path);
	}

	protected static com.lowagie.text.List bulletedList(Document document)
	{
		boolean isNumberedList = false;
		int indentationOfListSymbol = 10;
		com.lowagie.text.List list = new com.lowagie.text.List(isNumberedList, indentationOfListSymbol);

		float indentation = (float) (0.25 * document.getPageSize().RIGHT);
		list.setIndentationLeft(indentation);

		float indentationRight = (float) (0.1 * document.getPageSize().RIGHT);
		list.setIndentationRight(indentationRight);

		String unicodeForRoundBullet = "\u2022";
		Chunk listSymbol = new Chunk(unicodeForRoundBullet);
		Font symbolFont = new Font();
		symbolFont.setSize(14.0F);
		symbolFont.setStyle(Font.BOLD);
		listSymbol.setFont(symbolFont);

		list.setListSymbol(listSymbol);
		return list;
	}

	Paragraph bullettedListTitle(String textContent)
	{
		Paragraph newParagraph = new Paragraph();
		newParagraph.setAlignment(Element.ALIGN_CENTER);

		float riseHeight = 1.0F;
		Chunk title = new Chunk(textContent);
		title.setTextRise(riseHeight);

		Font font = new Font();
		font.setStyle(Font.BOLD);
		font.setSize(15.0F);
		title.setFont(font);

		newParagraph.add(title);

		return newParagraph;
	}

}
