/**
 * 
 */
package rmi.remote.screenshot;

import java.awt.Robot;
import java.io.IOException;
import java.io.Serializable;

public interface RobotAction extends Serializable {
	Object execute(Robot robot) throws IOException;
}