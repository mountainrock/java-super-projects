package threads;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

class MyScheduledExecutorService {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void beepForAnHour()
	{
		final Runnable beeper = new MyBeeper(scheduler);
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 1, 1, SECONDS);
		// ManagedScheduledExecutorService
		// System.out.println(scheduler.isTerminated());

		// scheduler.schedule(new Runnable() {
		// public void run() { beeperHandle.cancel(true); }
		// }, 60 * 60, SECONDS);
	}

	public static void main(String args[])
	{
		MyScheduledExecutorService mses = new MyScheduledExecutorService();
		mses.beepForAnHour();
	}
}

class MyBeeper implements Runnable {
	ScheduledExecutorService scheduler;
	int count = 0;

	public MyBeeper(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}

	public void run()
	{
		System.out.println("beep");
		count++;
		if (count % 5 == 0) {
			System.out.println("Resetting scheduler");
			// /scheduler.shutdown();
			scheduler.scheduleAtFixedRate(this, 2, 6, SECONDS);

		}
	}

}
