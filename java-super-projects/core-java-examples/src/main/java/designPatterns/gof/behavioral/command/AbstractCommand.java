package designPatterns.gof.behavioral.command;

/**
 * This class is the superclass of all of the command classes
 * 
 * @pattern Command (role=abstractCommandClass)
 * 
 * @generatedBy CodePro at 12/4/07 7:07 PM
 * 
 * @author Sandeep.Maloth
 * 
 * @version $Revision$
 */
public abstract class AbstractCommand {

	/**
	 * The CommandManager that manages command objects.
	 * 
	 * @see designPatterns.gof.behavioral.command.CommandManager
	 */
	public static final CommandManager manager = new CommandManager();

	/**
	 * Return the name of the command
	 * 
	 * @return the name of the command
	 */
	public abstract String name();

	/**
	 * Perform the command encapsulated by this object.
	 * 
	 * @return true if sucessful and can be undone.
	 */
	public abstract boolean doIt();

	/**
	 * Undo the last invocation of doIt.
	 * 
	 * @return true if the unndo was successful
	 */
	public abstract boolean undoIt();
}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId = com.instantiations.assist.eclipse.pattern.commandPattern strategyId =
 * com.instantiations.assist.eclipse.pattern.commandPattern.command abstract = true abstractCommandClass = AbstractCommand commandManagerClass = CommandManager commands = command, command2
 * generateUndoRedo = true interfaces = package = designPatterns.gof.behavioral.command package.sourceFolder = coreJavaExamples/src public = true superclassType = java.lang.Object
 */