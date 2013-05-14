/**
 * 
 */
package designPatterns.gof.structural.adapter;

/**
 * PatternBox: "Adapter" implementation.
 * <ul>
 * <li>adapts the interface of Adaptee to the Target interface.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class Adapter implements Target {

	private final Adaptee fAdaptee;

	public Adapter(Adaptee adaptee) {
		super();
		fAdaptee = adaptee;
	}

	/**
	 * This method adapts the Target to the Adaptee's specific method.
	 */
	public void request()
	{
		fAdaptee.specificRequest();
	}

}
