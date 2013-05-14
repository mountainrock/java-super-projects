/**
 * 
 */
package designPatterns.gof.creational.singleton;

/**
 * PatternBox: "Client" implementation.
 * <ul>
 * <li>requests the Singleton to get its unique instance.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class Client {

	/**
	 * Default constructor
	 */
	public Client() {
		super();
	}

	/**
	 * This is just an example how to use the Singleton pattern. This operation must be customized based on your application needs.
	 */
	public void useSingleton()
	{
		// TODO: Customize this method based on your application needs.
		Singleton singleton = Singleton.getUniqueInstance();
		// call a Singleton method
		singleton.doSomething();
	}

}
