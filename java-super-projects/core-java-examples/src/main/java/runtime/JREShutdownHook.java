package runtime;

/**
 * This class demo's that u can have a callback just before shutting donw the JVM. Useful to release resources like DB connections just before JRE goes down.
 * @author sandeep.maloth
 * 
 */
public class JREShutdownHook {
	public static void main(String[] args)
	{
		Thread hook = new Thread() {
			public void run()
			{
				System.out.println("Just before Exiting JVM");
			}
		};

		final Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(hook);
		// runtime.halt(0); //halting a JRE ends abruptly and doesnt invoke the shutdown hook.
		runtime.exit(0); // this invokes shutdown hook.
	}
}
