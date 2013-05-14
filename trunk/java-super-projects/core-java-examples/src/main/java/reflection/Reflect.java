package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities for reflection
 * @author Maurice Nicholson
 */
public class Reflect {

	// Get class instance
	// ---------------------------------------------------------
	/**
	 * Gets the named class; unlike {@link Class#forName(String)} this method handles primitive type names (int, char, double, etc) by returning the appropriate wrapper class (Integer.TYPE,
	 * Character.TYPE, etc)
	 * @return the Class for the given name, or null for null input
	 */
	public static Class getClassForName(String className) throws ReflectException
	{
		// Null input
		if (className == null) {
			return null;
		}
		// Primitive?
		if (className.equals("boolean")) {
			return Boolean.TYPE;
		} else if (className.equals("byte")) {
			return Byte.TYPE;
		} else if (className.equals("char")) {
			return Character.TYPE;
		} else if (className.equals("double")) {
			return Double.TYPE;
		} else if (className.equals("int")) {
			return Integer.TYPE;
		} else if (className.equals("short")) {
			return Short.TYPE;
		} else if (className.equals("long")) {
			return Long.TYPE;
		} else if (className.equals("float")) {
			return Float.TYPE;
		}
		// Get class
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException ex) {
			throw new ReflectException("No such class: " + className, ex);
		}
	}

	// ---------------------------------------------------------
	/**
	 * TODO handle primitive/wrapper compatibility? Return the most compatible class for the given type from the collection of classes; if none of the classes are compatible, returns null
	 * <p>
	 * The algorithm very simply traverses the given <tt>type</tt>'s class hierarchy attempting to match first the concrete class, then any interfaces defined to be implemented by that class, then the
	 * class's superclass, etc
	 * @param type the type for which to find the closest match
	 * @param classes the collection of classes in which to find the match
	 * @return the best class match found, or null
	 */
	public static Class getClosestCompatibleClassMatch(Class type, Collection classes)
	{

		// Null input?
		if (type == null || classes == null || classes.isEmpty()) {
			return null;
		}

		// Check for class and implemented interfaces of each class and
		// superclass
		for (Class clazz = type; clazz != null; clazz = clazz.getSuperclass()) {

			// Check for concrete class
			if (classes.contains(clazz)) {
				return clazz;
			}

			// Check for interfaces defined on this class
			Class[] interfaces = clazz.getInterfaces();
			for (int i = 0; interfaces != null && i < interfaces.length; i++) {
				if (classes.contains(interfaces[i])) {
					return interfaces[i];
				}
			}
		}
		// Not found
		return null;
	}

	/**
	 * Returns all the constructors in the given class for the given modifiers (one or more of of the static {@link Modifier} fields anded)
	 * @param clazz the class in which to find methods
	 * @param modifiers the modifiers the methods must match
	 * @return null for null input, otherwise a (maybe empty) list of {@link Method}s
	 */
	private static Constructor[] getConstructors(Class clazz, int modifiers)
	{
		if (clazz == null) {
			return null;
		}
		List list = new ArrayList();
		Constructor[] constructors = clazz.getConstructors();
		for (int i = 0; i < constructors.length; i++) {
			if ((modifiers & constructors[i].getModifiers()) != 0) {
				list.add(constructors[i]);
			}
		}
		constructors = (Constructor[]) list.toArray(new Constructor[] {});
		return constructors;
	}

	/**
	 * Gets an instance of the class, using the no-arg constructor obtained using reflection, and optionally attempting to override restricted (non-public) access
	 * @param clazz the Class to instantiate
	 * @return a new instance of the class
	 * @throws ReflectException if it was not possible to create the instance
	 */
	public static Object getInstance(Class clazz) throws ReflectException
	{
		try {
			// Is the constructor non-public?
			Constructor[] constructors = clazz.getDeclaredConstructors();
			for (int i = 0, iMax = constructors.length; i < iMax; i++) {
				if (constructors[i].getParameterTypes().length == 0) {
					if (constructors[i].isAccessible() == false) {
						constructors[i].setAccessible(true);
						return newInstance(constructors[i], null, clazz);
					}
				}
			}
			return clazz.newInstance();
		} catch (InstantiationException ex) {
			throw toReflectException(clazz, "<init>", null, ex);
		} catch (IllegalAccessException ex) {
			throw toReflectException(clazz, "<init>", null, ex);
		}
	}

	/**
	 * Gets an instance of the class with the given constructor arguments
	 * @param clazz the class to instantiate
	 * @param args the constructor arguments for the new object
	 * @return a newly constructed instance of the named class
	 * @throws ReflectException
	 */
	public static Object getInstance(Class clazz, Object[] args) throws ReflectException
	{
		Class[] classes = toClassArray(args);
		try {
			Constructor constructor = clazz.getConstructor(classes);
			return newInstance(constructor, args, clazz);
		} catch (NoSuchMethodException ex) {
			// No matching constructor, so try to find an equivalent one
			Constructor[] constructors = clazz.getConstructors();
			for (int i = 0, iSize = constructors.length; i < iSize; i++) {
				if (isClassArrayCompatible(classes, constructors[i].getParameterTypes())) {
					// Just try the first
					// TODO try others if any fail?
					return newInstance(constructors[i], args, clazz);
				}
			}
			// Complain that there really is no suitable method
			throw toReflectException(clazz, "<init>", classes, ex);
		}
	}

	/**
	 * Gets an instance of the class
	 */
	public static Object getInstance(String className) throws ReflectException
	{
		return getInstance(getClassForName(className));
	}

	// Get object instance
	// ---------------------------------------------------------
	/**
	 * Gets an instance of the named class with the given constructor arguments
	 * @param className the name of the class to instantiate
	 * @param args the constructor arguments for the new object
	 * @return a newly constructed instance of the named class
	 * @throws ReflectException
	 */
	public static Object getInstance(String className, Object[] args) throws ReflectException
	{
		Class clazz = getClassForName(className);
		return getInstance(clazz, args);
	}

	// ---------------------------------------------------------
	/**
	 * Get the named method, or the first compatible method found
	 */
	public static Method getMethod(Class clazz, String methodName, Class[] classes) throws ReflectException
	{

		if (clazz == null) {
			throw new IllegalArgumentException("Class cannot be null");
		}
		try {
			// Simple case
			return clazz.getMethod(methodName, classes);
		} catch (NoSuchMethodException ex) {
			// No exact match found so find the first compatible method
			Method[] methods = clazz.getMethods();
			for (int i = 0, iSize = methods.length; i < iSize; i++) {
				if (methods[i].getName().equals(methodName)) {
					Class[] argClasses = methods[i].getParameterTypes();
					if (isClassArrayCompatible(classes, argClasses)) {
						return methods[i];
					}
				}
			}
			throw toReflectException(clazz, methodName, classes, ex);
		}
	}

	/**
	 * Returns all the methods in the given class for the given modifiers (one or more of of the static {@link Modifier} fields anded)
	 * @param clazz the class in which to find methods
	 * @param modifiers the modifiers the methods must match
	 * @return null for null input, otherwise a (maybe empty) list of {@link Method}s
	 */
	private static Method[] getMethods(Class clazz, int modifiers)
	{
		if (clazz == null) {
			return null;
		}
		List list = new ArrayList();
		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if ((modifiers & methods[i].getModifiers()) != 0) {
				list.add(methods[i]);
			}
		}
		methods = (Method[]) list.toArray(new Method[] {});
		return methods;
	}

	/**
	 * Get the Class type of the given Class's named property
	 * @param clazz
	 * @param property
	 * @return
	 */
	public static Class getPropertyClass(Class clazz, String property) throws ReflectException
	{
		try {
			Method method = getMethod(clazz, "get" + property.toUpperCase().substring(0, 1) + property.substring(1), null);
			if (method == null) {
				method = getMethod(clazz, "is" + property.toUpperCase().substring(0, 1) + property.substring(1), null);
			}
			return method.getReturnType();
		} catch (ReflectException ex) {
			throw new ReflectException("Failed to find type of property " + clazz.getName() + "." + property, ex);
		}
	}

	/**
	 * Get the Class type of the given Object's named property
	 * @param object
	 * @param property
	 * @return
	 */
	public static Class getPropertyClass(Object object, String property) throws ReflectException
	{
		return getPropertyClass(object.getClass(), property);
	}

	/**
	 * Gets a map of the static fields for the given class, where the key is the field name and value the field value
	 * @param clazz the Class to retrieve fields for
	 */
	public static Map getStaticFieldMap(Class clazz) throws ReflectException
	{
		Map fieldMap = new HashMap();
		Field[] fields = clazz.getFields();
		for (int i = 0; i < fields.length; i++) {
			if (Modifier.isStatic(fields[i].getModifiers())) {
				try {
					fieldMap.put(fields[i].getName(), fields[i].get(null));
				} catch (IllegalAccessException e) {
					throw new ReflectException("Couldn't get value for static field " + clazz.getName() + "." + fields[i].getName());
				}
			}
		}
		return fieldMap;
	}

	/**
	 * Get the static named field of the given class type
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static Object getStaticFieldValue(Class clazz, String fieldName) throws ReflectException
	{
		try {
			Field field = clazz.getField(fieldName);
			if (field == null) {
				return null;
			}
			return field.get(null);
		} catch (Exception ex) {
			throw new ReflectException("Failed to get static field " + clazz.getName() + "." + fieldName, ex);
		}
	}

	/**
	 * Get the static named field of the given class type
	 * @param type
	 * @param fieldName
	 * @return
	 * @throws ReflectException
	 */
	public static Object getStaticFieldValue(String type, String fieldName) throws ReflectException
	{

		Class clazz = getClassForName(type);
		return getStaticFieldValue(clazz, fieldName);
	}

	// ---------------------------------------------------------
	/**
	 * Does the class have the named instance method for the given argument classes?
	 */
	public static boolean hasInstanceMethod(Class clazz, String methodName, Class[] classes)
	{
		try {
			Method method = getMethod(clazz, methodName, classes);
			return Modifier.isStatic(method.getModifiers()) == false;
		} catch (ReflectException ex) {
			return false;
		}
	}

	/**
	 * Does the class have the named static method for the given argument classes?
	 */
	public static boolean hasStaticMethod(Class clazz, String methodName, Class[] classes)
	{
		try {
			Method method = getMethod(clazz, methodName, classes);
			return Modifier.isStatic(method.getModifiers());
		} catch (ReflectException ex) {
			return false;
		}
	}

	/**
	 * Invoke the method on the given object instance with no arguments
	 */
	public static Object invoke(Object object, Method method) throws ReflectException
	{

		return invoke(object, method, null);
	}

	/**
	 * Invoke the given method on the object instance for the given args
	 */
	public static Object invoke(Object object, Method method, Object[] args) throws ReflectException
	{
		if (args != null) {
			// Assert.isTrue(method.getParameterTypes().length == args.length,
			// "Method [" + method.toString() + "] expects "
			// + method.getParameterTypes().length
			// + " arguments, but given " + args.length);
		}
		try {
			return method.invoke(object, args);
		} catch (Exception ex) {
			throw toReflectException(object, method, args, ex);
		}
	}

	/**
	 * Invoke the named no-arg method on the given object
	 */
	public static Object invoke(Object object, String methodName) throws ReflectException
	{

		try {
			Method method = object.getClass().getMethod(methodName, null);
			return invoke(object, method);
		} catch (Exception ex) {
			throw toReflectException(object, methodName, ex);
		}
	}

	/**
	 * Invoke the named method on the object instance for the given args
	 */
	public static Object invoke(Object object, String methodName, Object[] args) throws ReflectException
	{
		if (object == null) {
			return null;
		}
		Class[] classes = toClassArray(args);
		Method method = getMethod(object.getClass(), methodName, classes);
		return invoke(object, method, args);
	}

	/**
	 * Invokes the class (static) method with the given arguments. Just a simple helper method to hold common try/catch
	 * @param clazz the declaring class
	 * @param method the class (static) method to invoke
	 * @param args the object arguments
	 * @return the returned object
	 * @throws ReflectException
	 */
	protected static Object invokeStatic(Class clazz, Method method, Object[] args) throws ReflectException
	{

		if (method == null) {
			throw new IllegalArgumentException("Method argument cannot be null");
		}
		try {
			return method.invoke(null, args);
		} catch (Exception ex) {
			throw toReflectException(clazz, method.getName(), toClassArray(args), ex);
		}
	}

	// Invocation
	// ---------------------------------------------------------
	/**
	 * Invoke the static (class) method for the given args
	 */
	public static Object invokeStatic(Class clazz, String methodName, Object[] args) throws ReflectException
	{

		if (clazz == null) {
			return null;
		}
		Class[] classes = toClassArray(args);
		try {
			return invokeStatic(clazz, clazz.getMethod(methodName, classes), args);
		} catch (NoSuchMethodException ex) {
			// No method found for the specific class types, so try to find one
			// for equivalent classes
			Method[] methods = getMethods(clazz, Modifier.STATIC);
			for (int i = 0, iSize = methods.length; i < iSize; i++) {
				if (isClassArrayCompatible(classes, methods[i].getParameterTypes())) {
					// Just try the first
					// TODO try others if any fail
					return invokeStatic(clazz, methods[i], args);
				}
			}
			// Complain that there really is no suitable method
			throw toReflectException(clazz, methodName, classes, ex);
		}
	}

	/**
	 * Invokes the class (static) method with the given arguments. Just a simple helper method to hold common try/catch
	 * @param type the declaring class name
	 * @param methodName the class (static) method to invoke
	 * @param args the object arguments
	 * @return the returned object
	 * @throws ReflectException
	 */
	public static Object invokeStatic(String type, String methodName, Object[] args) throws ReflectException
	{

		Class clazz = getClassForName(type);
		Method method = getMethod(clazz, methodName, toClassArray(args));
		return invokeStatic(clazz, method, args);
	}

	// Equality and equivalence
	// ---------------------------------------------------------
	/**
	 * Is the object the given class type?
	 */
	public static boolean isClass(Object object, String className) throws ReflectException
	{
		if (object == null) {
			return false;
		}
		return object.getClass().equals(getClassForName(className));
	}

	/**
	 * Is the first class array compatible to the second <strong>from a constructor/method declaration/invocation perspective using the reflection APIs</strong>?
	 * <p>
	 * In other words, for each position in the array, is the class in argClasses equal to, a sub-class of or an implementation of the class in paramClasses.
	 * <p>
	 * For each array element calls {@link #isClassCompatible(Class, Class)}
	 * <p>
	 * Typically this method is used to determine whether the actual object arguments for a particular method or constructor call are applicable to the prototype classes.
	 * @param argClasses the array defining the "real" or "object instance" classes
	 * @param paramClasses the array defining the "base" or "method prototype" classes
	 * @return true if argClasses in is compatible with paramClasses
	 */
	protected static boolean isClassArrayCompatible(Class[] argClasses, Class[] paramClasses)
	{
		if (argClasses == null && paramClasses != null) {
			return false;
		} else if (argClasses != null && paramClasses == null) {
			return false;
		} else if (argClasses.length != paramClasses.length) {
			return false;
		}
		for (int i = 0, iSize = argClasses.length; i < iSize; i++) {
			if (isClassCompatible(argClasses[i], paramClasses[i]) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Is class1 runtime compatible with class2 instances <strong>from a constructor/method declaration/invocation perspective using the reflection APIs</strong>?
	 * <p>
	 * This method takes into account subclasses and interface implementations (using {@link #isSubclassOrImplementationOf(Class, Class)}) and compatible primitive/wrapper class types (using
	 * {@link #isEquivalentPrimitiveWrapperClass(Class, Class)})
	 */
	protected static boolean isClassCompatible(Class class1, Class class2)
	{
		return isSubclassOrImplementationOf(class1, class2) || isEquivalentPrimitiveWrapperClass(class1, class2);
	}

	/**
	 * Are the given classes equivalent primitive/wrapper classes?
	 * <p>
	 * This is only true when one class is the Object type (eg, Boolean.class) and the other is the wrapper type (eg, Boolean.TYPE).
	 * <p>
	 * This can be useful when finding methods by reflection, since the client may provide a Boolean object, where a method is declared as taking a "boolean" type; the reflection libraries actually
	 * convert this automatically when calling such a method, but {@link Class#getMethod(String, Class[])} won't return such matches
	 * <p>
	 * @param class1
	 * @param class2
	 * @return
	 */
	public static boolean isEquivalentPrimitiveWrapperClass(Class class1, Class class2)
	{
		if ((class1.equals(Boolean.class) && class2.equals(Boolean.TYPE)) || (class2.equals(Boolean.class) && class1.equals(Boolean.TYPE))) {
			return true;
		}
		if ((class1.equals(Byte.class) && class2.equals(Byte.TYPE)) || (class2.equals(Byte.class) && class1.equals(Byte.TYPE))) {
			return true;
		}
		if ((class1.equals(Character.class) && class2.equals(Character.TYPE)) || (class2.equals(Character.class) && class1.equals(Character.TYPE))) {
			return true;
		}
		if ((class1.equals(Double.class) && class2.equals(Double.TYPE)) || (class2.equals(Double.class) && class1.equals(Double.TYPE))) {
			return true;
		}
		if ((class1.equals(Integer.class) && class2.equals(Integer.TYPE)) || (class2.equals(Integer.class) && class1.equals(Integer.TYPE))) {
			return true;
		}
		if ((class1.equals(Short.class) && class2.equals(Short.TYPE)) || (class2.equals(Short.class) && class1.equals(Short.TYPE))) {
			return true;
		}
		if ((class1.equals(Long.class) && class2.equals(Long.TYPE)) || (class2.equals(Long.class) && class1.equals(Long.TYPE))) {
			return true;
		}
		if ((class1.equals(Float.class) && class2.equals(Float.TYPE)) || (class2.equals(Float.class) && class1.equals(Float.TYPE))) {
			return true;
		}
		return false;
	}

	/**
	 * Is the given class a primitive type?
	 * @param clazz the type to check
	 * @return true if the type is primitive
	 */
	public static boolean isPrimitive(Class clazz)
	{
		if (clazz == null) {
			return false;
		}
		return clazz.equals(Boolean.TYPE) || clazz.equals(Byte.TYPE) || clazz.equals(Character.TYPE) || clazz.equals(Double.TYPE) || clazz.equals(Integer.TYPE) || clazz.equals(Short.TYPE)
				|| clazz.equals(Long.TYPE) || clazz.equals(Float.TYPE);
	}

	// ---------------------------------------------------------
	// TODO the following three methods don't really have much to do with
	// reflection - could move to a new "java.lang.Class" style class
	/**
	 * Is the given type a primitive or object type wrapping primitive? This is true for Integer.class, Integer.type, etc as well as String.class
	 * @param clazz the type to check
	 * @return true if the given type is a primitive or primitive wrapper type
	 */
	public static boolean isPrimitiveOrPrimitiveWrapper(Class clazz)
	{
		return isPrimitive(clazz) || isPrimitiveWrapper(clazz);
	}

	/**
	 * Is the given type a primitive wrapper, eg, Integer?
	 * @param clazz the type to check
	 * @return true if the type is a primtive
	 */
	public static boolean isPrimitiveWrapper(Class clazz)
	{
		if (clazz == null) {
			return false;
		}
		return clazz.equals(Boolean.class) || clazz.equals(Byte.class) || clazz.equals(Character.class) || clazz.equals(Double.class) || clazz.equals(Integer.class) || clazz.equals(Short.class)
				|| clazz.equals(Long.class) || clazz.equals(Float.class);
	}

	/**
	 * Is class1 a sub-type or implementation type of class2?
	 */
	public static boolean isSubclassOrImplementationOf(Class class1, Class class2)
	{
		// Same class?
		if (class1.equals(class2)) {
			return true;
		}
		// Sub class?
		for (Class superClass = class1.getSuperclass(); superClass != null; superClass = superClass.getSuperclass()) {
			if (superClass == class2) {
				return true;
			}
		}
		// Interface implementor?
		Class[] interfaces = class1.getInterfaces();
		if (interfaces != null) {
			for (int i = 0, iSize = interfaces.length; i < iSize; i++) {
				if (interfaces[i].equals(class2)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Utility method to call a constructor, used to keep common exception handling code
	 * @param constructor the constructor to invoke newInstance() on
	 * @param args the arguments to pass
	 * @param clazz the class the constructor belongs to, used for error messages
	 * @return the new object instance
	 * @throws ReflectException if an error occurs
	 */
	private static Object newInstance(Constructor constructor, Object[] args, Class clazz) throws ReflectException
	{
		try {
			return constructor.newInstance(args);
		} catch (Exception ex) {
			throw toReflectException(clazz, "<init>", toClassArray(args), ex);
		}
	}

	/**
	 * Returns a class array for the given object types
	 */
	protected static Class[] toClassArray(Object[] objects)
	{
		if (objects == null) {
			return null;
		}
		Class[] classes = new Class[objects.length];
		for (int i = 0, iSize = objects.length; i < iSize; i++) {
			Class clazz = objects[i].getClass();
			classes[i] = clazz;
		}
		return classes;
	}

	/**
	 * Return a ReflectException for the given error
	 */
	private static ReflectException toReflectException(Class clazz, String methodName, Class[] classes, Throwable ex)
	{
		StringBuffer buf = new StringBuffer();
		if (ex instanceof NoSuchMethodException) {
			buf.append("No such method: ");
		} else if (ex instanceof IllegalAccessException || ex instanceof InvocationTargetException || ex instanceof InstantiationException) {
			buf.append("Failed to invoke method: ");
			if (ex instanceof InvocationTargetException) {
				ex = ((InvocationTargetException) ex).getTargetException();
			}
		}
		buf.append(clazz.getName() + "." + methodName + "(");
		if (classes != null) {
			for (int i = 0, iSize = classes.length; i < iSize; i++) {
				buf.append(classes[i].getName());
				if (i < iSize - 1) {
					buf.append(", ");
				}
			}
		}
		return new ReflectException(buf.toString() + ")", ex);
	}

	/**
	 * Return a ReflectException for the given error
	 */
	private static ReflectException toReflectException(Object object, Method method, Class[] classes, Exception ex)
	{
		return toReflectException((object == null ? null : object.getClass()), method.getName(), classes, ex);
	}

	/**
	 * Return a ReflectException for the given error
	 */
	private static ReflectException toReflectException(Object object, Method method, Exception ex)
	{
		return toReflectException(object, method, null, ex);
	}

	/**
	 * Return a ReflectException for the given error
	 */
	private static ReflectException toReflectException(Object object, Method method, Object[] args, Exception ex)
	{
		return toReflectException(object, method, toClassArray(args), ex);
	}

	// ---------------------------------------------------------
	/**
	 * Return a ReflectException for the given error
	 */
	private static ReflectException toReflectException(Object object, String methodName, Exception ex)
	{
		return toReflectException((Class) object == null ? null : object.getClass(), methodName, null, ex);
	}
}
