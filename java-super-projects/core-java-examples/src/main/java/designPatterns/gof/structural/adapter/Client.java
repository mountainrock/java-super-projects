/**
 * 
 */
package designPatterns.gof.structural.adapter;

/**
 * PatternBox: "Client" implementation.
 * <ul>
 * <li>collaborates with objects conforming to the Target interface.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class Client {

	private final Target fTarget;

	public Client(Target target) {
		super();
		fTarget = target;
	}

	/**
	 * This is just an example how to use the Adapter pattern. This operation must be customized based on your application needs.
	 */
	public void useAdapter()
	{
		// TODO: Customize this method based on your application needs.
		fTarget.request();
	}

}
