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
		for (int i=0;i<data.length;i++) {			
			String[] datos = data[i].split(" ");
			int k = Integer.parseInt(datos[1]);
			i++;
			String n = data[i];
			procesarPeor(k,n);
		}
	}

	private void procesarPeor(int k, String n) {
				
		/* Esto anda pero no es muy performante,
		 * Lo que se puede hacer es multiplicar por el siguiente numero y dividir por el primero
		 * el tema con eso es que si el primero es un cero no hay que dividir por él,
		 * con lo cual lo que hay que hacer es ir detectando cuando hay un cero y avanzar
		 * 
		 */
		char[] numeros = n.toCharArray();
		
		long max = 0;
		
		//recorro todos las ventanas
		for (int i=0;i<numeros.length-k;i++) {

			//calculo la multiplicacion de la ventana
			long mult = 1;
			for (int j = 0;j<k;j++) {
				int num = Integer.parseInt(""+numeros[i+j]);
				mult = mult * num;				
			}
			
			//me guardo el máximo
			if (mult>max)
				max = mult;
			
		}
		  
		System.out.println(max);
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
