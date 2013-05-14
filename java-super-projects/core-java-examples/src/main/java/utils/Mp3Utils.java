package utils;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.StringUtils;
import org.farng.mp3.AbstractMP3Tag;
import org.farng.mp3.MP3File;

public class Mp3Utils {
	private static Mp3Utils _instance;

	private Mp3Utils() { // singleton
	}

	public static synchronized Mp3Utils getInstance() {
		if (_instance == null)
			_instance = new Mp3Utils();
		return _instance;
	}

	public String getMp3Title(String filePath) {
		MP3File mp3File;
		try {
			mp3File = new MP3File(filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		AbstractMP3Tag mp3Tag = mp3File.getID3v1Tag() != null ? mp3File.getID3v1Tag() : mp3File.getID3v2Tag();

		String yearReleased = StringUtils.isNotBlank(mp3Tag.getYearReleased()) ? " - " + mp3Tag.getYearReleased() : "";
		return mp3Tag.getSongTitle() + yearReleased;
	}

	public void bulkRename(String directory){
		File[] files= new File(directory).listFiles(new FileFilter(){

			public boolean accept(File f) {
				return f.getAbsolutePath().toLowerCase().endsWith(".mp3");
			}
			
		});
		for (int i = 0; i < files.length; i++) {
			try{
			String mp3Title = Mp3Utils.getInstance().getMp3Title(files[i].getAbsolutePath());
			files[i].renameTo(new File(files[i].getParent(),mp3Title+".mp3"));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
