package com.bri8.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class JpedalPdf2ImageUtilTest extends TestCase {

	private static Log logger = LogFactory.getLog(JpedalPdf2ImageUtilTest.class);

	@Test
	public void testPdfGeneration() throws Exception {
		final  String pdf = "src/test/resources/design_culture_mag.pdf";

		InputStream in = new FileInputStream(new File(pdf));
		List<BufferedImage> images = JpedalPdf2ImageUtil.getInstance().pdfToImage(in);

		 logger.info("testPdfGeneration");
		for (int i = 0; i < images.size(); i++) {
			System.out.println("writing " + i);
			BufferedImage bufferedImage = images.get(i);
			ImageIO.write(bufferedImage, "png", new FileOutputStream(new File("src/test/resources/test/" + i + ".png")));
		}
	}
}
