/**
 * 
 */
package miscellaneous;

import java.io.File;


public class FileLister {
	public static void main(String[] args)
	{
		String path = "D:\\Sandeep\\JAVA\\java-projects\\workspace\\CoreJavaExamples_trunk\\src\\main\\js\\gallery-images";
		File f = new File(path);
		String[] list = f.list();
		for (int i = 0; i < list.length; i++) {
			String innerPath = path + "/" + list[i] + "/psd";
			System.out.println("{url: \"gallery-images/" + list[i] + "\", title: \"sample-" + i + "\"},");
			// System.out.println("<a href=\\""+ list[i]+ "\\\\index.html\\" target=\\"mainFrame\\">"+list[i]+"</a><br/>");
		}

	}
}
