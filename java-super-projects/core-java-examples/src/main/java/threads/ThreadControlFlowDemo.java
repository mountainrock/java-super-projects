/**
 * 
 */
package threads;


public class ThreadControlFlowDemo {
	public ThreadControlFlowDemo() {
		// TODO Auto-generated constructor stub
	}

	static {
		Runtime.getRuntime().traceInstructions(true);// 
	}

	
	public static void main(String[] args) throws Throwable
	{
		// wait();
		Master m = new Master();
		m.start();
		// m.start(); throws illegal thread state exception
		m.join();
		System.out.println("Exiting main ");
		System.exit(0);
	}
}

class Master extends Thread {
	public void run()
	{
		System.out.println("I am the master " + this.getName());
		new Slave().start();
	}
}

class Slave extends Thread {
	public void run()
	{
		System.out.println("I am a slave obeying my masters orders " + this.getName());
	}
}
