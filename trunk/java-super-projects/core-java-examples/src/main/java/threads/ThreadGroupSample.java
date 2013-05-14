package threads;

public class ThreadGroupSample {

	public static void main(String[] args)
	{
		ThreadGroup tg = new ThreadGroup("subgroup 1");
		Thread t1 = new Thread(tg, "thread 1");
		Thread t2 = new Thread(tg, "thread 2");
		Thread t3 = new Thread(tg, "thread 3");
		System.out.println("Active thread groups in " + tg.getName() + " thread group: " + tg.activeGroupCount());
		tg.list();

		tg = new ThreadGroup("subgroup 2");
		Thread t4 = new Thread(tg, "my thread");

		System.out.println("Active thread groups in " + tg.getName() + " thread group: " + tg.activeGroupCount());
		tg.list();

		tg = Thread.currentThread().getThreadGroup();
		System.out.println("Active thread groups in " + tg.getName() + " thread group: " + tg.activeGroupCount());
		tg.list();
	}

}
