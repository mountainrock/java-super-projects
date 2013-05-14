package velocity;

import java.io.File;
import java.io.FilenameFilter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import utils.VelocityUtil;

public class DemoApplyTagsTemplate
{

	
	public static void main(String[] args) throws Exception
	{
		VelocityContext context = new VelocityContext();

		context.put("date", new Date().toString());

		String inputDir = "C:\\development\\projects\\BIL_trunk\\webapp\\src\\main\\java\\com\\biomedcentral\\application\\imagelibrary\\taglibs";
		String outputDir = "C:\\development\\projects\\BIL_trunk\\webapp\\src\\test\\java\\com\\biomedcentral\\application\\imagelibrary\\taglibs";
		String[] files = new File(inputDir).list(new FilenameFilter()
		{

			public boolean accept(File dir, String name)
			{
				if (name.contains(".java"))
					return true;
				return false;
			}

		});
		String template=("src/main/resources/junit-taglib-generate.vm");
		for (int i = 0; i < files.length; i++)
		{
			String fileName = files[i];
			String className = fileName.substring(0, fileName.indexOf('.'));

			String outFile = className + "Test.java";
			File outputFile = new File(outputDir, outFile);
			if (!outputFile.exists())
			{
				context.put("tagClass", className);
				// System.out.println(fileName);
				StringWriter writer = new StringWriter();
				String result=VelocityUtil.applyTemplate(template, context);
				System.out.println(result);
				//FileUtils.fileWrite(outputDir + "\\" + outFile, writer.toString());
				System.out.println("wrote " + outFile);
			}
		}

	}
	
/**
 * wrote GenerateCheckBoxListTest.java
wrote GenerateComboTagTest.java
wrote GenerateUploadWizardStepTest.java
 */
}
