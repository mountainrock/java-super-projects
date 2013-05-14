package itext;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

/**
 * 
 */


public class BestPrintPageSize extends AbstractPDFCreator {

	public final Rectangle[][] standardPageSizes = {
			// 595 612 792 842 1008 1190 1684 2384 3370
			{ PageSize.A4, PageSize.LETTER, PageSize.A3, PageSize.A3, PageSize.A2, PageSize.A2, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.A4, PageSize.LETTER, PageSize.A3, PageSize.A3, PageSize.A2, PageSize.A2, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.A4, PageSize.LETTER, PageSize.A3, PageSize.A3, PageSize.A2, PageSize.A2, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.A4, PageSize.LEGAL, PageSize.A3, PageSize.A3, PageSize.A2, PageSize.A2, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.LEGAL, PageSize.LEGAL, PageSize.A3, PageSize.A3, PageSize.A2, PageSize.A2, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.A3, PageSize.A3, PageSize.A3, PageSize.A3, PageSize.A2, PageSize.A2, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.A2, PageSize.A2, PageSize.A2, PageSize.A2, PageSize.A2, PageSize.A1, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.A1, PageSize.A1, PageSize.A1, PageSize.A1, PageSize.A1, PageSize.A1, PageSize.A1, PageSize.A0, PageSize.A0 },
			{ PageSize.A0, PageSize.A0, PageSize.A0, PageSize.A0, PageSize.A0, PageSize.A0, PageSize.A0, PageSize.A0, PageSize.A0 },

	};

	private static Logger logger = Logger.getLogger(BestPrintPageSize.class);

	
	public static void main(String[] args) throws FileNotFoundException, DocumentException
	{

		String path = new StringBuffer("D:").append(File.separator).append("TestBestPrintPageSize.pdf").toString();
		BestPrintPageSize bestPrintPageSize = new BestPrintPageSize();
		Document doc = bestPrintPageSize.createDocument(path);

		bestPrintPageSize.addNewLine(doc);
		doc.add(AbstractPDFCreator.getParagraphForText("Sample Text", Element.ALIGN_CENTER, Font.BOLD, 15.0F));

		StringBuffer junkText = new StringBuffer();
		int length = 1000;
		for (int i = 0; i < length; i++) {
			junkText.append(Math.random());
		}

		doc.add(AbstractPDFCreator.getParagraphForText(junkText.toString(), Element.ALIGN_UNDEFINED, Font.COURIER, 15.0F));

		final int INPUT_WIDTH = 1008;
		final int INPUT_HEIGHT = 612;
		final Rectangle bestFitPageSize = bestPrintPageSize.getBestFitPageSize(INPUT_WIDTH, INPUT_HEIGHT);

		doc.setPageSize(bestFitPageSize);

		System.out.println("Done with PDF creation.." + path);
		doc.close();

	}

	/**
	 * Expects a standard page sizes array . The best page size is determined based on the width first and then on the height.
	 * @param width
	 * @param height
	 * @return
	 */
	private Rectangle getBestFitPageSize(int width, int height)
	{
		float weightWidth = -1;
		float weightHeight = -1;
		int columnIndex = -1;
		logger.info("Getting best fit page size for inputs (width=" + width + ", height=" + height + ")");

		// default
		Rectangle bestFitPageSize = null;
		// get the column having the least weight -widthwise
		for (int i = 0; i < standardPageSizes.length; i++) {
			Rectangle[] row = standardPageSizes[i];
			for (int j = 0; j < row.length; j++) {
				Rectangle rectangle = row[j];
				logger.info("checking size (width = " + rectangle.RIGHT + " , height = " + rectangle.TOP + ")");

				final float diffWidth = (rectangle.RIGHT - width);
				if ((weightWidth == -1 && diffWidth >= 0) || (diffWidth < weightWidth && diffWidth >= 0)) {
					weightWidth = diffWidth;
					columnIndex = j;
				}

			}
		}

		logger.info("Possible page size found in column : " + columnIndex);

		// get the right page size in this column..having least weight-height

		int rowIndex = -1;
		for (int k = 0; k < standardPageSizes.length; k++) {
			final float diffHeight = (standardPageSizes[k][columnIndex].TOP) - height;
			if ((diffHeight >= 0 && weightHeight == -1) || (diffHeight < weightHeight && diffHeight >= 0)) {
				weightHeight = diffHeight;
				rowIndex = k;
			}

		}

		if (rowIndex == -1 || columnIndex == -1) {
			logger.info("no best fit page size found ...defaulting to A4");
			bestFitPageSize = PageSize.A4;
		} else {
			bestFitPageSize = standardPageSizes[rowIndex][columnIndex];
		}

		logger.info("Best fit page size was " + (rowIndex == -1 || columnIndex == -1 ? "not found" : "found at index [" + rowIndex + "," + columnIndex + "]") + "(width=" + bestFitPageSize.RIGHT
				+ ", height=" + bestFitPageSize.TOP + ")");

		return bestFitPageSize;
	}

}
