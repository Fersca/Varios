package _7_10001st_prime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

	public static void main(String[] args) {
		Solution s = new Solution();	
		s.run();				
	}
    
	private void run() {		
		long[] data = leer();
		for(long s: data) {			
			System.out.println(calcular((int)s));
		}		
	}
			
	private int calcular(int n) {

		if (n==1) return 2;
		if (n==2) return 3;
		if (n==2) return 5;
		
		int[] primos = new int[n];
		int cant = 1;
		primos[cant-1] = 2;
		for(int i = 3;;i=i+2) {
			boolean primo = true;
			for(int j=0;j<cant;j++) {
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
