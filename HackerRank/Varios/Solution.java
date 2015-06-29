import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Solution {

	public static void main(String[] args) {

		Solution s = new Solution();	
		s.run();
		
	}
    
	private void run() {
		
		//obtiene el imput
		ArrayList<String> data = leer();
		ArrayList<Integer> result = new ArrayList<Integer>();		
		
		//procesa las frases
		for(String s: data) {
			int sum = procesaFrase(s);
			result.add(sum);			
		}
		
		//imprime el resultado
		for(Integer n : result) {
			System.out.println(n);
		}
				
	}
	

	private int procesaFrase(String original) {
		
		//genera la lista de sufijos
		ArrayList<String> suffixes = generaSufix(original);
		
		//suma de las similaridades
		int total=0;
		
		//procesa cada sufijo contra el original
		for (String s :suffixes) {
			int similar = calcularSimilaridad(original, s);
			total = total + similar;
		}
		
		return total;
		
	}
	
	
	
	private int calcularSimilaridad(String original, String s) {

		int iguales = 0;
		for(int i = 0;i<s.length();i++) {
			if (s.charAt(i)==original.charAt(i)) {
				iguales++;
			} else {
				break;
			}
		}

		return iguales;
	}

	private ArrayList<String> generaSufix(String original) {
		
		ArrayList<String> suffixes = new ArrayList<String>();
		
		for (int i = 0; i<original.length();i++) {
			String nuevo = substring(original, i);
			suffixes.add(nuevo);
		}
				
		return suffixes;
	}
	

	private String substring(String original, int i) {
		
		StringBuilder sb = new StringBuilder();
		
		for (int j = i;j<original.length();j++) {
			sb.append(original.charAt(j));
		}
		
		return sb.toString();
	}

	private ArrayList<String> leer() {
		
		ArrayList<String> datos = new ArrayList<String>();
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	 
			String input;
	
			int max = Integer.parseInt(input=br.readLine());
			
			int cant = 0;;
			while(cant<max && (input=br.readLine())!=null){
				datos.add(input);
				cant++;
			}
			
		}catch(IOException io){
			io.printStackTrace();
		}
		
		return datos;

	}
	

}
