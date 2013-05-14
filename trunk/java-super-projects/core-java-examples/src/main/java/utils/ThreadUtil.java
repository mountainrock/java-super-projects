package utils;

public class ThreadUtil {
	public static final int SECONDS_5 = 5;
	public static final int SECONDS_10 = SECONDS_5 * 2;

	public static void sleep(int seconds) {
		try {
			Thread.sleep(1000 * seconds);
		} catch (InterruptedException e) {// ignore
		}
	}
}
