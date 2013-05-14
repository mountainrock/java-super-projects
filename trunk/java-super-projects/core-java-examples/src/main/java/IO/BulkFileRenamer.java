package IO;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import antlr.StringUtils;

public class BulkFileRenamer {
	private static final String dir = "E:/mp3/audio-books/AbsolutelySmoothJazz/";

	
	public static void main(String[] args) throws IOException
	{
		File[] files = new File(dir).listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println("processing .... "+files[i].getName());

			String title = files[i].getName();
			
			title = title.trim().replaceAll(" ", "");

			File file = new File(dir , title);
			files[i].renameTo(file);

			System.out.println("Renamed "+title);

		}
	}

}
