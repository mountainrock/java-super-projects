package queue;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class Worker extends Thread {
	// Special end-of-stream marker. If a worker retrieves
	// an Integer that equals this marker, the worker will terminate.
	static final Integer NO_MORE_WORK = new Integer(0);
	private static Log logger = LogFactory.getLog(Worker.class.getName());
	BlockingQueue<Integer> q;

	Worker(BlockingQueue<Integer> q) {
		this.q = q;
	}

	public void run()
	{
		try {
			while (true) {
				Thread.sleep(1000 * 5);
				// Retrieve an integer; block if the queue is empty
				Integer x = q.take();

				// Terminate if the end-of-stream marker was retrieved
				if (x == NO_MORE_WORK) {
					break;
				}

				logger.info(Thread.currentThread() + " worker consuming : " + x);
			}
		} catch (InterruptedException e) {
		}
	}
}
