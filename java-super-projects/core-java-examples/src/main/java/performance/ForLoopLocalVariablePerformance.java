package performance;

public class ForLoopLocalVariablePerformance {

	public static void main(String[] args)
	{
		MemoryMonitor.getInstance().start(MemoryMonitor.DO_SYSTEM_GC);

		for (int i = 0; i < 2000; i++) {
			String s = "A";
			System.out.println(s);
		}
		MemoryMonitor.getInstance().stop(MemoryMonitor.PRINT_STATUS);
		System.out.println("Memory used :" + (MemoryMonitor.getInstance()));

	}

	private static String[] getDummyArray(int i)
	{
		String ar[] = new String[i];
		for (int j = 0; j < i; j++) {
			ar[j] = "DUMMY";

		}
		return ar;
	}

}
