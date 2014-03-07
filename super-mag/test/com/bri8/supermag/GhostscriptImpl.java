package parser.pdf;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;


public class GhostscriptImpl {
	final static String pdf="C:/Program Files (x86)/GS/gs8.00/bin/April.May_all_pages.pdf";

	public static void main(String[] args) throws Exception {
		CommandLine cmdLine = new CommandLine("C:/Program Files (x86)/GS/gs8.00/bin/gswin32c.exe");
		cmdLine.addArgument("-dFirstPage=1");
		cmdLine.addArgument("-sOutputFile=C:/Program Files (x86)/GS/gs8.00/bin/tests%d.jpg");
		cmdLine.addArgument("-dLastPage=14");
		cmdLine.addArgument("-dNOPAUSE");
		cmdLine.addArgument("-sDEVICE=jpeg");

		cmdLine.addArgument(pdf);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(0);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
		executor.setWatchdog(watchdog);
		int exitValue = executor.execute(cmdLine);
	}

}