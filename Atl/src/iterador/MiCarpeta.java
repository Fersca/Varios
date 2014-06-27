package iterador;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MiCarpeta<T, U> implements Carpeta<T, U>{

	
	public U aplica2(U u, Queue<T> list, Function2<T, U, U> function) {

		if(u == null || list == null || function == null)
            throw new IllegalArgumentException();

        if (list.isEmpty()) {
            return u;
        }
				
		return aplica2(function.apply(list.poll(), u), list, function);
		
	}

	@Override
	public U aplica(U u, Queue<T> list, Function2<T, U, U> function) {

		if(u == null || list == null || function == null)
            throw new IllegalArgumentException();
 
        while(!list.isEmpty()) {
        	u = function.apply(list.poll(), u);
        }
        
        return u;
        
	}
	
	
	public static void main(String[] args) {
		
		MiCarpeta<String, String> myfolder = new MiCarpeta<String, String>();
		
		Queue<String> queue = new ConcurrentLinkedQueue<String>();  
		
		queue.add("1");
		queue.add("2");
		queue.add("3");
		queue.add("4");
		queue.add("5");
		queue.add("5");
		queue.add("5");
		
		Print functClass = new Print();
		
		myfolder.aplica("0",queue,functClass);
		
		queue.add("1");
		queue.add("2");
		queue.add("3");
		queue.add("4");
		queue.add("5");
		queue.add("5");
		queue.add("5");

		myfolder.aplica2("0",queue,functClass);
				
	}
		
	static class Print implements Function2<String, String, String>{

		@Override
		public String apply(String t, String u) {
			String result = u.toString()+t.toString();
			System.out.println(result);
			return result;
		}

	}
}
