package IO;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TraverseDirectory {

	String tab = "-";
	private String workingDir;
	private String backupDir;
	private String outputDir;

	/**
	 * 
	 *
	 */
	public TraverseDirectory() {

	}

	/**
	 * 
	 * @param workingDir
	 * @param backupDir
	 * @param outputDir
	 */
	public TraverseDirectory(String workingDir, String backupDir, String outputDir) {
		this.workingDir = workingDir;
		this.backupDir = backupDir;
		this.outputDir = outputDir;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		TraverseDirectory td = new TraverseDirectory("D:/CSGROOT/CSG/1.0/Application/lib", null, null);
		// String timeStamp = "_BACKUP_"+ new SimpleDateFormat("E_ddMMMyyyy_hh_mm_ss").format(new Date(System.currentTimeMillis()));
		// File backupDir= new File(td.workingDir + timeStamp);
		// IOUtility.backupDir(new File(td.workingDir),backupDir); //backup files first
		td.visitAllDirsAndFiles(new File(td.workingDir));// traverse the files and process them
	}

	// Process all files and directories under dir

	public void visitAllDirsAndFiles(File fileOrDir)
	{
		String fileName = fileOrDir.getName();
		String childString = "";
		if (fileOrDir.isDirectory()) {

			System.out.println("You are inside directory :" + fileName);

			String[] children = fileOrDir.list();

			tab = "\t";

			for (int i = 0; i < children.length; i++) {

				File child = new File(fileOrDir, children[i]);

				if (!child.isDirectory()) {
					childString = childString + " " + children[i];
					// System.out.println("File:"+tab+children[i]);
					System.out.println(childString);
				} else {
					System.out.println("Dir:" + tab + children[i]);
					if (children[i].equals("CVS")) {
						continue;
					}
				}
				visitAllDirsAndFiles(child);

			}

		}

		else // if(dir.getName().charAt()=='M')
		{
			// StringBuffer insertContent = new StringBuffer();
			// insertContent.append( "<%@page errorPage=\"/pages/error.jsp\" isErrorPage=\"false\"%>\r\n\r\n");
			// String type = ( fileName.substring(fileName.lastIndexOf('.'), fileName.length())).toLowerCase();
			// if(type.indexOf("jsp")!=-1)
			// {
			// IOUtility.insertContentAtIndex(insertContent , fileOrDir, 0); //insert at begining=0
			// }
		}
	}

	/**
	 * 
	 * @param file
	 */
	public void process(File file)
	{
	}

	/**
	 * 
	 * @param buffer
	 * @param strTableName
	 * @return
	 */
	public StringBuffer processFileContent(StringBuffer buffer, String strTableName)
	{
		StringBuffer processedBuffer = null;
		return processedBuffer;
	}

	/**
	 * 
	 * @param buffer
	 * @param strPattern
	 * @param strInsert
	 * @return
	 */
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