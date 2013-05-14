/**
 * 
 */
package threads;


public class ThreadWaitNotify {

	
	public static void main(String[] args) throws Exception
	{
		Class c = Class.forName("threads.ThreadWaitNotify");
		Object obj = c.newInstance();
		obj.wait(); // / this gives java.lang.IllegalMonitorStateException: current thread not owner

	}

}
