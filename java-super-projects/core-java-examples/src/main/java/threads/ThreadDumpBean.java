package threads;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ThreadDumpBean implements Serializable {
	private final Map traces;

	public static void main(String[] args)
	{
		Thread.dumpStack();
	}

	public ThreadDumpBean() {
		traces = new TreeMap(THREAD_COMP);
		// traces.putAll(Thread.());
	}

	public Collection getThreads()
	{
		return traces.keySet();
	}

	public Map getTraces()
	{
		return traces;
	}

	/**
	 * Compare the threads by name and id.
	 */

	private static final Comparator THREAD_COMP = new Comparator() {
		public int compare(Thread o1, Thread o2)
		{
			int result = o1.getName().compareTo(o2.getName());
			/*
			 * if (result == 0) { Long id1 = o1.getId(); Long id2 = o2.getId(); return id1.compareTo(id2); }
			 */
			return result;
		}

		public int compare(Object o1, Object o2)
		{
			int result = ((Thread) o1).getName().compareTo(((Thread) o2).getName());
			/*
			 * if (result == 0) { Long id1 = o1.getId(); Long id2 = o2.getId(); return id1.compareTo(id2); }
			 */
			return result;
		}
	};
}
