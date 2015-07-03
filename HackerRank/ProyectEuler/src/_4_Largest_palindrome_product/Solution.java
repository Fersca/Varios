package _4_Largest_palindrome_product;

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
			System.out.println(calculo((int)s));			
		}
		
	}
			
	private long calculo(int n) {
			
		int max = 0;
		for(int i=100;i<1000;i++)
			for(int j=100;j<1000;j++) {
				int mult = i*j;
				if (mult<n && mult>max && palindrome(mult)) {
					max = mult;
				}
			}

		return max;
	}

	private boolean palindrome(int mult) {

		char[] numStr = (""+mult).toCharArray();

		for(int i=0;i<(numStr.length/2);i++) {
			if (numStr[i]!=numStr[numStr.length-1-i])
				return false;
		}
		
		return true;
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
