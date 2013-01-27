package facebook;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeautyStrings {

	public static void main(String[] args) throws IOException {

		BeautyStrings bs = new BeautyStrings();
		bs.run();
	
	}

	private void run() throws IOException {

		//leer el archivo
		ArrayList<String> frases = leer();
		
		int i=1;
		//para cada frase
		for (String s : frases) {
			//calcular numero
			int result = calcularResultado(s);
			
			//imprimir el resultado del test
			resultado(result,i);
			i++;
		}
				
	}


	String permitidas = "abcdefghijklmnopqrstuvwxyz";

	private int calcularResultado(String s) {
		
		
		HashMap<String, Integer> tabla = new HashMap<String, Integer>();
		
		for(int i = 0;i<s.length();i++){
			String letra = ""+s.charAt(i);
			
			letra = letra.toLowerCase();
			
			if (permitidas.contains(letra)){
			
				if (tabla.get(letra)!=null){
					tabla.put(letra, tabla.get(letra)+1);
				} else {
					tabla.put(letra, 1);
				}
				
			}
			
		}

		int valor = 26;
		HashMap<String, Integer> valores = new HashMap<String, Integer>();
		
		String letraMax = sacaMaximo(tabla);
		while(letraMax!=null){
			valores.put(letraMax, valor);
			valor = valor -1;
			letraMax = sacaMaximo(tabla);
		}
		
		int resultado = 0;
		for(int i = 0;i<s.length();i++){
			String letra = ""+s.charAt(i);
		
			Integer val = valores.get(letra.toLowerCase()); 
			if (val!=null){
				resultado = resultado + val;
				
			}
			
		}

		return resultado;
	}

	private String sacaMaximo(HashMap<String, Integer> tabla) {

		int max = 0;
		String letra = null;
		
		for (Map.Entry<String, Integer> entry : tabla.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		
		    if (key!=null && value!=null){
		    	
		    	if (value>max){
		    		max = value;
		    		letra = key;
		    		
		    	}
		    	
		    }

		}
		
		tabla.put(letra, null);
		return letra;
	}

	private void resultado(int result, int i) {
		
		System.out.println("Case #"+i+": "+result);
		
	}

	private ArrayList<String> leer() throws IOException {
		
		ArrayList<String> lista = new ArrayList<String>();
		
	    FileInputStream fstream = new FileInputStream("/home/fersca/code/facebook/problem1.txt");
	    
	    // Get the object of DataInputStream
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	    
	    int cantLine = Integer.parseInt(br.readLine());
	    
	    for (int i=1;i<=cantLine;i++){
	    	lista.add(br.readLine());
	    }

	    //Close the input stream
	    in.close();
		
	    return lista;
	}
	
}
