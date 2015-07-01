package _2_Even_Fibonacci_numbers;

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
			System.out.println(fib(s));			
		}
		
	}
			
    private long fib(long n) {
        long x = 0, y = 1, z = 1;
        long sum = 0;
        for (;;) {
            x = y;
            y = z;
            z = x + y;
            if (x>n)
            	break;
            if (x%2==0)
            	sum = sum +x;
        }
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
