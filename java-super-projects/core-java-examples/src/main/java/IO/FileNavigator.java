package IO;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNavigator

{

	String tab = "\t+";

	private String workingDir;

	private String backupDir;

	private String outputDir;

	public FileNavigator() {

	}

	public FileNavigator(String workingDir, String backupDir, String outputDir)

	{

		this.workingDir = workingDir;

		this.backupDir = backupDir;

		this.outputDir = outputDir;

	}

	public static void main(String[] args)
	{

		FileNavigator td = new FileNavigator("D:\\SampleApps\\JavaPrograms\\src\\IO", "c:\\TEST_BACKUP", "d:\\test\\FCJMaintEJB.txt");

		td.visitAllDirsAndFiles(new File(td.workingDir));// traverse the files and process them

	}

	// Process all files and directories under dir

	public void visitAllDirsAndFiles(File dir)
	{

		if (dir.isDirectory()) {

			System.out.println("You are inside directory :" + dir.getName());

			String[] children = dir.list();

			tab = "\t";

			for (int i = 0; i < children.length; i++) {

				File child = new File(dir, children[i]);

				if (!child.isDirectory())

					System.out.println(tab + "File:" + tab + children[i]);

				else

					System.out.println("Dir:" + tab + children[i]);

				visitAllDirsAndFiles(child);

			}

		}

		else

			process(dir);

	}

	public void process(File dir)
	{

		StringBuffer buf = IOUtility.readFile(dir);

		String fileContent = buf.toString();

		int fin = fileContent.indexOf(" START OF LOG HISTORY");

		String identifier = "\r\n/*@@@@@@@@@---------->" + dir.getAbsolutePath() + "*/\r\n";

		if (fin > 0) {

			// fileContent =identifier+ fileContent.substring(0,fin)+ fileContent.substring(fin2, fileContent.length()-1);

		}

		else {

			fileContent = fileContent + identifier;

		}

		fileContent = fileContent + "\r\n/*<-----------$$$$$$$$*/\r\n";

		IOUtility.writeToFile(new StringBuffer(fileContent), new File("c:\\temp\\FileNavigatorProgram.txt"), false);

		// ----------------------------------------------------------------------------------------------------

	}

	public StringBuffer matchAndInsert(StringBuffer buffer, String strPattern, String strInsert)
	{

		Pattern p = Pattern.compile(strPattern);

		Matcher m = p.matcher(buffer);

		if (m.find()) {

			System.out.println("Inserting for pattern ::" + strPattern + " ::contents:::" + strInsert);

			buffer.insert(m.end(), strInsert);

		}

		return buffer;

	}
}