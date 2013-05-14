package apache.commons;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class BeanUtilsPopulateTest {
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
		Map map = new HashMap();
		map.put("firstName", "Sandeep");

		Person movie = new Person();
		BeanUtils.populate(movie, map);
		System.out.println(ReflectionToStringBuilder.toString(movie));
	}
}
