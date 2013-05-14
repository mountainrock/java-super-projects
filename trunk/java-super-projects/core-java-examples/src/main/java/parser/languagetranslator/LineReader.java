package parser.languagetranslator;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Simple class to read lines in from a file. Create a LineReader object, then send it the readLine() message as many times as desired. When readLine() returns the value null, the last line has been
 * read, and you should close() the file.
 */
class LineReader {
	BufferedReader bufferedReader;

	/**
	 * Creates a LineReader to read lines (as Strings) from a file. Calling this constructor causes a standard file dialog box to open, allowing the user to choose a file; the message is displayed at
	 * the top of the dialog box.
	 */
	LineReader(String message) {
		// create and display a file dialog
		FileDialog dialog = new FileDialog(new Frame(), message, FileDialog.LOAD);
		dialog.setVisible(true);
		// get the directory and name of the selected file
		String dir = dialog.getDirectory();
		String file = dialog.getFile();
		// make sure we got a file
		if (dir == null || file == null) {
			System.err.println("No file selected.");
			return;
		}
		// construct the full path name of the file
		String fileName = dir + file;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("LineReader can't find input file: " + fileName);
			e.printStackTrace();
		}
		// create and save a reader for the file
		bufferedReader = new BufferedReader(fileReader);
	}

	/**
	 * This constructor is used if you forget to supply a message to be displayed in the dialog box.
	 */
	LineReader() {
		this("Read lines from what file?");
	}

	/**
	 * Once you have created a LineReader for a file, each call to readLine() will return another line from that file. After the last line in read, readLine() will return null instead of a String.
	 */
	String readLine()
	{
		if (bufferedReader == null) {
			System.err.println("readLine() called without a valid input file.");
			return null;
		}
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Closes the file used by the LineReader.
	 */
	void close()
	{
		try {
			bufferedReader.close();
		} catch (IOException e) {
		}
	}
}