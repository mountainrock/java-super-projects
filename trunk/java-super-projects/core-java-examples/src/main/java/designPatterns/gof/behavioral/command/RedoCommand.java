package designPatterns.gof.behavioral.command;

/**
 * Instances of this class represent redo commands.
 * 
 * @pattern Command
 * 
 * @generatedBy CodePro at 12/4/07 7:07 PM
 * 
 * @author Sandeep.Maloth
 * 
 * @version $Revision$
 */
public class RedoCommand extends AbstractCommand implements Redo {

	/**
	 * Return the name of the command
	 * 
	 * @return the name of the command
	 */
	public String name()
	{
		return "Redo";
	}

	/**
	 * This implementation of doIt does not actually do anything. The logic for redo is in the CommandManager class. A CommandManager object knows that it is supposed to invoke its redo logic when it
	 * sees an instace of this class because this class implments the Redo interface. The Redo interface is a semantic interface that is used to mark a class as representing an redo command.
	 * 
	 * @return this method never returns a value
	 */
	public boolean doIt()
	{
		// This method should never be called
		throw new NoSuchMethodError();
	}

	/**
	 * This implementation of undoIt does not actually do anything. Redo commands are not undone. Instead a undo command is issued.
	 * 
	 * @return this method never returns a value
	 */
	public boolean undoIt()
	{
		// This method should never be called
		throw new NoSuchMethodError();
	}
}
