package _5_Smallest_multiple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Solution {

	public static void main(String[] args) {
		Solution s = new Solution();	
		s.run();				
		
	}
    
	private void run() {
		
		long[] data = leer();
		for(long s: data) {
			
			System.out.println(multipleMcm((int)s));
		}
		
	}
			
	private long multipleMcm(int n) {
		
		long mcm=1;
		for (int i=1;i<=n;i++) {
			mcm = mcm(mcm,i);
		}		
		return mcm;
	}

	private long mcm(long mcm, long i) {
		HashMap<Integer, Integer> f1 = factores(mcm);
		HashMap<Integer, Integer> f2 = factores(i);
		HashMap<Integer, Integer> minumos = minimosFactors(f1,f2);
		return multiplicaFactores(minumos);		
	}

	private long multiplicaFactores(HashMap<Integer, Integer> factores) {

		long result = 1;
		//multiplica cada factor por su potencia.
		for (Integer f: factores.keySet()) {
			result = result * ((long)Math.pow(f, factores.get(f)));
		}
		return result;
		
	}

	private HashMap<Integer, Integer> minimosFactors(HashMap<Integer, Integer> f1, HashMap<Integer, Integer> f2) {
		
		HashMap<Integer, Integer> minimosFactores = new HashMap<Integer, Integer>();
		
		//por cada primo en un mapa
		for (Integer primo: f1.keySet()) {			
			//verifica si el primo está en el otro mapa
		    if (f2.containsKey(primo)) {		    	
		    	//si está pone en el mapa resultante el primo con la mayor potencia
		    	if (f1.get(primo)>f2.get(primo)) {
		    		minimosFactores.put(primo, f1.get(primo));
		    	} else {
		    		minimosFactores.put(primo, f2.get(primo));
		    	}
		    } else {
		    	//si no está pone directamente el primo con la potencia que tiene
		    	minimosFactores.put(primo, f1.get(primo));
		    }
		}

		//hago lo mismo con el segundo mapa
		for (Integer primo: f2.keySet()) {			
			//verifica si el primo está en el otro mapa
		    if (f1.containsKey(primo)) {		    	
		    	//si está pone en el mapa resultante el primo con la mayor potencia
		    	if (f1.get(primo)>f2.get(primo)) {
		    		minimosFactores.put(primo, f1.get(primo));
		    	} else {
		    		minimosFactores.put(primo, f2.get(primo));
		    	}
		    } else {
		    	//si no está pone directamente el primo con la potencia que tiene
		    	minimosFactores.put(primo, f2.get(primo));
		    }
		}

		//devuelve el resultado final
		return minimosFactores;
	}

	HashMap<Long, HashMap<Integer, Integer>> cache = new HashMap<Long, HashMap<Integer,Integer>>(); 
	private HashMap<Integer, Integer> factores(long numero) {

		//devuelve del caché si es que está
		if (cache.containsKey(numero)) return cache.get(numero);
		
		//busco los primos
		ArrayList<Integer> primos = primosPara(numero);
		HashMap<Integer, Integer> factores = new HashMap<Integer, Integer>();
		
		long result = numero;

		//por cada primo, calculo la division
		for (int i = 0;i<primos.size();) {
			
			int p = primos.get(i);
			//calculo si es dividor
			if (result%p==0) { 
				
				//incrementa el factor
				if (factores.containsKey(p)) 
					factores.put(p, factores.get(p)+1); 
				else 
					factores.put(p, 1);  
				
				//calculo el resultado
				result = result/p;
				
			} else {
				i++;
			}
		}
		
		if (factores.isEmpty()) {
			factores.put((int)numero, 1);
		} else {
			if (result>1)
				factores.put((int)result, 1);
		}
		
		//lo guarda en el cache
		cache.put(numero, factores);
		
		//devuelvo los factores
		return factores;
	}

	private ArrayList<Integer> primosPara(long s) {
		
	    ArrayList<Integer> primos = new ArrayList<Integer>();
	    if (s==1) return primos;
	    /*
	    if (s==2) {
	    	primos.add(2);
	    	return primos;
	    }
	    */
		boolean[] primes = new boolean[(int)Math.sqrt(s)+1];
		
		Arrays.fill(primes,true);
	    primes[0]=primes[1]=false;
	    for (int i=2;i<primes.length;i++) {
	        if(primes[i]) {
	            for (int j=2;i*j<primes.length;j++) {
	                primes[i*j]=false;
	            }
	        }
	    }	    	    
	    	    
	    for(int i=0;i<primes.length;i++)
	    	if (primes[i]) primos.add(i);
	     
	    //agrego el numero que da dividido por dos, si llega a usarse es porque es primo 
	    //if (s%2==0) primos.add((int)s/2);
	   
	    return primos;
	}
	

	private long[] leer() {
			
		long[] datos=null;
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	 
			String input;
	
			int max = Integer.parseInt(input=br.readLine());		
			datos = new long[max];
			
			int cant = 0;
			while(cant<max && (input=br.readLine())!=null){				
				datos[cant] = Long.parseLong(input);
				cant++;
			}
			
		}catch(IOException io){
			io.printStackTrace();
		}
		
		return datos;

	}


}
