package _3_Largest_prime_factor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class Solution {

	public static void main(String[] args) {
		Solution s = new Solution();	
		s.run();		
	}
    
	private void run() {
		
		long[] data = leer();
		//long[] data = {10,11,17,999,13195,33333,100000000,9999999,999999999,999999999,24523535};
		//333667, 9091,119627
		for(long s: data) {
			fillSieve(s);
			System.out.println(calculoImpar(s));			
		}
		
	}
	
	boolean[] primes;
	
	private void fillSieve(long s) {
			
		minimoImpar = calcularMinRange(s,0);
		
		if (found) {
			primes = new boolean[((int)minimoImpar)+1];
		} else {
						
			if ((s/prim[prim.length-1])<minimoImpar)
				minimoImpar = s/prim[prim.length-1];
						
			primes = new boolean[((int)minimoImpar)+1];
		}
		
		Arrays.fill(primes,true);
	    primes[0]=primes[1]=false;
	    for (int i=2;i<primes.length;i++) {
	        if(primes[i]) {
	            for (int j=2;i*j<primes.length;j++) {
	                primes[i*j]=false;
	            }
	        }
	    }
	}	
	
	boolean found=false;
	
	int[] prim = generateInitialPrimes();

	private long calcularMinRange(long n, int pos) {
    	
		if (pos>prim.length-1)
			return n;
		
    	if (n%prim[pos]==0) {
    		if (n/prim[pos]==1) {
    			found=true;
    			return n;
    		}
    		n = n/prim[pos];
    		return calcularMinRange(n,pos);
    	}
    	else 
    		return calcularMinRange(n,pos+1);    		
		
	}

	private int[] generateInitialPrimes() {

		boolean[] primes1 = new boolean[10000];
	
		Arrays.fill(primes1,true);
	    primes1[0]=primes1[1]=false;
	    for (int i=2;i<primes1.length;i++) {
	        if(primes1[i]) {
	            for (int j=2;i*j<primes1.length;j++) {
	                primes1[i*j]=false;
	            }
	        }
	    }

	    ArrayList<Integer> primos2 = new ArrayList<Integer>();
	    
	    for (int i=0;i<10000;i++) {
	    	if (primes1[i]) primos2.add(i); 
	    }

	    int[] primos3 = new int[primos2.size()];
	    
	    int j = 0;
	    for (int i : primos2) {
			primos3[j] = i;		
			j++;
		}

	    return primos3;

	    
	}

	long minimoImpar = 0;
    private long calculoImpar(long n) {
        	
    	//A este punto solo llegan los impares
    	int i = (int)minimoImpar;
		for (; i>2;i=i-2) {
			if (primes[(int)i]) {
				if (n%i==0) {
					return i;
				}				
			}
		}		
    	
		if (n%2==0) return 2;
    	return n;    	
    	
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
