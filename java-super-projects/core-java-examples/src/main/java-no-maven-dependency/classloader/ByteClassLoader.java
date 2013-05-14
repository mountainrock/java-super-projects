package classloader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

/**
 * Ref: http://stackoverflow.com/questions/1781091/java-how-to-load-class-stored-as-byte-into-the-jvm
 * @author sandeep
 * 
 */
public class ByteClassLoader extends URLClassLoader {
	private final Map<String, byte[]> extraClassDefs;

	public ByteClassLoader(URL[] urls, ClassLoader parent, Map<String, byte[]> extraClassDefs) {
		super(urls, parent);
		this.extraClassDefs = new HashMap<String, byte[]>(extraClassDefs);
	}

	@Override
	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		byte[] classBytes = this.extraClassDefs.remove(name);
		if (classBytes != null) {
			return defineClass(name, classBytes, 0, classBytes.length);
		}
		return super.findClass(name);
	}

	public static void main(String[] args) throws Exception {
		Map classDefs = new HashMap();
		String clazz = "public class Hello{" + "}";
		new JavaCompiler(new Context()) {
			@Override
			public InputStream openSource(String arg0) {
				return super.openSource(arg0);
			}
		};
		// TODO compile the class. To do this somehow inject the source code into the inputstream.

		classDefs.put("Hello", clazz.getBytes());
		ByteClassLoader b = new ByteClassLoader(new URL[] {}, Class.class.getClassLoader(), classDefs);
		Class<?> findClass = b.findClass("Hello");
		System.out.println(findClass.getName());
	}
}