package runtime;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.lf5.util.StreamUtils;

public class RuntimeUtilsDemo {

	private static Log logger = LogFactory.getLog(RuntimeUtilsDemo.class);

	public static void main(String[] args) {
		String perlCommand = "cmd /c perl ";

		run(perlCommand);
	}

	private static void run(String command) {
		try {
			logger.info("Running " + command);
			Process exec = Runtime.getRuntime().exec(command);
			StreamUtils.copy(exec.getInputStream(), System.out);
			StreamUtils.copy(exec.getErrorStream(), System.err);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
