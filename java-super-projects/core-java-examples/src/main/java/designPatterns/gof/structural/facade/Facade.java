/**
 * 
 */
package designPatterns.gof.structural.facade;

/**
 * PatternBox: "Facade" implementation.
 * <ul>
 * <li>knows which subsystem classes are responsible for a request.</li>
 * <li>delegates client requests to appropriate subsystem objects.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class Facade {

	/** stores the subsystem interface */
	private Subsystem fSubsystem;

	/**
	 * Default constuctor
	 */
	public Facade() {
		super();
	}

	/**
	 * This method registers the associated subsystem interface.
	 */
	public void regSubsystem(Subsystem subsystem)
	{
		this.fSubsystem = subsystem;
	}

	/**
	 * This method calls a specific operation defined in your model.
	 */
	public void callOperation1()
	{
		fSubsystem.operation1();
	}

	/**
	 * This method calls a specific operation defined in your model.
	 */
	public void callOperation2()
	{
		fSubsystem.operation2();
	}

	/**
	 * This method calls a specific operation defined in your model.
	 */
	public void callOperation3()
	{
		fSubsystem.operation3();
	}

}
