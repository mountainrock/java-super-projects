/**
 * 
 */
package designPatterns.gof.structural.facade;

/**
 * PatternBox: "Subsystem" implementation.
 * <ul>
 * <li>implement subsystem functionality.</li>
 * <li>handle work assigned by the Facade object.</li>
 * <li>have no knowledge of the facade; that is, they keep no references to it.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class Subsystem {

	public Subsystem() {
		super();
	}

	/**
	 * This can be any method of your model.
	 */
	public void operation1()
	{
		// TODO: Customize this method based on your application needs.
	}

	/**
	 * This can be any method of your model.
	 */
	public void operation2()
	{
		// TODO: Customize this method based on your application needs.
	}

	/**
	 * This can be any method of your model.
	 */
	public void operation3()
	{
		// TODO: Customize this method based on your application needs.
	}

}
