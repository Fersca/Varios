import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class shows how you could simulate the Golang "go" 
 * function to run tasks in background
 * @author Fersca
 * @date   15/1/2015
 *
 */
public class JavaGo {

	public static void main(String[] args) throws Exception {
		new JavaGo().run();
	}

	private void run() throws Exception {

		//Create a Lamda function to process in background
		Runnable printHello = () -> {
			int a = 4;
			for (int i=0;i<a;i++){
				sleep();
				System.out.println("Hello World..."+i);
			}
		};

		//Create a channel to wait for tasks
		AtomicInteger channel = new AtomicInteger();

		System.out.println("Sending tasks to background...");
		
		//Send three times the lamba to be proess in background
		go(printHello, channel);
		go(printHello, channel);
		go(printHello, channel);

		System.out.println("End sending background tasks");
		
		//send Another task but we are not going to wait for it to finish 
		go(printHello);
		
		//Wait until all tasks finish (the ones that uses the channel)
		wait(channel);
		System.out.println("Tasks finished");
		
		//End the threads in the executor
		executor.shutdown();

	}
	
	///////////////////////////////////////////////////
	//Mini framework to process the background tasks //
	///////////////////////////////////////////////////
	
	//Creating an executor that has one thread per core.
	ExecutorService executor = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors());


	/**
	 * Runs only one tasks in background without waiting to finish it
	 * @param f
	 */
	public void go(Runnable f) {
		Runnable r1 = () -> {
			f.run();
		};
		executor.execute(r1);
	}
	
	/**
	 * Sleep a random number of seconds between 0 and 3
	 */
	private void sleep(){
		try {
			Thread.sleep(new Random().nextInt(3000));
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Runs a runnable function in background, with a channel to synchronize 
	 * with the concurrent tasks
	 * @param f
	 * @param channel
	 */
	public void go(Runnable f, AtomicInteger channel) {
		channel.incrementAndGet();
		Runnable r1 = () -> {
			f.run();
			synchronized (channel) {
				channel.decrementAndGet();
				channel.notify();
			}
		};
		executor.execute(r1);
	}
	
	/**
	 * Wait until all the tasks are finished
	 * @param channel
	 * @throws Exception
	 */
	private void wait(AtomicInteger channel) throws Exception {
		synchronized (channel) {
			while (channel.get() > 0) {
				System.out.println("Waiting...");
				channel.wait();
			}
		}
	}
}
