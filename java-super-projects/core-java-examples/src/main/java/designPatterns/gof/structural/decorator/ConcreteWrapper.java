package designPatterns.gof.structural.decorator;

/**
 * The class <code>AbstractWrapper</code> acts as a wrapper for subclasses of the class {@link <code>Object</code>}.
 * 
 * @pattern Decorator (role=concreteWrapperClass)
 * 
 * @generatedBy CodePro at 12/4/07 7:10 PM
 * 
 * @author Sandeep.Maloth
 * 
 * @version $Revision$
 */
public class ConcreteWrapper extends AbstractWrapper {

	public ConcreteWrapper(Object component) {
		super(component);
	}
}
