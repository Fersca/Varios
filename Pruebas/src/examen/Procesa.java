package examen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Procesa implements Runnable{

	ArrayList<Integer> col;
	
	@Override
	public void run() {
		
		Integer repetido = procesoFila(col);
		
		guardaRepe(repetido);					

	}
	
	public synchronized void guardaRepe(Integer repetido) {
		//me guardo el repetido
		if (repetido!=null)
			ExamenFer.repetidos.add(repetido);
	}

	public Integer procesoFila(ArrayList<Integer> fila) {

		HashMap<Integer, Integer> repeFila = new HashMap<Integer, Integer>(); 
		
		//llena el hashmap
		for (Integer numero : fila) {
			if (repeFila.containsKey(numero)) {
				repeFila.put(numero, repeFila.get(numero)+1);
			} else {
				repeFila.put(numero, 1);	
			}
		}
		
		Integer maximo = null;
		Integer value = null;
		
		Integer valueSegundo = null;
		
		//Busca el máximo en el hashmap
	    Iterator<Entry<Integer, Integer>> it = repeFila.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<Integer, Integer> pairs = (Map.Entry<Integer, Integer>)it.next();
	        
	        //busca el maximo
	        if (maximo==null) {
	        	maximo = pairs.getKey();
	        	value = pairs.getValue();
	        } else {
	        	if (pairs.getValue()>=value) {
		        	valueSegundo = value;
		        	maximo = pairs.getKey();
		        	value = pairs.getValue();	        		
	        	}
	        }    	
	    }
	    
	    if (maximo==null || (value==valueSegundo))
	    	return null;
	    else 
	    	return maximo;

	}

}
