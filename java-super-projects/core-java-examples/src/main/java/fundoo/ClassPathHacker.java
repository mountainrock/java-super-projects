/**
 * 
 */
package fundoo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Ref: http://forum.java.sun.com/thread.jspa?threadID=300557&start=0&tstart=0
 * @author Sandeep.Maloth
 * 
 */
public class ClassPathHacker {

	static URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();

	public static void main(String[] args) throws IOException
	{
		ClassPathHacker classPathHacker = new ClassPathHacker();
		String springConfig = "spring-applicationContext.xml";
		String path = "D:" + File.separator + "EclipseWorkSpace" + File.separator + "CoreJavaExamples" + File.separator + "config" + File.separator + "" + springConfig;

		classPathHacker.addFile(path);
		URL[] urls = sysloader.getURLs();
		for (int i = 0; i < urls.length; i++) {
			URL url = urls[i];
			System.out.println(url.getPath());
			if (url.getPath().equals("/" + path.replaceAll("\\\\", "/")))
				System.out.println("successfully added url " + path);

		}

	}

	private static final Class[] parameters = new Class[] { URL.class };

	public static void addFile(String s) throws IOException
	{
		File f = new File(s);
		addFile(f);

	}// end method

	public static void addFile(File f) throws IOException
	{
		addURL(f.toURL());
	}// end method

	public static void addURL(URL u) throws IOException
	{

		Class sysclass = URLClassLoader.class;

		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}// end try catch

	}// end method

}// end class
