import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class FinMin4 {

	public static void main(String[] args) throws IOException {
		FinMin4 sm = new FinMin4();
		sm.run();
	}

	private void run() throws IOException {

		System.out.println("Version array");
		
		//leer el archivo
		ArrayList<ArrayList<Long>> numeros = leer();
		
		ArrayList<String> resultados = new ArrayList<String>();
		
		int i=1;
		arrayInt = new boolean[1000000100];
		
		//para cada frase
		for (ArrayList<Long> caso : numeros) {
			
			System.gc();
			
			long n = caso.get(0);
			long k = caso.get(1);
			long a = caso.get(2);
			long b = caso.get(3);
			long c = caso.get(4);
			long r = caso.get(5);
			
			for (int l=0;l<1000000100;l++)
				arrayInt[l] = false;
			
			canti=0;
			
			//calcular numero
			long result = calcularResultado(k,n,a,b,c,r,i);
			
			System.out.println("Fin caso"+i);
			//imprimir el resultado del test
			resultados.add(resultado(result,i));
			i++;
		}
		
		grabarResultado(resultados);
				
	}	
	


	private long calcularResultado(long k, long n, long a, long b, long c, long r, int test) {
	
		int[] valores;
		
		valores = new int[(int)(2*k)+15];
		
		/*
		if (  ((2*k)+5) > ((int)(n*1.1)) ){
			valores = new int[(int)(2*k)+15];
		} else {
			valores = new int[(int)(n*1.1)];	
		}
		*/
		
		valores[0]=(int)a;
		canti++;
		
		for (int i=1; i<k;i++){
			
			long calculo = (b * valores[i-1] + c) % r;
			valores[i] =(int)calculo;
			canti++;
			
		}

		for (long i=k;i<=((2*k)+5);i++){
			
			long minimo = buscaMinimo(valores,k, test);
			valores[(int)i]=(int)minimo;
			canti++;
			if ((i-k)%100==0)
				System.out.println(i-k);
		}			
		
		int posReal = (int)((k)+(n%(k+1)));
		
		return valores[posReal];
	}

	boolean[] arrayInt;
	int canti=0;
	private long buscaMinimo(int[] valores,long k, int test) {
		
		long cantidad = canti;
		long saltear = cantidad-k;
		
		int[] usados = new int[(int)k+10];
		
		int x = 0;
		for(int i=(int)saltear;i<cantidad;i++){

			int pus = (int)(long)valores[i];
			
			//arrayInt[pus]=(byte)test;
			arrayInt[pus]=true;
			usados[x]=pus;
		}
	
	
		for (long i=0;;i++){	
			if (arrayInt[(int)i]!=true){

				for(int y=0;y<k+10;y++){
					//arrayInt[y]=-1;
					arrayInt[y]=false;
					usados[y]=-1;
				}

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
