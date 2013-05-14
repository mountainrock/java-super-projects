/**
 * 
 */
package designPatterns.gof.structural.composite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PatternBox: "Composite" implementation.
 * <ul>
 * <li>defines behavior for components having children.</li>
 * <li>stores child components.</li>
 * <li>implements child-related operations in the Component interface.</li>
 * </ul>
 * 
 * @author <a href="mailto:dirk.ehms@patternbox.com">Dirk Ehms</a>
 * @author Sandeep.Maloth
 */
public class Composite extends Component {

	/** Collection of children in the Composite hierachy */
	private final List fChildren = new ArrayList();

	/**
	 * This method iterates of all Component children specific operations.
	 */
	public void operation()
	{
		Iterator iterator = fChildren.iterator();
		while (iterator.hasNext()) {
			((Component) iterator.next()).operation();
		} // while
	}

	/**
	 * This method adds a new child (either Leaf or Composite) to the other children.
	 */
	public void add(Component child) throws AbstractMethodError
	{
		fChildren.add(child);
	}

	/**
	 * This method removes a child (either Leaf or Composite) from the children.
	 */
	public void remove(Component child) throws AbstractMethodError
	{
		fChildren.remove(child);
	}

	/**
	 * This method returns a child (either Leaf or Composite) at a certain index.
	 */
	public Component getChild(int index) throws AbstractMethodError
	{
		return (Component) fChildren.get(index);
	}

}
