/**
 * 
 */
package designPatterns.gof.behavioral.template;

/**
 * PatternBox: "AbstractClass" implementation.
 * <ul>
 * <li>defines abstract primitive operations that concrete subclasses define to implement steps of an algorithm.</li>
 * <li>implements a template method defining the skeleton of an algorithm. The template method calls primitive operations as well as operations defined in AbstractClass or those of other objects.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public abstract class AbstractClass {

	/**
	 * Default constuctor
	 */
	public AbstractClass() {
		super();
	}

	/**
	 * This method defines a skeleton of an algorithm, delegating some steps to primitive operations to its subclass implementation.
	 */
	public void templateMethod()
	{
		// TODO: Customize this method based on your application needs.
		primitiveOperation1();
		primitiveOperation2();
	}

	/**
	 * This abstract method must be implemented by a ConcreteClass. It is used by the templateMethod to proceed the algorithm.
	 */
	protected abstract void primitiveOperation1();

	/**
	 * This abstract method must be implemented by a ConcreteClass. It is used by the templateMethod to proceed the algorithm.
	 */
	protected abstract void primitiveOperation2();

}
