import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LamdaExample {

	public static void main(String[] args) throws Exception {

		LamdaExample l = new LamdaExample();
		l.run();

	}

	interface F {
		abstract void go();
	}

	private void run() throws Exception {

		// Ejemplo con una interface
		F function = new F() {
			@Override
			public void go() {
				System.out.println("Fer function");
			}
		};

		function.go();

		// Ejemplo con una clase runnable pasada a un thread
		Thread t = new Thread(new FerPrueba());
		t.run();

		// ejemplo creando un runnable directamente y ejecutando run
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				System.out.println("Prueba");

			}
		};
		r1.run();

		// Create a Lamda function to process in background
		Runnable imprime = () -> {
			int a = 4;
			for (int i=0;i<a;i++){
				System.out.println("Hello World..."+i);
			}
		};

		//Se lo pasa al executor
		AtomicInteger channel = new AtomicInteger();

		System.out.println("*************************");
		go(imprime, channel);
		go(imprime, channel);
		go(imprime, channel);
		System.out.println("1111111111111111111111111");
		wait(channel);
		System.out.println("2222222222222222222222222");
		executor.shutdown();
	}
	
	//Creating an executor that has one thread per core.
	ExecutorService executor = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors());


	/**
	 * Runs only one taks in background without waiting to finish it
	 * @param f
	 */
	public void go(Runnable f) {
		Runnable r1 = () -> {
			f.run();
		};
		executor.execute(r1);
	}
	
	/**
	 * Runs a runnable function in background, with a channel to synchronice with the concurrent tasks
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
	 * Waint until all the tasks are finished
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

	class FerPrueba implements Runnable {

		@Override
		public void run() {
			System.out.println("Fer thread");
		}

	}

}
