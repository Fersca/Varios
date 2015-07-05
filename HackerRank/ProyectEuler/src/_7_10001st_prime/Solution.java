package _7_10001st_prime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

	public static void main(String[] args) {
		Solution s = new Solution();	
		s.run();				
	}

	int[] primos = new int[10000];
	int cant = 2;
	
	private void run() {		
		long[] data = leer();
		primos[0] = 2;	
		primos[1] = 3;
		
		for(long s: data) {			
			System.out.println(calcular((int)s));
		}		
	}
			
	private int calcular(int n) {
		
		if (n<=cant) return primos[n-1];
		
		for(int i = primos[cant-1]+2;;i=i+2) {
			boolean primo = true;
			for(int j=1;j<cant;j++) {
				if(i%primos[j]==0) {
					primo = false;
					break;
				}
			}
			if (primo) {
				cant++;
				primos[cant-1]=i;
				if (cant==n) {
					return i;			
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
