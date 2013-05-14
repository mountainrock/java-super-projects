/**
 * 
 */
package IO;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.ibm.icu.text.SimpleDateFormat;


public class SyncFolders {

	public static final String SEPERATOR = " >>>> ";

	public static String BACKUP_DSTN_DIR_TO = "C:/junk/UKCTG-dev/backup/";

	private static final String FILE_LIST = "C:/development/miscellaneous/CoreJavaExamples_Feb_2008/src/main/java/IO/syncFolders-ukctg-pilot-dev.txt";

	public static void main(String[] args) throws Exception
	{

		File file = new File(FILE_LIST);
		List<String> paths = FileUtils.readLines(file);
		String backupDIR = BACKUP_DSTN_DIR_TO + new SimpleDateFormat("d_M_yy__h_mm").format(new Date());

		File backupDirObj = new File(backupDIR);
		if (!backupDirObj.exists()) {
			backupDirObj.mkdirs();
			System.out.println("created backup dir : " + backupDIR);
		}
		for (String path : paths) {

			String[] split = path.split(SEPERATOR);
			String src = split[0];
			String dstn = split[1];
			File srcDir = new File(src);
			File destDir = new File(dstn);
			FileFilter filter = getFileFilter();
			FileUtils.copyDirectory(srcDir, destDir, filter);
			System.out.println("copied " + src + "   to   " + dstn);
		}

	}

	private static FileFilter getFileFilter()
	{
		FileFilter filter = new FileFilter() {

			public boolean accept(File pathname)
			{
				if (pathname.getAbsolutePath().endsWith(".scc"))
					return false;
				return true;
			}

		};
		return filter;
	}
}
