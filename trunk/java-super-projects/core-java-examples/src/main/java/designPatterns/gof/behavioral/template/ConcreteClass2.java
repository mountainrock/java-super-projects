package designPatterns.gof.behavioral.template;

/**
 * PatternBox: "ConcreteClass" implementation.
 * <ul>
 * <li>implements the primitive operations to carry out subclass-specific steps of the algorithm.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class ConcreteClass2 extends AbstractClass {

	/**
	 * Default constuctor
	 */
	public ConcreteClass2() {
		super();
	}

	/**
	 * This abstract method must be implemented by a ConcreteClass. It is used by the templateMethod to proceed the algorithm.
	 */
	protected void primitiveOperation1()
	{
		// TODO Implement this primitive operation to perform a step in the algorithm defined in the AbstractClass.
	}

	/**
	 * This abstract method must be implemented by a ConcreteClass. It is used by the templateMethod to proceed the algorithm.
	 */
	protected void primitiveOperation2()
	{
		// TODO Implement this primitive operation to perform a step in the algorithm defined in the AbstractClass.
	}

}
