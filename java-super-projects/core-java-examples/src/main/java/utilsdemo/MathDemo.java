package utilsdemo;

import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MathDemo {
	private static final Log logger = LogFactory.getLog(MathDemo.class);

	public static void main(String[] args)
	{
		formatDecimal();

	}

	private static void formatDecimal()
	{
		String pattern = "#.00";
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		Double value = 1.5;
		String output = myFormatter.format(value);
		logger.info(value + " " + pattern + " " + output);
	}

}
