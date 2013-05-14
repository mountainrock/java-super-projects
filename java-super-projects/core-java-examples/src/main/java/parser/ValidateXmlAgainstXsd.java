package parser;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;

public class ValidateXmlAgainstXsd {

	static final String SCHEMA = "C:/dev/trunk/components/marklogic/schemas/qtpListing.xsd";
	static final String TEST_FOLDER_PATH = "C:/dev/trunk/modules/com.yell.testautomation/datapack/qa/ST5/CRM_FEED/branch_feed/marklogic-feed/";
	static final String TEST_URL_FILE_PATH = "C:/development/database/TEST/TEST_URLs.txt";

	List failed = new ArrayList();
	List passed = new ArrayList();
	Map failedMessages = new HashMap();

	public static void main(String[] args) throws Exception {
		new ValidateXmlAgainstXsd().validateFileUnderFolder();
		// new ValidateXmlAgainstXsd().parseFile(TEST_FOLDER_PATH+ File.separator+"ISRCTN00049084.xml");
		//new ValidateXmlAgainstXsd().validateXSDURL();

	}

	private void validateFile(String path) throws Exception {
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.XML_NS_URI);
		Schema schemaXSD = schemaFactory.newSchema(new File(SCHEMA));
		Validator validator = schemaXSD.newValidator();

		System.out.println("Starting validation for file under : " + path);
		System.out.println("Running validation ..." + path);
		try {
			Document document = parser.parse(new File(path));
			// parse the XML DOM tree againts the stricter XSD schema
			validator.validate(new DOMSource(document));
			passed.add(path);
		} catch (Exception e) {
			e.printStackTrace();
			failed.add(path);
		}
		dumpResult();
	}

	private void validateFileUnderFolder() throws Exception {
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schemaXSD = schemaFactory.newSchema(new File(SCHEMA));
		Validator validator = schemaXSD.newValidator();

		File[] files = new File(TEST_FOLDER_PATH).listFiles(new FileFilter(){

			@Override
			public boolean accept(File file) {
				
				return file.isFile();
			}
			
		});
		System.out.println("Starting validation for files under : " + TEST_FOLDER_PATH);
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			System.out.println("Running validation ..." + file);
			try {
				Document document = parser.parse(file);
				// parse the XML DOM tree againts the stricter XSD schema
				validator.validate(new DOMSource(document));
				passed.add(file.getCanonicalFile());
			} catch (Exception e) {
				e.printStackTrace();
				failed.add(file.getCanonicalFile());
			}
		}
		dumpResult();
	}

	private void validateXSDURL() throws Exception {
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.XML_NS_URI);
		Schema schemaXSD = schemaFactory.newSchema(new File(SCHEMA));
		Validator validator = schemaXSD.newValidator();

		List isrctns = FileUtils.readLines(new File(TEST_URL_FILE_PATH));

		System.out.println("Starting validation for files under : " + TEST_URL_FILE_PATH);
		for (int i = 0; i < isrctns.size(); i++) {
			System.out.println("Running validation ..." + isrctns.get(i));
			try {
				Document document = parser.parse("http://url/schema" + isrctns.get(i) + ".xml");
				// parse the XML DOM tree againts the stricter XSD schema
				validator.validate(new DOMSource(document));
				System.out.println("Passed validation ..." + isrctns.get(i));
				passed.add(isrctns.get(i));
			} catch (Exception e) {
				e.printStackTrace();
				failed.add(isrctns.get(i));
				int count = failedMessages.get(e.getMessage()) != null ? Integer.parseInt(failedMessages.get(e.getMessage()) + "") : 0;
				failedMessages.put(e.getMessage(), ++count + "");
			}
		}
		dumpResult();
	}

	private void dumpResult() {
		System.out.println("failed: " + failed);
		/*for (Iterator iterator = failedMessages.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) failedMessages.get(key);
			System.out.println("failed: " + key + ", count : " + value);
		}*/
		System.out.println("failed count : " + failed.size());
		System.out.println("passed : "+ passed);
		System.out.println("passed count: "+ passed.size());
	}
}

