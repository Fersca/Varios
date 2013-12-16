
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import net.spy.memcached.MemcachedClient;

public class MultigetWorker implements Runnable {

	MemcachedClient mem;
	Stats stats;
	int cantItems;
	Random r = new Random();
	ArrayList<String> items;
	
	MultigetWorker(int cantItems, MemcachedClient mem, Stats stats, ArrayList<String> items) {
	
		this.stats=stats;
		this.mem = mem;
		this.cantItems=cantItems;
		this.items = items;

	}

	public void run() {
				
		//Genera las 50 claves a buscar
		ArrayList<String> keys = new ArrayList<String>();

		int init = r.nextInt(items.size() - cantItems);
		
		for (int i = 0; i < cantItems; i++) {

			//genera la pos
			int itemPos = i + init;
			//Agrega el item a la lista de keys
			keys.add(items.get(itemPos));
		}
			
		long antes = System.currentTimeMillis();
		
		//hace el multiget
		Map<String, Object> mapa = mem.getBulk(keys);
		//hace un size solo para hace algo
		int tamanio=0;
		if (mapa!=null){
			tamanio = mapa.size();
		}
			
		long despues = System.currentTimeMillis();
				
		stats.calculateMaxMin(despues-antes, tamanio);
			
	}
	
} 