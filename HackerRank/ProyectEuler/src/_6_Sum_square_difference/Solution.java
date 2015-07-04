package _6_Sum_square_difference;

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
			System.out.println(calcular(s));
		}		
	}
			
	private long calcular(long n) {

		return (long)Math.pow((n*(n+1)/2), 2)-((n*(n+1)*(2*n+1))/6);
		
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
