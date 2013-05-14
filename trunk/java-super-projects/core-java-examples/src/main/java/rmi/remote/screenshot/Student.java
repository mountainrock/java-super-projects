/**
 * 
 */
package rmi.remote.screenshot;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Student {
	private final ObjectOutputStream out;

	private final ObjectInputStream in;

	private final Robot robot;

	public Student(String serverMachine, String studentName) throws IOException, AWTException {
		Socket socket = new Socket(serverMachine, Teacher.PORT);
		robot = new Robot();
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		out.writeObject(studentName);
		out.flush();
	}

	public void run() throws ClassNotFoundException
	{
		try {
			while (true) {
				RobotAction action = (RobotAction) in.readObject();
				Object result = action.execute(robot);
				if (result != null) {
					out.writeObject(result);
					out.flush();
					out.reset();
				}
			}
		} catch (IOException ex) {
			System.out.println("Connection closed");
		}
	}

	public static void main(String[] args) throws Exception
	{
		if (args == null || args.length == 0) {
			args = new String[] { "127.0.0.1", "sandeep" };
		}
		Student student = new Student(args[0], args[1]);
		student.run();
	}
}