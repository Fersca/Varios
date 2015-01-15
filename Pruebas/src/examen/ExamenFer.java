package examen;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExamenFer {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		ExamenFer ex = new ExamenFer();
		
		//Leer el archivo, llenar la matriz
		ex.leerArchivo();

		//procesa
		ex.run();

	}

	public static ArrayList<ArrayList<Integer>> matriz = new ArrayList<ArrayList<Integer>>();
	
	public static List<Integer> repetidos = Collections.synchronizedList(new ArrayList<Integer>());

	ExecutorService executor;

	public Integer run() throws Exception {
		
		//crea el pool the threads
		executor = Executors.newFixedThreadPool(5);
						
		//por cada fila, busco el más repetido
		buscaRepetidosFila();
		
		//por cada columna busco el más repetido
		buscaRepetidosColumna();
		
        executor.shutdown();
        while (!executor.isTerminated()) {
        	System.out.println("Esperando.....");
        	//Thread.sleep(100);
        }
        System.out.println("Finished all threads");		
		
		//sumar todo y devolver el resultado
		int suma = 0;
		for (Integer numero : repetidos) {
			suma = suma + numero;
		}
		
		//graba el archivo
		grabaResultado(suma);
		
		System.out.println("Resultado: "+suma);
		
		return suma;
		
	}
	
	private void grabaResultado(int suma) throws Exception {
		PrintWriter writer = new PrintWriter("resukt.txt", "UTF-8");
		writer.println("Resultado: "+suma);
		writer.close();
	}

	private void buscaRepetidosColumna() {

		for (int i = 0;i<matriz.size();i++) {
			
			//creo la columna
			ArrayList<Integer> col = new ArrayList<Integer>();
			for (int k=0;k<matriz.size();k++) {
				Integer num = matriz.get(k).get(i);
				col.add(num);
			}

			Procesa pr = new Procesa();
			pr.col = col;
            executor.execute(pr);

		}
				
	}


	private void buscaRepetidosFila() {

		//recorro cada fila
		for (ArrayList<Integer> fila : matriz) {
			
			Procesa pr = new Procesa();
			pr.col = fila;
            executor.execute(pr);

		}
		
	}
	
	private void leerArchivo() throws Exception {

		ArrayList<Integer> numbers;
		
		for (String line : Files.readAllLines(Paths.get("/datos/matriz.txt"))) {
			numbers = new ArrayList<Integer>();
		    for (String part : line.split("\\s+")) {
		        Integer i = Integer.valueOf(part);
		        numbers.add(i);
		    }
		    matriz.add(numbers);
		}		
						
	}

}
