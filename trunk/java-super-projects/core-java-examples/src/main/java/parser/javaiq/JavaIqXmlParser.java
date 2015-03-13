package parser.javaiq;

import java.io.File;
import java.io.StringBufferInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.io.FileUtils;

import utils.HttpUtil;

public class JavaIqXmlParser {

	static String files[]={
		"javaiq_About.xml",
		"javaiq_Architecture, Patterns, UML.xml",
		"javaiq_Backend technologies.xml",
		"javaiq_Core Java.xml",
		"javaiq_Design patterns.xml",
		"javaiq_Frameworks.xml",
		"javaiq_Frontend.xml",
		"javaiq_Help.xml",
		"javaiq_J2ee.xml",
		"javaiq_Mock interview.xml",
		"javaiq_UML.xml"};
	
	public static void main(String[] args) throws Exception {
		
		for (String filestr : files) {
			filestr = String.format("src/main/resources/javaiq/%s", filestr);
			System.out.println("Processing "+ filestr);
			String file = FileUtils.readFileToString(new File(filestr));
			QuestionBank qb = new JavaIqXmlParser().parsesampleXML(file);
			HttpUtil httpPoster = HttpUtil.getInstance();
			String relativePath="/qa/add";
			String host="jshoutbox.appspot.com";
			
			System.out.println("*"+qb.getGroup());
			int count=0;
			for (Category c : qb.getCategories()) {
				
				System.out.println("***"+c.getName());
				for (QuestionAnswer qa : c.getQas()) {
					System.out.println("\t\t"+qa.getQuestion());
					Map<String, String> params= new HashMap<String, String>();
					params.put("language","Java");
					params.put("category",qb.getGroup());
					params.put("subCategory",c.getName());
					params.put("question",qa.getQuestion().replaceFirst("q:", ""));
					params.put("answer",qa.getAnswer());
					params.put("rating",""+qa.getRating());
					params.put("level",""+qa.getLevel());
					httpPoster.post(host, relativePath, params);
					Thread.sleep(500);//half second delay
					if(count++>2)
						System.exit(0);
				}
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

