package designPatterns.gof.behavioral.command;

/**
 * Instances of this class represent undo commands.
 * 
 * @pattern Command
 * 
 * @generatedBy CodePro at 12/4/07 7:07 PM
 * 
 * @author Sandeep.Maloth
 * 
 * @version $Revision$
 */
public class UndoCommand implements Undo {

	/**
	 * Return the name of the command
	 * 
	 * @return the name of the command
	 */
	public String name()
	{
		return "Undo";
	}

	/**
	 * This implementation of doIt does not actually do anything. The logic for undo is in the CommandManager class. A CommandManager object knows that it is supposed to invoke its undo logic when it
	 * sees an instace of this class because this class implments the Undo interface. The Undo interface is a semantic interface that is used to mark a class as representing an undo command.
	 * 
	 * @return this method never returns a value
	 */
	public boolean doIt()
	{
		// This method should never be called
		throw new NoSuchMethodError();
	}

	/**
	 * This implementation of undoIt does not actually do anything. Undo commands are not undone. Instead a redo command is issued.
	 * 
	 * @return this method never returns a value
	 */
	public boolean undoIt()
	{
		// This method should never be called
		throw new NoSuchMethodError();
	}
}
