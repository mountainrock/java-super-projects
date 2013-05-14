/**
 * 
 */
package parser.htmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.TextExtractor;

import org.apache.commons.lang.StringUtils;



public class JerichoInternationalizer {
	final static String SRC_DIR_NAME = "D:\\ProjectRelated\\evaluation\\liferay4.3.2\\webapps\\cmpWeb2\\WEB-INF\\jsp";

	final static String DSTN_DIR_NAME = "D:\\ProjectRelated\\evaluation\\liferay4.3.2\\webapps\\cmpWeb2\\WEB-INF\\jsp\\TEMP";

	final static String FILE_NAME = "articledisplay_view.jsp";

	static Map messageKeyValueHolder = new LinkedHashMap();

	static Map messageValueKeyHolder = new LinkedHashMap();

	static Map elementsToReplace = new LinkedHashMap();

	static int counter = 1;

	
	public static void main(String[] args) throws Exception, IOException
	{
		doProcess(SRC_DIR_NAME, DSTN_DIR_NAME);
		storeMessages(SRC_DIR_NAME);
		System.out.println(messageKeyValueHolder);
		System.out.println(messageValueKeyHolder);

	}

	private static void doProcess(String srcDirName, String dstnDirName) throws IOException, FileNotFoundException, Exception
	{
		File dir = new File(srcDirName);
		String files[] = dir.list();

		for (int i = 0; i < files.length; i++) {
			String fileName = files[i];
			final File fileObject = new File(srcDirName, fileName);
			if (fileObject.isDirectory()) {
				String tempSrcDir = srcDirName + "\\" + fileName;
				final String tempDstnDirName = dstnDirName + "\\" + fileName;
				doProcess(tempSrcDir, tempDstnDirName);
			} else {
				if (!(fileName.toUpperCase().endsWith("JSP") || fileName.toUpperCase().endsWith("HTML"))) {
					System.out.println("Skipping " + fileName);
					continue;
				}

				System.out.println("Parsing .... " + fileName);

				Source source = new Source(new FileInputStream(fileObject));

				source.fullSequentialParse();
				final List childElements = source.getChildElements();
				extractMessages(childElements, source, fileName);

				// The extracted messages will be in elementsToReplace and need
				// to be replaced in the source
				Set entrySet = elementsToReplace.entrySet();
				OutputDocument outputDocument = new OutputDocument(source);
				for (Iterator iter = entrySet.iterator(); iter.hasNext();) {
					Map.Entry entry = (Map.Entry) iter.next();
					Segment content = (Segment) entry.getKey();
					String value = (String) entry.getValue();

					outputDocument.replace(content, value);

				}

				final File dstnDir = new File(dstnDirName);
				if (!dstnDir.exists()) {
					dstnDir.mkdirs();
				}
				final File tempFile = new File(dstnDirName, fileName);
				FileWriter fileWriter = new FileWriter(tempFile);
				outputDocument.writeTo(fileWriter);
				fileWriter.close();

				elementsToReplace.clear();
				System.out.println();
			}
		}
	}

	private static void storeMessages(String dstnDirName) throws FileNotFoundException, IOException
	{
		Set msgsEntrySet = messageKeyValueHolder.entrySet();
		FileWriter fileWriter = null;
		StringBuffer sbuf = new StringBuffer();
		for (Iterator iter = msgsEntrySet.iterator(); iter.hasNext();) {
			final Object object = iter.next();
			System.out.println(object);
			Map.Entry entry = (Map.Entry) object;
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			sbuf.append(key).append("=").append(value).append("\r\n");
		}
		try {
			fileWriter = new FileWriter(new File(dstnDirName + "\\messages_en_US.properties"));
			fileWriter.write(sbuf.toString());
		} finally {
			if (fileWriter != null)
				fileWriter.close();
		}
	}

	private static void extractMessages(List childElements, Source source, String fileName) throws Exception
	{

		for (Iterator iter = childElements.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();

			final List childElements2 = element.getChildElements();

			if (childElements2 != null && childElements2.size() > 0) {
				extractMessages(childElements2, source, fileName);
			} else {
				final TextExtractor textExtractor = element.getTextExtractor();

				String txt = textExtractor.toString();
				String key = "";
				if (txt != null) {
					txt = txt.trim();

					if (txt.length() > 0 && !StringUtils.isNumeric(txt)) {
						System.out.println(txt);
						String ctrString = "";
						if (counter < 10) {
							ctrString = "0000" + counter;
						} else if (counter < 100) {
							ctrString = "000" + counter;
						} else if (counter < 1000) {
							ctrString = "00" + counter;
						} else if (counter < 10000) {
							ctrString = "0" + counter;
						}

						if (!messageValueKeyHolder.containsKey(txt)) {
							key = "MESG" + ctrString;
							messageKeyValueHolder.put(key, txt);
							messageValueKeyHolder.put(txt, key);
							elementsToReplace.put(element.getContent(), "<spring:message code=\"" + key + "\"></spring:message>");

							counter++;
						} else {
							key = (String) messageValueKeyHolder.get(txt);
							elementsToReplace.put(element.getContent(), "<spring:message code=\"" + key + "\"></spring:message>");

						}
					}
				}
			}
		}
	}

}
