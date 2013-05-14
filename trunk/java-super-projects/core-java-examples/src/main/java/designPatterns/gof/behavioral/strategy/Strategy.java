/**
 * 
 */
package designPatterns.gof.behavioral.strategy;

/**
 * PatternBox: "Strategy" implementation.
 * <ul>
 * <li>declares an interface common to all supported algorithms. Context uses this interface to call the algorithm defined by a ConcreteStrategy.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public interface Strategy {

	/**
	 * This method declaration must be implemented by the ConcreteStrategy implementations.
	 */
	public void algorithmInterface();

}
