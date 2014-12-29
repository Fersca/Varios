
public class LamdaExample {

	public static void main(String[] args) {

		LamdaExample l = new LamdaExample();
		LamdaExample q = new LamdaExample(); 
		l.run();
		q.run();

	}

	interface F {
		abstract void go();
	}
	
	
	private void run() {

		F function = new F() {
			@Override
			public void go() {
				System.out.println("Fer function");
			}
		}; 
		
		Thread t = new Thread(new FerPrueba());
		t.run();
		
		function.go();
		
			
	}
	
	class FerPrueba implements Runnable {

		@Override
		public void run() {
			System.out.println("Fer thread");
		}
		
	}
	
}
