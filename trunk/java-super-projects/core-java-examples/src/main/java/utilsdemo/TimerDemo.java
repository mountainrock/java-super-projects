package utilsdemo;

import java.util.Timer;
import java.util.TimerTask;

public class TimerDemo {
	public static void main(String[] args)
	{
		System.out.println("This is a timer");
		TimerDemo timerDemo = new TimerDemo();

		timerDemo.schedule();
	}

	/**
	 * 
	 */
	private void schedule()
	{
		Timer t = new Timer();
		t.scheduleAtFixedRate(new EatTask(), 0, 1000);
		// t.cancel();
	}

	private class EatTask extends TimerTask {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run()
		{
			System.out.println("I am hungry .I want to eat some stuff!!");

		}

	}

}
