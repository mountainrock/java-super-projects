package designPatterns.gof.behavioral.command;

/**
 * Instances of this class represent "command" commands.
 * 
 * @pattern Command
 * 
 * @generatedBy CodePro at 12/4/07 7:07 PM
 * 
 * @author Sandeep.Maloth
 * 
 * @version $Revision$
 */
public class CommandCommand extends AbstractCommand {

	/**
	 * Constructor
	 */
	public CommandCommand() {
		manager.invokeCommand(this);
	}

	/**
	 * Return the name of the command
	 * 
	 * @return the name of the command
	 */
	public String name()
	{
		return "command";
	}

	/**
	 * Perform the command encapsulated by this object.
	 * 
	 * @return true if this call to doIt was successful and can be undone
	 */
	public boolean doIt()
	{
		try {
			// insert the code to implement the command here
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Undo the command encapsulated by this object.
	 * 
	 * @return true if the undo was successful
	 */
	public boolean undoIt()
	{
		try {
			// insert the code to undo the command here
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
