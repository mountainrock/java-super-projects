package rmi.remote.screenshot;

import java.util.LinkedList;

public class RobotActionQueue {
	private final LinkedList jobs = new LinkedList();

	public RobotAction next() throws InterruptedException
	{
		synchronized (jobs) {
			while (jobs.isEmpty()) {
				jobs.wait();
			}
			return (RobotAction) jobs.removeFirst();
		}
	}

	public void add(RobotAction action)
	{
		synchronized (jobs) {
			jobs.add(action);
			System.out.println("jobs = " + jobs);
			jobs.notifyAll();
		}
	}
}