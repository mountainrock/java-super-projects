/**
 * 
 */
package parser.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * The Class POIExcelParserDemo.
 * 
 * @author sandeep.maloth
 */
public class POIExcelParserDemo {

	/** The Constant META_SHEET_NUM. */
	private static final int META_SHEET_NUM = 0;

	/** The Constant META_COLUMN_NUMBER_ARTICLE_ID. */
	private static final String META_COLUMN_NUMBER_ARTICLE_ID = "Article ID";

	/** The Constant META_ROW_NUMBER. */
	private static final int META_ROW_NUMBER = 0;

	/**
	 * The main method.
	 * 
	 * @param args the args
	 * 
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception
	{
		String path = "C:\\development\\database\\BIL\\extractingBMCFigureInfo\\MasterRequestFile_Jul07-Apr08.xls";
		List<String> articleIDList = extractArticleDetails(path, META_ROW_NUMBER, META_SHEET_NUM, META_COLUMN_NUMBER_ARTICLE_ID);

		System.out.println("Number of article id's retreived : " + articleIDList.size());

	}

	/**
	 * Extract article details.
	 * 
	 * @param path the path
	 * @param sheetNo the sheet no
	 * @param metaRowNo the meta row no
	 * @param metaColumnName the meta column name
	 * 
	 * @return the list< string>
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static List<String> extractArticleDetails(String path, int sheetNo, int metaRowNo, String metaColumnName) throws FileNotFoundException, IOException
	{
		List<String> articleIDList = new ArrayList<String>();

		InputStream myxls = new FileInputStream(path);
		HSSFWorkbook wb = new HSSFWorkbook(myxls);
		HSSFSheet sheet = wb.getSheetAt(sheetNo); // first sheet
		HSSFRow row = sheet.getRow(metaRowNo); // row

		int articleIDIndex = 0;

		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			HSSFCell cell = row.getCell((short) i);
			if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING && cell.getRichStringCellValue().getString().equalsIgnoreCase(metaColumnName)) {
				articleIDIndex = i;
				break;
			}
		}
		for (int i = sheet.getFirstRowNum() + (metaRowNo + 1); i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			String value = row.getCell(articleIDIndex).getRichStringCellValue().getString();
			articleIDList.add(value);
			System.out.println("Retreiving [row =" + row.getRowNum() + ", col =" + articleIDIndex + ", value= " + value + " ]");
		}
		return articleIDList;
	}
}
