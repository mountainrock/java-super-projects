/**
 * 
 */
package designPatterns.gof.creational.singleton;

/**
 * PatternBox: "Singleton" implementation.
 * <ul>
 * <li>defines an Instance operation that lets clients access its unique instance. Instance is a class operation</li>
 * <li>may be responsible for creating its own unique instance.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class Singleton {

	/** unique instance */
	private static Singleton sInstance = null;
	/**
	 * The unique instance of this class.
	 */
	private static Singleton instance;

	/**
	 * Private constuctor
	 */
	private Singleton() {
		super();
	}

	/**
	 * Get the unique instance of this class.
	 */
	public static synchronized Singleton getUniqueInstance()
	{

		if (sInstance == null) {
			sInstance = new Singleton();
		}

		return sInstance;

	}

	/**
	 * This is just a dummy method that can be called by the client. Replace this method by another one which you really need.
	 */
	public void doSomething()
	{
	}

	/**
	 * Return the unique instance of this class.
	 * 
	 * @return the unique instance of this class
	 */
	public static Singleton getInstance()
	{
		if (instance == null) {
			instance = new Singleton();
		}
		return instance;
	}

}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId = com.instantiations.assist.eclipse.pattern.singletonPattern strategyId =
 * com.instantiations.assist.eclipse.pattern.singletonPattern.singleton createDynamic = true createDynamic.index = 0 fieldModifiers = private static fieldModifiers.index = 2 final = false
 * makeSerializable = false modifiers = public modifiers.index = 0 package = designPatterns.gof.creational.singleton package.sourceFolder = coreJavaExamples/src singletonType = Singleton
 * superclassType = java.lang.Object threadSafe = false uniqueInstanceFieldName = instance uniqueInstanceMethodName = getInstance useEnclosingType = false
 */