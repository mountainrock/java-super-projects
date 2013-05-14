/**
 * 
 */
package performance;


public class MemoryMonitor {
	public static boolean PRINT_STATUS = true;

	public static boolean DO_SYSTEM_GC = true;

	private static ThreadLocal _instanceThreadLocal = new ThreadLocal() {
		protected synchronized Object initialValue()
		{
			return _instance = new MemoryMonitor();
		}

	};

	private static MemoryMonitor _instance;

	Runtime runtime = Runtime.getRuntime();

	private long initiaFreelMemory;

	private long afterUseFreeMemory;

	private MemoryMonitor() {

	}

	public static synchronized MemoryMonitor getInstance()
	{

		return (MemoryMonitor) _instanceThreadLocal.get();
	}

	public void start(boolean doGC)
	{
		if (doGC)
			System.gc();
		initiaFreelMemory = runtime.freeMemory();

	}

	public void stop(boolean printStatus)
	{
		afterUseFreeMemory = runtime.freeMemory();
		if (printStatus)
			toString();

	}

	private void log(String string)
	{
		System.out.println(string);

	}

	@Override
	public String toString()
	{
		return new StringBuffer("Initial = ").append(initiaFreelMemory).append("\r\n").append("Final = ").append(afterUseFreeMemory).append("\r\n").append("Consumed=").append(
				initiaFreelMemory - afterUseFreeMemory).toString();
	}

}
