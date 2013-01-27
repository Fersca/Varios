package facebook;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class FinMin {

	public static void main(String[] args) throws IOException {
		FinMin sm = new FinMin();
		sm.run();
	}

	private void run() throws IOException {

		//leer el archivo
		ArrayList<ArrayList<Long>> numeros = leer();
		
		ArrayList<String> resultados = new ArrayList<String>();
		
		int i=1;
		//para cada frase
		for (ArrayList<Long> caso : numeros) {
			
			long n = caso.get(0);
			long k = caso.get(1);
			long a = caso.get(2);
			long b = caso.get(3);
			long c = caso.get(4);
			long r = caso.get(5);
			
			//nuevoArray = new ArrayList<Long>((int)k+10);
			nuevoArrayMapa = new HashMap<Long, Long>((int)k*3);
			
			//calcular numero
			long result = calcularResultado(k,n,a,b,c,r);
			
			System.out.println("Fin caso");
			//imprimir el resultado del test
			resultados.add(resultado(result,i));
			i++;
		}
		
		grabarResultado(resultados);
				
	}	
	


	private long calcularResultado(long k, long n, long a, long b, long c, long r) {
	
		ArrayList<Long> valores = new ArrayList<Long>((int)n);
		
		valores.add(0, a);
		
		for (int i=1; i<k;i++){
			
			long calculo = (b * valores.get(i - 1) + c) % r;
			valores.add(i,calculo);
			
		}

		for (long i=k;i<=((2*k)+5);i++){
			
			long minimo = buscaMinimo(valores,k);
			valores.add((int)i,minimo);
			System.out.println(i-k);
		}			

		//int posicionDesdeRepeticion = calcularPos(valores,k);
		
		int posReal = (int)((k)+(n%(k+1)));
		//long value = valores.get((int)n-1);
		
		return valores.get(posReal);
	}

	/*
	private int calcularPos(ArrayList<Long> valores, long k) {

		
		for (long i=k;i<(k*2);i++){
			if (valores.get((int)i).equals(valores.get((int)i+(int)k+1)))
				return (int)i;
		}
		return 0;
		//return (int)k;
	}
	*/
	
	//ArrayList<Long> nuevoArray;
	HashMap<Long, Long> nuevoArrayMapa;
	
	private long buscaMinimo(ArrayList<Long> valores,long k) {
		
		long ms = System.currentTimeMillis();
		
		//nuevoArray.clear();
		nuevoArrayMapa.clear();
		
		long cantidad = valores.size();
		long saltear = cantidad-k;
		
		long ms2 = System.currentTimeMillis();
		
		for(int i=(int)saltear;i<cantidad;i++){
			//nuevoArray.add(valores.get(i));
			nuevoArrayMapa.put(valores.get(i),valores.get(i));
		}
	
		long ms3 = System.currentTimeMillis();
	
		for (long i=0;;i++){	
			if (nuevoArrayMapa.get(i)==null){
			//if (!nuevoArray.contains(i)){
				long ms4 = System.currentTimeMillis();
				System.out.println("ms1: "+(ms2-ms)+" ms2:"+(ms3-ms2)+ " ms3:"+(ms4-ms3));
				return i;
			}
		}
	
	}

	private String resultado(long number, int i) {
		
		return "Case #"+i+": "+number;	
		
	}	
	
	private ArrayList<ArrayList<Long>> leer() throws IOException {
		
		ArrayList<ArrayList<Long>> lista = new ArrayList<ArrayList<Long>>();
		
		ArrayList<Long> numeros;
				
	    FileInputStream fstream = new FileInputStream("/home/fersca/code/facebook/problem3.txt");
	    
	    // Get the object of DataInputStream
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	    
	    int cantLine = Integer.parseInt(br.readLine());
	    
	    for (int i=1;i<=cantLine;i++){
	    	
	    	numeros = new ArrayList<Long>();
	    	
	    	String[] num1 = br.readLine().split(" ");
	    	numeros.add(new Long(num1[0]));
	    	numeros.add(new Long(num1[1]));
	    	
	    	num1 = br.readLine().split(" ");
	    	
	    	numeros.add(new Long(num1[0]));
	    	numeros.add(new Long(num1[1]));
	    	numeros.add(new Long(num1[2]));
	    	numeros.add(new Long(num1[3]));

	    	lista.add(numeros);
	    }

	    //Close the input stream
	    in.close();
		
	    return lista;
	}
	
	private void grabarResultado(ArrayList<String> resultados) throws FileNotFoundException {
		
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream("/home/fersca/code/facebook/solution3.txt"));
		    for (String string : resultados) {
		    	System.out.println(string);
		    	out.println(string);	
			}
		}
		finally {
		    if (out != null) out.close();
		}
		
	}
}
