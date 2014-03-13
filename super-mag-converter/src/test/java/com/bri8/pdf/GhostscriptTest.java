package com.bri8.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class GhostscriptTest {
	static String pdf = "src/test/resources/April.May_all_pages.pdf";

	public static void ghostScriptTest(String[] args) throws Exception {
		CommandLine cmdLine = new CommandLine("C:/Program Files/gs/gs9.10/bin/gswin32c.exe");
		cmdLine.addArgument("-h");
		cmdLine.addArgument("-dFirstPage=1");
		cmdLine.addArgument("-sOutputFile=D:/test%d.jpg");
		// cmdLine.addArgument("-dLastPage=2");
		cmdLine.addArgument("-dNOPAUSE");
		cmdLine.addArgument("-sDEVICE=jpeg");

		cmdLine.addArgument(pdf);
		System.out.println(cmdLine.getExecutable() + " " + StringUtils.join(cmdLine.getArguments(), " "));

		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(0);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
		executor.setWatchdog(watchdog);
		int exitValue = executor.execute(cmdLine);
	}

	public void pdfBoxTest() throws Exception {

		PDDocument doc = PDDocument.loadNonSeq(new File(pdf), null);
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		int i = 0;
		for (PDPage pdPage : pages) {
			PDPage page = pages.get(i++);
			System.out.println("page " + i);
			BufferedImage image = page.convertToImage();
			File outputfile = new File("src/test/resources/test/April.May_all_pages-" + i + ".png");
			ImageIO.write(image, "png", outputfile);

		}
		doc.close();
	}

}