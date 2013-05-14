package miscellaneous;

import java.util.TimerTask;

/**
 * 
 */


public class TimerTest {

	
	public static void main(String[] args) throws Exception
	{

		long second = 1; // millis

		for (int i = 0; i < 10; i++) {
			delay(second);
			System.out.println("hi");
		}

	}

	private static void delay(long second) throws InterruptedException
	{
		long milli = second * 1000;
		Thread.sleep(milli);
	}

}

class RemindTask extends TimerTask {

	public void run()
	{
		System.out.println("reminder");
	}

}
