package performance;

public class ForLoopLocalVariablePerformanceOutside {

	public static void main(String[] args)
	{
		MemoryMonitor.getInstance().start(MemoryMonitor.DO_SYSTEM_GC);
		String s = null;
		for (int i = 0; i < 100; i++) {
			s = "asfsd";
		}
		MemoryMonitor.getInstance().stop(MemoryMonitor.PRINT_STATUS);
		System.out.println("Memory used :\r\n" + MemoryMonitor.getInstance().toString());

	}

}
