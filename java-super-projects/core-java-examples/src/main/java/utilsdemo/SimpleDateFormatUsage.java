/**
 * 
 */
package utilsdemo;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SimpleDateFormatUsage {

	
	public static void main(String[] args)
	{
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		System.out.println(simpleDateFormat.format(date));

	}

}
