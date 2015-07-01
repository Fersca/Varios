package _1_multiplesOfAnd5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
	

public class Solution {

	public static void main(String[] args) {
		Solution s = new Solution();	
		s.run();		
	}
    
	private void run() {
		
		long[] data = leer();		
		for(long s: data) {						
			System.out.println(calcularMultiplos(s));			
		}
		
	}
			
	public long factorial(long n) {
		return ((((n+1)/2)*(2+(n-1))) + (((n)/2)*(2+(n-1))))/2;				
    }

	private long calcularMultiplos(Long n) {

		if (n<3) return 0L;
		
		long canti = (n-1)/30L;		
		long sum = 225*canti + 14*30*(factorial(canti-1));
		
		long i = 30 * canti;		
				
		if ((i +3)<n)
			sum = sum + i +3;
		if ((i +5)<n)
			sum = sum + i +5;
		if ((i +6)<n)
			sum = sum + i +6;
		if ((i +9)<n)
			sum = sum + i +9;
		if ((i +10)<n)
			sum = sum + i +10;	
		if ((i +12)<n)
			sum = sum + i +12;
		if ((i +15)<n)
			sum = sum + i +15;
		if ((i +18)<n)
			sum = sum + i +18;
		if ((i +20)<n)
			sum = sum + i +20;
		if ((i +21)<n)
			sum = sum + i +21;
		if ((i +24)<n)
			sum = sum + i +24;
		if ((i +25)<n)
			sum = sum + i +25;
		if ((i +27)<n)
			sum = sum + i +27;
		if ((i +30)<n)
			sum = sum + i +30;

		return sum;
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
