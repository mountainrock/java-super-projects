package queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Producer {
	private static Log logger = LogFactory.getLog(Producer.class.getName());

	public static void main(String[] args)
	{
		// Create a bounded blocking queue of integers
		final int capacity = 1;
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(capacity);

		// Create a set of worker threads
		final int numWorkers = 2;
		Worker[] workers = new Worker[numWorkers];
		for (int i = 0; i < workers.length; i++) {
			workers[i] = new Worker(queue);
			workers[i].start();
		}

		try {
			// Add some work to the queue; block if the queue is full.
			// Note that null cannot be added to a blocking queue.
			for (int i = 0; i < 100; i++) {
				queue.put(i);
				logger.info(Thread.currentThread() + " Producer inserting - " + i);

			}

			// Add special end-of-stream markers to terminate the workers
			for (int i = 0; i < workers.length; i++) {
				queue.put(Worker.NO_MORE_WORK);
			}
		} catch (InterruptedException e) {
		}
	}
}
