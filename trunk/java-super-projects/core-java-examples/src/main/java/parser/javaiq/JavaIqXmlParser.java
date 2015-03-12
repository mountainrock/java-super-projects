package parser.javaiq;

import java.io.File;
import java.io.StringBufferInputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.FileUtils;

public class JavaIqXmlParser {

	public static void main(String[] args) throws Exception {
		String file = FileUtils.readFileToString(new File("src/main/resources/javaiq/corejava.xml"));
		QuestionBank qb = new JavaIqXmlParser().parsesampleXML(file);
		System.out.println("*"+qb.getGroup());
		for (Category c : qb.getCategories()) {
			
			System.out.println("***"+c.getName());
			for (QuestionAnswer qa : c.getQas()) {
				System.out.println("\t\t"+qa.getQuestion());
				System.out.println("\t\t"+qa.getAnswer());
				
			}
		}
	}

	protected QuestionBank parsesampleXML(String fullText) throws Exception {
		Digester digester = new Digester();
		digester.addObjectCreate( "questionbank", QuestionBank.class );
		digester.addSetProperties( "questionbank", "group", "group" );
		
		digester.addObjectCreate("questionbank/category", Category.class);
		digester.addSetProperties( "questionbank/category", "name", "name" );
		digester.addSetNext( "questionbank/category", "addCategory" );
		
		digester.addObjectCreate("questionbank/category/qa", QuestionAnswer.class);
		digester.addCallMethod("questionbank/category/qa/question", "setQuestion", 0);
		digester.addCallMethod("questionbank/category/qa/answer", "setAnswer", 0);
		digester.addSetProperties( "questionbank/category/qa", "rating", "level" );
		digester.addSetNext( "questionbank/category/qa", "addQa" );
		
		StringBufferInputStream xml = new StringBufferInputStream(fullText);
		QuestionBank sampleXMLTO = (QuestionBank) digester.parse(xml);
	
		return sampleXMLTO;
	}
	

}

