package IO;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import utils.Constants;

/**
 * Moves bulk folders of a name to temp folder(recycle bin?).
 */
public class BulkDeleteFolder {
	int count;
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Please enter the following : [Root Folder ] [Directory to delete] \r\n\t eg: java BulkDeleteFolder c:\\project .svn");
			System.exit(0);
		}
		String dir = args[0];
		String removeFolder = args[1];
		new BulkDeleteFolder().process(new File(dir), removeFolder);
	}

	private void process(File dir, String removeFolder) {
		doIt(dir, removeFolder);
		System.out.println("Moved : "+ count);
		
	}

	public void doIt(File dir, final String removeFolder) {

		if (dir.isDirectory()) {
			String[] children = dir.list(new FilenameFilter(){
				public boolean accept(File dir, String name) {
					return dir.isDirectory();
				}
			});

			for (int i = 0; i < children.length; i++) {
				File child = new File(dir, children[i]);
				if (child.isDirectory() && child.getName().equalsIgnoreCase(removeFolder)){
					try {
						String tempPath = System.getProperty(Constants.SYSTEM_PROPERTY_TEMP_DIR)+File.separator+child.getAbsolutePath().replaceAll(":", "");
						System.out.println("Moving "+ child.getAbsolutePath() + " to temp " + tempPath+"");
						FileUtils.moveDirectoryToDirectory(child, new File(tempPath), true);
						count++;
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}

				doIt(child,removeFolder);
				
			}
		}
	}
}
