/**
 * 
 */
package designPatterns.gof.structural.composite;

/**
 * PatternBox: "Component" implementation.
 * <ul>
 * <li>declares the interface for objects in the composition.</li>
 * <li>implements default behavior for the interface common to all classes, as appropriate.</li>
 * <li>declares an interface for accessing and managing its child components.</li>
 * <li>(optional) defines an interface for accessing a component's parent in the recursive structure, and implements it if that's appropriate.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public abstract class Component {

	/**
	 * This method is the implementation of Component specific operation.
	 */
	public abstract void operation();

	/**
	 * This method adds a new child (either Leaf or Composite) to the other children.
	 */
	public abstract void add(Component child) throws AbstractMethodError;

	/**
	 * This method removes a child (either Leaf or Composite) from the children.
	 */
	public abstract void remove(Component child) throws AbstractMethodError;

	/**
	 * This method returns a child (either Leaf or Composite) at a certain index.
	 */
	public abstract Component getChild(int index) throws AbstractMethodError;

}
