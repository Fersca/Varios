import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import net.spy.memcached.MemcachedClient;

public class MultigetWorker implements Runnable {

	MemcachedClient mem;
	Stats stats;
	int cantItems;
	Random r = new Random();
	
	MultigetWorker(int cantItems, MemcachedClient mem, Stats stats) {
	
		this.stats=stats;
		this.mem = mem;
		this.cantItems=cantItems;
	
	}

	public void run() {
		
		//base de items
		int num = 157714877;
		
		//Genera las 50 claves a buscar
		ArrayList<String> keys = new ArrayList<String>();
		for (int i = 0; i < cantItems; i++) {
			keys.add("MLA"+(num+r.nextInt(1000000)));
		}
			
		long antes = System.currentTimeMillis();
		
		//hace el multiget
		Map<String, Object> mapa = mem.getBulk(keys);
		//hace un size solo para hace algo
		int tamanio=0;
		if (mapa!=null)
			tamanio = mapa.size();
		
		long despues = System.currentTimeMillis();
				
		stats.calculateMaxMin(despues-antes, tamanio);
			
	}
	
} 