import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class CardGame {

	public static void main(String[] args) throws IOException {
		CardGame cg = new CardGame();
		cg.run();
	}

	private void run() throws IOException {

		//leer el archivo
		ArrayList<ArrayList<String>> cases = leer();
		
		ArrayList<String> resultados = new ArrayList<String>();
		
		int i=1;
		//para cada Caso
		for (ArrayList<String> caso : cases) {
			
			String[] linea1 = caso.get(0).split(" ");
			
			int n = Integer.parseInt(linea1[0]);
			int k = Integer.parseInt(linea1[1]);
			
			int[] a = new int[n];
			
			String[] linea2 = caso.get(1).split(" ");
			int cont=0;
			for (String numero : linea2) {
				a[cont]= Integer.parseInt(numero);
				cont++;
			}
			
			//calcular numero
			long result = calcularResultado(n,k,a);
			
			System.out.println("Fin caso");
			
			//imprimir el resultado del test
			resultados.add(resultado(result,i));
			i++;
		}
		
		grabarResultado(resultados);
				
	}	
	
	int[] arrayConUnos;

	
	private long calcularResultado(int n, int k, int[] a) {
		
		int suma=0;
		
		arrayConUnos = new int[k];
		
		for (int o=0;o<k;o++){
			arrayConUnos[o]=o;
		}
		
		//cuento de 0 a N
		for (;;){
			
			//me guardo el maximo de la combinacion
			int maximo=0;
										
			//recorro el arrayconUnos para ver la posicion de cada uno en A
			for(int j=0;j<k;j++){
				
				if (a[arrayConUnos[j]]>maximo){
					maximo=a[arrayConUnos[j]];
				}
				
			}	

			//sumo el maximo al total de ese maso
			suma = suma+maximo;
			
			if (siguientePos(n,k))
				break;
					
		}
		
		return suma%1000000007;
	}

	private boolean siguientePos(int n, int k) {
		
		int cant=0;
		
		for (int i=k-1;i>=0;i--){
			
			if (arrayConUnos[i]<(n-1-cant)){
				arrayConUnos[i]=arrayConUnos[i]+1;
				int c1=1;
				for (int j=i+1;j<=k-1;j++){
					arrayConUnos[j]=arrayConUnos[i]+c1;
					c1++;
				}
				return false;
			}

			cant++;
		}
		
		
		return true;
	}

	private String resultado(long number, int i) {
		
		return "Case #"+i+": "+number;	
		
	}	
	
	private ArrayList<ArrayList<String>> leer() throws IOException {
		
		ArrayList<ArrayList<String>> lista = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> numeros;
				
	    FileInputStream fstream = new FileInputStream("/home/fersca/code/facebook/problem1B.txt");
	    
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	    
	    int cantLine = Integer.parseInt(br.readLine());
	    
	    for (int i=1;i<=cantLine;i++){
	    	
	    	numeros = new ArrayList<String>();
	    	
	    	String num1 = br.readLine();
	    	numeros.add(num1);
	    	
	    	num1 = br.readLine();
	    	
	    	numeros.add(num1);

	    	lista.add(numeros);
	    }

	    in.close();
		
	    return lista;
	}
	
	private void grabarResultado(ArrayList<String> resultados) throws FileNotFoundException {
		
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream("/home/fersca/code/facebook/solution1B.txt"));
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
