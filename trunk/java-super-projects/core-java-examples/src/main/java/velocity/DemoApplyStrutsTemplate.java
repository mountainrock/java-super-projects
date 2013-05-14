package velocity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import org.apache.velocity.VelocityContext;

import utils.VelocityUtil;

public class DemoApplyStrutsTemplate
{

	
	public static void main(String[] args) throws Exception
	{

		VelocityContext context = new VelocityContext();

		context.put("date", new Date().toString());

		String inputDir = "C:\\development\\projects\\BIL_trunk\\webapp\\src\\main\\java\\com\\biomedcentral\\application\\imagelibrary\\actions";
		String outputDir = "C:\\development\\projects\\BIL_trunk\\webapp\\src\\test\\java\\com\\biomedcentral\\application\\imagelibrary\\actions";
		String[] files = new File(inputDir).list(new FilenameFilter()
		{

			public boolean accept(File dir, String name)
			{
				if (name.contains(".java"))
					return true;
				return false;
			}

		});
		String template="src/main/resources/junit-struts-action-generate.vm";
		for (int i = 0; i < files.length; i++)
		{
			String fileName = files[i];
			String className = fileName.substring(0, fileName.indexOf('.'));

			String outFile = className + "Test.java";
			File outputFile = new File(outputDir, outFile);
			if (!outputFile.exists())
			{
				context.put("actionClass", className);
				// System.out.println(fileName);
				//FileUtils.fileWrite(outputDir + "\\" + outFile, writer.toString());
				String result=VelocityUtil.applyTemplate(template, context);
				System.out.println(result);
				System.out.println("wrote " + outFile);
			}
		}

	}
	/**
- wrote DeleteSlidesetActionTest.java
-wrote DisplayMyLibraryActionTest.java
	 */

}
