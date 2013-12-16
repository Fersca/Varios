
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.KetamaConnectionFactory;
import net.spy.memcached.MemcachedClient;

public class PruebaMemcached {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		//obtiene los par‡metros
		String archivo = args[0];
		String items = args[1];
		int threads = Integer.parseInt(args[2]);
		int multigets = Integer.parseInt(args[3]);
		
		//corre la simulaci—n
		PruebaMemcached memPru = new PruebaMemcached();
		memPru.run(archivo, items, threads, multigets);
	}
	
	private void run(String archivo, String itemsFile, int threads, int multigets) throws FileNotFoundException, IOException, InterruptedException {

		//variable para sumar lo que tardan los threads
		Stats stats = new Stats();
		
		//Obtiene la lista de servers
		String servers = readFileWithServers(archivo);
				
		//Obtiene el listado de items
		ArrayList<String> items = readFileWithItems(itemsFile);
		
		//Crea la conexion a los memcached con ketame de hash	
		MemcachedClient mem = new MemcachedClient(new KetamaConnectionFactory(), AddrUtil.getAddresses(servers));
					
		//Crea los workers y los ejecuta
		stats.init = System.currentTimeMillis();
		createWorkers(threads, multigets, stats, mem, items);
		stats.end = System.currentTimeMillis();
		
	    //Imprime stats
	    stats.printStats();
	    
	    //Fin de la simulaci—n
	    System.exit(0);
		
	}

	private ArrayList<String> readFileWithItems(String itemsFile) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(itemsFile));
		String line = null;
		
		ArrayList<String> items = new ArrayList<String>();
		
		while ((line = reader.readLine()) != null) {
			items.add("CUST"+line);
		}
		
		reader.close();
				
		return items;
		
	}

	private void createWorkers(int threads, int multigets, Stats stats, MemcachedClient mem, ArrayList<String> items) throws InterruptedException {
		
		//Crea varios threads para paralelizar los pedidos
	    ExecutorService executor = Executors.newFixedThreadPool(threads);
	    
	    //Hace X multigets de 50 items cada uno
	    for (int i = 0; i < multigets; i++) {
	      Runnable worker = new MultigetWorker(10, mem, stats, items);
	      executor.execute(worker);
	    }
	    
	    //no acepta ningun otro thread
	    executor.shutdown();
	    
	    System.out.println("Comienza a ejecutar threads");
	    
	    //espera que termine la ejecuci—n de threads
	    executor.awaitTermination(180,TimeUnit.SECONDS);
	}

	private String readFileWithServers(String archivo) throws FileNotFoundException, IOException {
		
		System.out.println("Leyendo la lista de equipos....");
		
		BufferedReader reader = new BufferedReader(new FileReader(archivo));
		String line = null;
		
		String serverList="";
		
		while ((line = reader.readLine()) != null) {
			
			if (line.contains("host")){
				
				int inicio = line.indexOf("e-");
				int fin = inicio + 10;
				String equipo = line.substring(inicio, fin) + ".melicloud.com:11211";
				
				serverList = serverList+ " "+equipo;

			}
		
		}
		
		reader.close();
		
		System.out.println("Servers: "+serverList);
		
		return serverList;
	}

}
