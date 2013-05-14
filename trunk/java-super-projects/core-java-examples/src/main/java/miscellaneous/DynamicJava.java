package miscellaneous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 */


public class DynamicJava {

	public static void main(String args[]) throws IOException, InterruptedException
	{
		String fn = "JC.java";
		if (args.length > 0)
			fn = args[0];
		System.out.println("BEGIN (" + fn + ")");
		Process p = Runtime.getRuntime().exec("javac -verbose " + fn);
		String buf;
		BufferedReader se = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		while ((buf = se.readLine()) != null)
			System.out.println(" : " + buf);
		System.out.println("END (rc:" + p.waitFor() + ")");
		// StringUtils.
		// org.apache.commons.lang.StringUtils.

	}/*
	*/
	
	/*
	 * public static void main(String[] args) {
	 * 
	 * 
	 * Javac j = new Javac(); //j.setSrcdir(new Path(new Project())); j.setSource("D:\\EclipseWorkSpace\\CoreJavaExamples"); j.execute();
	 * 
	 * // TODO Auto-generated method stub
	 * 
	 * }
	 */

}
