package threads;

class ThreadLocalDemo1 {
	public static void main(String[] args)
	{
		MyThread mt1 = new MyThread("A");
		MyThread mt2 = new MyThread("B");
		MyThread mt3 = new MyThread("C");
		mt1.start();
		mt2.start();
		mt3.start();
	}
}

class MyThread extends Thread {
	private static ThreadLocal tl = new ThreadLocal() {
		protected synchronized Object initialValue()
		{
			return new Integer(sernum++);
		}
	};
	private static int sernum = 100;

	MyThread(String name) {
		super(name);
	}

	public void run()
	{
		for (int i = 0; i < 10; i++)
			System.out.println(getName() + " " + tl.get());
	}
}