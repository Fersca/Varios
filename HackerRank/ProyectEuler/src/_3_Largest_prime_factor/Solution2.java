package _3_Largest_prime_factor;

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
			generaPrimos(s);
			System.out.println(calculoImpar(s));			
		}
		
	}
		
	boolean[] primes;
	private void generaPrimos(long s) {		
		primes = new boolean[(int)Math.sqrt(s)+1];
		
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
	
	private long calculoImpar(long s) {
			
		long result = s;
		
		for(int i=2;i<primes.length;) {
			if (primes[i]) {
				if (result%i==0) {
					result = result/i;
					if (result==1) return i;
					continue;
				}
			}
			if (i==2) i++; else i=i+2;
		}
		
		return result;
		
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
