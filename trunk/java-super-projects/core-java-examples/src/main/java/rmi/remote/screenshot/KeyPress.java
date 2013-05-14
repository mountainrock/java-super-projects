package rmi.remote.screenshot;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class KeyPress implements RobotAction {
	int key;

	public KeyPress(KeyEvent keyEvent) {
		key = keyEvent.getKeyCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rmi.remote.screenshot.RobotAction#execute(java.awt.Robot)
	 */
	public Object execute(Robot robot) throws IOException
	{
		robot.keyPress(key);
		System.out.println(Character.toChars(key)[0]);
		return null;
	}

}
