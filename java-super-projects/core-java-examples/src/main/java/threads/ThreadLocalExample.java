package threads;

class LocalExample extends Thread {
	private static ThreadLocal name = new ThreadLocal();

	LocalExample(String x) {

	}

	public void run()
	{
		double random = Math.random();

		this.name.set(random + ":::");
		while (true) {
			System.out.println(Thread.currentThread().getName());
			System.out.println(name());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String name()
	{

		return (String) name.get();
	}
}

public class ThreadLocalExample {
	public static void main(String args[]) throws InterruptedException
	{
		System.out.println(Thread.currentThread().getName());
		LocalExample a = new LocalExample("A");
		LocalExample b = new LocalExample("B");
		a.start();
		b.start();
		Thread.sleep(1000);

		System.out.println("TL A's name " + a.name());
		System.out.println("TL B's name " + b.name());
		a.join();
		b.join();

	}
}
