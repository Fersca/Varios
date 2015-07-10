package _8_Largest_product_in_a_series;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

	public static void main(String[] args) {
		Solution s = new Solution();	
		s.run();				
	}
    
	private void run() {		
		String[] data = leer();
		for (int i=0;i<data.length;i=i+2) {			
			String[] datos = data[i].split(" ");
			int k = Integer.parseInt(datos[1]);
			i++;
			String n = data[i];
			procesar(k,n);
		}
	}
			
	private void procesar(int k, String n) {

		
		//TODO: hay que arreglar el tema de cuando se tienen un cero.
		
		char[] numeros = n.toCharArray();
		
		long mult = 1;
		for (int i=0;i<k;i++) {						
			int num = Integer.parseInt(""+numeros[i]);
			mult = mult * num;
		}
		long max = mult;
		for (int i=k;i<numeros.length;i++) {						
			int num1 = Integer.parseInt(""+numeros[i-k]);
			int num2 = Integer.parseInt(""+numeros[i]);
			mult = (mult/num1)*num2;
			if (mult>max)
				max = mult;
		}
		System.out.println(max);
		  
	}
	
	private String[] leer() {
			
		String[] datos=null;
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	 
			String input;
	
			int max = Integer.parseInt(input=br.readLine())*2;		
			datos = new String[max];
			
			int cant = 0;
			while(cant<max && (input=br.readLine())!=null){				
				datos[cant] = input;
				cant++;
			}
			
		}catch(IOException io){
			io.printStackTrace();
		}
		
		return datos;

	}


}
