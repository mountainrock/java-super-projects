package parser;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class RadioStationURLChecker {

	public static void main(String[] args) throws Exception {

		String xml = FileUtils.readFileToString(new File("src/main/resources/station.xml"));
		String stations[] = StringUtils.substringsBetween(xml, "<station ", "/>");
		for (String string : stations) {

			String name = StringUtils.substringBetween(string, "name=\"", "\"");
			String sUrl = StringUtils.substringBetween(string, "url=\"", "\"");

			URL url = new URL(sUrl);
			HttpURLConnection urlConn = null;
			try {
			
				urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setConnectTimeout(5000);
				urlConn.connect();
				if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
					 System.out.println("* Valid "+name + " : " + sUrl);
				} else {
					System.out.println(urlConn.getResponseCode()+ " Invalid " + name + " : " + sUrl);
				}
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Invalid " + name + " : " + sUrl);
			}

		}

	}

}
