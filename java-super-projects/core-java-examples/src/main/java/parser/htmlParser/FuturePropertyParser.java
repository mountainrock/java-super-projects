package parser.htmlParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.DateUtil;
import utils.WebDriverUtil;

public class FuturePropertyParser {

	private static final String URL = "http://www.futurepropertyauctions.co.uk/catalogue_text.asp";
	static String rowXpath = "/html/body/table[2]/tbody/tr/td/table/tbody/tr/td[3]/table[3]/tbody/tr[2]/td/table/tbody/tr[3]/td[2]/div/table/tbody/tr";
	static String filterAddress = "glasgow";

	public static void main(String[] args) throws Exception {
		WebDriverUtil.getWebDriver();
		WebDriverUtil.defaultGet(URL);
		// WebDriverUtil.defaultGet("H:/sandeep/gae-code/java-super-projects/java-super-projects/core-java-examples/src/main/resources/catalogue_text.asp.htm");

		List<WebElement> rows = WebDriverUtil.getWebDriver().findElements(By.xpath(rowXpath));
		List<PropertyDetail> list = new ArrayList<PropertyDetail>();
		StringBuffer sb = new StringBuffer();
		for (WebElement row : rows) {
			String rowText = row.getText();
			Scanner sc2 = new Scanner(rowText);
			PropertyDetail p = new PropertyDetail();
			if (sc2.hasNextLine())
				p.lot = sc2.nextLine();
			if (sc2.hasNextLine())
				p.address = sc2.nextLine();
			if (sc2.hasNextLine())
				p.description = sc2.nextLine();
			if (sc2.hasNextLine())
				p.price = sc2.nextLine();
			if (p.address.toLowerCase().contains(filterAddress)) {
				list.add(p);
				System.out.println(" Added : " + p);
				sb.append(p).append("\r\n");
			}else{
				System.out.println(" Skipped: " + p);
			}
		}
		System.out.println(list);
		FileUtils.writeStringToFile(new File("src/main/resources/future-catalogue-"+filterAddress+"-" + DateUtil.getDateAsFileName() + ".txt"), sb.toString());

	}

}

class PropertyDetail {
	String lot;
	String address;
	String description;
	String price;

	@Override
	public String toString() {
		return String.format(" %s \t:: Lot %s  %s \t - %s", address, lot, description, price);
	}
}