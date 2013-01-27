package facebook;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Smiles {

	public static void main(String[] args) throws IOException {
		Smiles sm = new Smiles();
		sm.run();
	}

	private void run() throws IOException {

		//leer el archivo
		ArrayList<String> frases = leer();
		
		int i=1;
		//para cada frase
		for (String s : frases) {
			//calcular numero
			boolean result = calcularResultado(s);
			
			//imprimir el resultado del test
			resultado(result,i);
			i++;
		}
				
	}
	
	private boolean calcularResultado(String s) {

		if (s.equals(""))
			return true;
		
		
		String sinSmiles = sacarSmiles(s);
		
		String sinLetras = sacarLetras(sinSmiles);
		
		if ((""+sinLetras.charAt(0)).equals("(") && (""+sinLetras.charAt(sinLetras.length()-1)).equals(")")){
			
			int cantOpen=0;
			int cantClose=0;
			
			for(int i =0;i<sinLetras.length();i++){
				
				if ((""+sinLetras.charAt(i)).equals("(")){
					cantOpen++;
				} else {
					if ((""+sinLetras.charAt(i)).equals(")")){
						cantClose++;
					} else {
						return false;
					}
				}
				
			}
			
			return cantOpen==cantClose;			
			
		} else {
			return false;
		}
		
		
	
	}

	String permitidas = "abcdefghijklmnopqrstuvwxyz :";
	
	private String sacarLetras(String sinSmiles) {

		StringBuilder sb = new StringBuilder();
		
		for (int i=0;i<sinSmiles.length();i++){
			if (!permitidas.contains(""+sinSmiles.charAt(i))){
				sb.append(""+sinSmiles.charAt(i));
			}
		}
		
		return sb.toString();
	}

	private String sacarSmiles(String s) {
		
		String s2 = s.replaceAll(":\\)", "  ");
		String s3 = s2.replaceAll(":\\(", "  ");
		
		return s3;
	}

	private void resultado(boolean result, int i) {
		
		if (result){
			System.out.println("Case #"+i+": YES");	
		} else {
			System.out.println("Case #"+i+": NO");
		}
		
		
	}
	
	private ArrayList<String> leer() throws IOException {
		
		ArrayList<String> lista = new ArrayList<String>();
		
	    FileInputStream fstream = new FileInputStream("/home/fersca/code/facebook/problem2.txt");
	    
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
