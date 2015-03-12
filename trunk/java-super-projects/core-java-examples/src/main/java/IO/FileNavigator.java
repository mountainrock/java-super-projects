package IO;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

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

	public static void main(String[] args) {

		FileNavigator td = new FileNavigator("C:\\project\\Jan21_2015\\WebContent\\WEB-INF\\lib", "c:\\TEST_BACKUP", "d:\\test\\FCJMaintEJB.txt");

		td.visitAllDirsAndFiles(new File(td.workingDir));// traverse the files
															// and process them

	}

	// Process all files and directories under dir

	public void visitAllDirsAndFiles(File dirOrFile) {

		if (dirOrFile.isDirectory()) {

			System.out.println("You are inside directory :" + dirOrFile.getName());

			String[] children = dirOrFile.list();

			tab = "\t";

			for (int i = 0; i < children.length; i++) {

				File child = new File(dirOrFile, children[i]);

				if (!child.isDirectory()) {
					//System.out.println(children[i]);
				} else {
					System.out.println("Dir:" + tab + children[i]);
				}
				visitAllDirsAndFiles(child);

			}

		}

		else {
			process(dirOrFile);
		}

	}

	public void process(File dirOrFile) {
		String templ="<dependency>\r\n" + 
				"    <groupId>local.dummy</groupId>\r\n" + 
				"    <artifactId>%s</artifactId>\r\n" + 
				"    <version>0.0.1</version>\r\n" + 
				"    <scope>system</scope>\r\n" + 
				"    <systemPath>\\${project.basedir}/lib/%s.jar</systemPath>\r\n" + 
				"</dependency>\r\n" + 
				"\r\n";
		String name = StringUtils.substringBefore(dirOrFile.getName(),".");
		System.out.println(String.format(templ, name,name));

	}

	public StringBuffer matchAndInsert(StringBuffer buffer, String strPattern, String strInsert) {

		Pattern p = Pattern.compile(strPattern);

		Matcher m = p.matcher(buffer);

		if (m.find()) {

			System.out.println("Inserting for pattern ::" + strPattern + " ::contents:::" + strInsert);

			buffer.insert(m.end(), strInsert);

		}

		return buffer;

	}
}