package parser.languagetranslator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Converter {
	public Converter(String input, String output) {
		try {
			FileInputStream fis = new FileInputStream(new File(input));
			BufferedReader in = new BufferedReader(new InputStreamReader(fis, "SJIS"));

			FileOutputStream fos = new FileOutputStream(new File(output));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));

			// create a buffer to hold the characters
			int len = 80;
			char buf[] = new char[len];

			// read the file len characters at a time and write them out
			int numRead;
			while ((numRead = in.read(buf, 0, len)) != -1)
				out.write(buf, 0, numRead);
			// close the streams
			out.close();
			in.close();
		} catch (IOException e) {
			System.out.println("An I/O Exception Occurred: " + e);
		}
	}

	public static void main(String args[])
	{
		new Converter(args[0], args[1]);
	}
}