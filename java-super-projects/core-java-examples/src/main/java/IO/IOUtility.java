package IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

//import oracle.jdbc.driver.OracleDriver;

public class IOUtility {
	final static public String NEW_LINE = System.getProperty("line.seperator");

	/**
	 * Delete's the directory specified as dirToDelete within the specified folder recursively.
	 * 
	 * @param srcDir
	 * @param dirToDelete
	 * @throws IOException
	 */
	public static void deleteDirectoryRecursively(File srcDir, String dirToDelete) throws IOException
	{
		String[] children = srcDir.list();

		for (int i = 0; i < children.length; i++) {
			File child = new File(children[i]);
			if (child.isDirectory()) {
				if (srcDir.getName().equals(dirToDelete)) {
					deleteDir(child);
				} else
					deleteDirectoryRecursively(child, dirToDelete);
			}

		}

	}

	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns
	// false.
	public static boolean deleteDir(File dir)
	{
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Copies all files under srcDir to dstDir. If dstDir does not exist, it will be created.
	 */

	public static void copyDirectory(File srcDir, File dstDir) throws IOException
	{

		if (srcDir.isDirectory()) {

			if (!dstDir.exists()) {

				dstDir.mkdir();

			}

			String[] children = srcDir.list();

			for (int i = 0; i < children.length; i++) {

				copyDirectory(new File(srcDir, children[i]),

				new File(dstDir, children[i]));

			}

		} else {

			// Copying a File

			try {

				copy(srcDir, dstDir);

			} catch (Exception e)

			{

				e.printStackTrace();

			}

		}

	}

	/**
	 * Copies src file to dst file. If the dst file does not exist, it is created
	 */

	static void copy(File src, File dst) throws IOException
	{

		InputStream in = new FileInputStream(src);

		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out

		byte[] buf = new byte[1024];

		int len;

		while ((len = in.read(buf)) > 0) {

			out.write(buf, 0, len);

		}

		in.close();

		out.close();

	}

	public static String readFile(String fileLocation)

	{
		return readFile(new File(fileLocation)).toString();
	}

	/**
	 * read a file and return the contents
	 * 
	 * @param file
	 * @return
	 */

	public static StringBuffer readFile(File file)

	{

		StringBuffer buffer = new StringBuffer();
		BufferedReader in = null;
		try {

			in = new BufferedReader(new FileReader(file));

			int content;

			while ((content = in.read()) > 0) {

				buffer.append((char) content);// System.out.println(str
				// +buffer.toString());

			}

			in.close();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("Read File :"+dir.getName());

		// System.out.println("The File Contents :"+buffer.toString());

		return buffer;

	}

	/**
	 * API to insert content in a file at a specified index
	 * 
	 * @param contentsToInsert
	 * @param file
	 * @param index
	 */
	public static void insertContentAtIndex(StringBuffer contentsToInsert, File file, int index)
	{
		StringBuffer tempBuffer = new StringBuffer();
		tempBuffer = IOUtility.readFile(file); // read File
		tempBuffer.insert(index, contentsToInsert);
		writeToFile(tempBuffer, file, false);
		System.out.println("Inserted content (" + contentsToInsert.toString() + ") for file :" + file.getName());
	}

	/**
	 * write to a file.
	 */
	public static void writeToFile(String contents, String fileLocation, boolean append)

	{
		writeToFile(new StringBuffer(contents), new File(fileLocation), append);
	}

	/**
	 * write to a file.
	 */
	public static void writeToFile(StringBuffer contents, File file, boolean append)

	{
		BufferedWriter out = null;
		try {

			out = new BufferedWriter(new FileWriter(file, append));
			out.write(contents.toString());
			out.flush();
			out.close();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("Wrote to File :"+dir.getName());

		// System.out.println("The File Contents :"+contents);

	}

	/**
	 * backup
	 * 
	 * @param workingDir
	 * @param backupDir
	 */
	public static void backupDir(File workingDir, File backupDir)
	{

		try {

			IOUtility.copyDirectory(workingDir, backupDir); // do a backup first

			System.out.println("Backed up files at ->" + backupDir.getName());

		} catch (Exception e) {

			System.out.println("Unable to Back up files at ->" + backupDir.getName());

			e.printStackTrace();

		}

	}

	public static String readSystemInputLineByLine() throws IOException
	{
		BufferedReader msgStream = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		StringBuffer buf = new StringBuffer();
		System.out.print("Enter message (\"EOF\" to quit): \n");
		while (!(line = msgStream.readLine()).equalsIgnoreCase("eof")) {
			buf.append(line + NEW_LINE);
		}

		return buf.toString();
	}

	/**
	 * Return's files available in the directory after applying a filter of the provided filetype extension.
	 * 
	 * @param dirPath
	 * @param fileType
	 * @return
	 */
	public static String[] getFilesInDirectory(final String dirPath, final String fileType)
	{
		File dir = new File(dirPath);
		String files[] = null;
		if (dir.isDirectory()) {
			FilenameFilter filenameFilter = new FilenameFilter() {
				public boolean accept(File dir, String name)
				{
					if (!name.endsWith(fileType) || name.endsWith(fileType.toUpperCase())) {
						return false;
					}
					return true;
				}
			};

			files = dir.list(filenameFilter);
		}
		return files;
	}

	public static Properties loadPropertyFile(String fileLocation)
	{
		log(fileLocation);
		Properties properties = new Properties();
		try {
			InputStream is = new FileInputStream(fileLocation);
			properties.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;

	}

	/**
	 * Moves a file from one directory to another
	 * 
	 * @param fileLocation- Absolute location of the file
	 * @param moveDir -The new location for the file to be moved
	 * @return
	 */
	public static boolean moveFile(String fileLocation, String moveDir)
	{
		File fileToMove = new File(fileLocation);
		return fileToMove.renameTo(new File(moveDir, fileToMove.getName()));
	}

	/**
	 * Rename a file
	 * 
	 * @param fileLocation
	 * @param newName
	 * @return
	 */
	public static boolean renameFile(String fileLocation, String newName)
	{
		File fileToRen = new File(fileLocation);
		String absolutePath = fileToRen.getParentFile().getAbsolutePath();
		System.out.println(absolutePath);
		return fileToRen.renameTo(new File(absolutePath, newName));
	}

	private static void log(Object str)
	{
		System.out.println(str);
	}

	public static void main(String[] args) throws Exception
	{

		// String input = IOUtility.readSystemInputLineByLine();
		// System.out.println(input);

		// deleteDirectoryRecursively(new File("D:\\xyz"),"nunnu");
		/*
		 * String fileLocation = "D:\\OSS\\POC_LATEST\\developer-pack\\jmstest\\jndi.properties"; Properties p = loadPropertyFile(fileLocation); log(p.get("java.naming.provider.url"));
		 * 
		 * Object oraDriver = Class.forName("oracle.jdbc.driver.OracleDriver") .newInstance(); // DriverManager.registerDriver((OracleDriver)oraDriver); // import oracle.jdbc.driver.OracleDriver;
		 * log(System.getProperties().get("sql.drivers"));
		 */
		// moveFile("d:\\capture\\Marni- Guru-Len-SatishMarn.JPG", "d:\\");
		renameFile("d:\\Marni- Guru-Len-SatishMarn.JPG", "testing123.jpg");
	}

}