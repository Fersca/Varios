package _7_10001st_prime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Solution2 {

	public static void main(String[] args) {
		Solution2 s = new Solution2();	
		s.run();				
	}
    
	private void run() {		
		long[] data = leer();
		for(long s: data) {
			if (s==1) {
				System.out.println(2);
			} else {
				primosPara((int)(s));
			}
		}		
	}
			
	private void primosPara(int s) {
		
	    boolean[] primes = new boolean[(s*s)+1];
		
		Arrays.fill(primes,true);
	    primes[0]=primes[1]=false;
	    for (int i=2;i<primes.length;i++) {
	        if(primes[i]) {
	            for (int j=2;i*j<primes.length;j++) {
	                primes[i*j]=false;
	            }
	        }
	    }	    	    
	    	    
	    int cant=0;
	    for(int i=0;i<primes.length;i++) {
	    	if (primes[i]) {
	    		cant++;
	    		if (cant==s) {
	    			System.out.println(i);
	    			return;
	    		}
	    	}
	    }
	    	
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
