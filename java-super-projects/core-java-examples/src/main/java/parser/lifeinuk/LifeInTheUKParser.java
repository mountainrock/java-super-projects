package parser.lifeinuk;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class LifeInTheUKParser {

	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(new File("C:\\sandeep\\open-source\\java-super-projects\\trunk\\java-super-projects\\core-java-examples\\src\\main\\java\\parser\\lifeinuk\\links.txt"));
		StringBuilder extractedText = new StringBuilder();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (StringUtils.isNotEmpty(line)) {
				System.out.println("getting ..." + line);
				String url = StringUtils.substringBefore(line, " = ");
				String title = StringUtils.substringAfter(line, " = ");

				Source source = new Source(new URL(url));
				source.fullSequentialParse();
				Segment contentSegment = getElementsText(source, HTMLElementName.BODY);// entry
				String content = contentSegment.getAllElementsByClass("entry").get(0).toString();
				extractedText = extractedText.append( String.format(">>%s>>\r\n%s\r\n\n",title,content));
				System.out.println(extractedText);
			}
		}
		FileUtils.writeStringToFile(new File("src/main/resources/result-lifeinUK-2.txt"), extractedText.toString());

	}

	private static Segment getElementsText(Source source, String elementName) {
		Element bodyElement = source.getNextElement(0, elementName);
		Segment contentSegment = (bodyElement == null) ? source : bodyElement.getContent();
		return contentSegment;
	}

}
