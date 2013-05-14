package groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.MetaMethod;
import java.io.File;

public class CLEmbedGroovy {
	/**
	 * http://www.ibm.com/developerworks/java/library/j-pg05245/index.html
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String args[]) throws Throwable
	{

		ClassLoader parent = CLEmbedGroovy.class.getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);

		Class groovyClass = loader.parseClass(new File("src/main/resources/hello.groovy"));

		GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();

		Object[] path = { "C://music//temp//mp3s" };
		groovyObject.setProperty("args", path);
		Object[] argz = {};

		groovyObject.invokeMethod("run", argz);

	}
}
