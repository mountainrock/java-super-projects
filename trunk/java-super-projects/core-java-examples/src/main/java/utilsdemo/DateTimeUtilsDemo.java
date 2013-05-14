/**
 * 
 */
package utilsdemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.StopWatch;


public class DateTimeUtilsDemo {

	
	public static void main(String[] args) throws ParseException
	{
		// testStopWatch();
		String format = "MM/dd/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		sdf.setLenient(false);
		String date = "02/01/2007";
		// Date strDate = sdf.format(date);
		// System.out.println(strDate);
		System.out.println(":" + new Date(date).getYear());
	}

	/**
	 * 
	 */
	private static void testStopWatch()
	{
		StopWatch w = new StopWatch();
		w.start();
		for (int i = 0; i < 10000000; i++) {
			int a = 0;
		}
		// w.stop();
		System.out.println(w.getTime());
		System.out.println(new Date().toString().replaceAll(" ", "_"));
		System.out.println(new SimpleDateFormat("dd-MMM-yyyy").format(new Date()));
	}

}
