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

public class Smiles2 {

	public static void main(String[] args) throws IOException {
		Smiles2 sm = new Smiles2();
		sm.run();
	}

	private void run() throws IOException {

		//leer el archivo
		ArrayList<String> frases = leer();
		ArrayList<String> resultados = new ArrayList<String>();
		int i=1;
		//para cada frase
		for (String s : frases) {
			//calcular numero
			boolean result = calcularResultado(s);
			
			//imprimir el resultado del test
			resultados.add(resultado(result,i));
			i++;
		}
		
		grabarResultado(resultados);
				
	}
	
	private void grabarResultado(ArrayList<String> resultados) throws FileNotFoundException {
		
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream("/home/fersca/code/facebook/solution2.txt"));
		    for (String string : resultados) {
		    	System.out.println(string);
		    	out.println(string);	
			}
		}
		finally {
		    if (out != null) out.close();
		}
		
	}

	private boolean calcularResultado(String s) {

		if (s.equals(""))
			return true;
		
		if (!letrasCorrectas(s))
			return false;

		if (balanceado(s,""))
			return true;
		
		//Agrego el primer nodo a la lista
		Node raiz = new SmileNodo(s);
		BFSEngine engine = new BFSEngine();
		engine.procesar(raiz, 100);		
		
		return engine.correcto;

		
	}
		
	String permitidas = "abcdefghijklmnopqrstuvwxyz :()";
	
	private boolean letrasCorrectas(String sinSmiles) {
		
		for (int i=0;i<sinSmiles.length();i++){
			if (!permitidas.contains(""+sinSmiles.charAt(i))){
				return false;
			}
		}
		
		return true;
	}	
	
	private String resultado(boolean result, int i) {
		
		if (result){
			return "Case #"+i+": YES";	
		} else {
			return "Case #"+i+": NO";
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
	
	static String abre  = "(";
	static String cierra = ")";

	static boolean isOpen(char ch) {
	    return abre.indexOf(ch) != -1;
	}
	static boolean isClose(char ch) {
	    return cierra.indexOf(ch) != -1;
	}
	static boolean isMatching(char chOpen, char chClose) {
	    return abre.indexOf(chOpen) == cierra.indexOf(chClose);
	}

	static boolean balanceado(String input, String stack) {
	    return
	        input.isEmpty() ?
	            stack.isEmpty()
	        : isOpen(input.charAt(0)) ?
	            balanceado(input.substring(1), input.charAt(0) + stack)
	        : isClose(input.charAt(0)) ?
	            !stack.isEmpty() && isMatching(stack.charAt(0), input.charAt(0))
	              && balanceado(input.substring(1), stack.substring(1))
	        : balanceado(input.substring(1), stack);
	}	
	
	
	/////////////////////////////
	//// Graph engine
	/////////////////////////////
	class SmileNodo implements Node{
		
		String s;
		public SmileNodo(String s){
			this.s=s;
		}
		
		//Verifica si es una solucion valida
		public boolean solucion(){
			return Smiles2.balanceado(this.s, "");
		}
		
		
		@Override
		//Se implementa para comparar contra los que ya estan procesados
		public boolean equals(Object obj) {

			SmileNodo otro = (SmileNodo)obj;
			if (this.s.equals(otro.s)) return true; else return false;
			
		}

		@Override
		public ArrayList<Node> generarNuevos() {
			
			ArrayList<Node> nuevos = new ArrayList<Node>(); 

			String s1 = this.s;
			s1 = s1.replaceFirst(":\\)", " ");
			
			String s2 = this.s;
			s2 = s1.replaceFirst(":\\(", " ");
			
			
			SmileNodo h1 = new SmileNodo(s1);
			SmileNodo h2 = new SmileNodo(s2);
			
			nuevos.add(h1);
			nuevos.add(h2);
			
			return nuevos;
		}
		
	}	
	
	public interface Node {
				
		//Verifica si es una solucion valida
		public boolean solucion();
		
		//Genera la lista de nodos hijos
		public ArrayList<Node> generarNuevos();
		
		@Override
		//Se implementa para comparar contra los que ya estan procesados
		public boolean equals(Object obj);

	}
	
	public class BFSEngine {

		public boolean correcto=false;
		
		//Esto se puede mejorar, poniendo un mapa, para no recorrer todo el array cuando se hace el contains.
		private ArrayList<Node> procesados = new ArrayList<Node>();
		
		/**
		 * Procesa el arbol desde el nodo raiz
		 * @param raiz
		 * @param nivel
		 */
		public void procesar(Node raiz, int nivel) {
			
			//Creo la lista de nodos vacia
			ArrayList<Node> nodos = new ArrayList<Node>(); 
			nodos.add(raiz);
			
			//Voy recorriendo cada uno de los niveles y chequeando los nodos de esos niveles
			for(int i=0;i<nivel;i++){

				//Verifica los nodos de los niveles y ademas devuelve el nuevo nivel al mismo tiempo
				nodos = verificarYGenerarNivel(nodos);
				if (nodos.size()==0)
					break;
			}
			
		}	
		
		/**
		 * En base a a lista de nodos, genera los hijos, los verifica y devuele el nuevo nivel
		 * @param nodos
		 * @return
		 */
		public ArrayList<Node> verificarYGenerarNivel(ArrayList<Node> nodos) {

			//para ir guardando los nodos hijos de todo este nivel
			ArrayList<Node> nuevos = new ArrayList<Node>();
			ArrayList<Node> proximoNivel = new ArrayList<Node>();
			
			//Para cada uno de los nodos de este nivel
			for (Node nodo : nodos) {
				
				//vacio la lista de nuevos elementos que ya use seguro para el nodo anterior
				nuevos.clear();
				
				//Genero todos los hijos del nodo
				nuevos = nodo.generarNuevos();
				
				//Proceso cada uno de ellos
				for (Node nuevo : nuevos) {
					
					//Verifico si el nodo es valido, sino lo descarto
					if (!procesados.contains(nuevo)){
						
						//Verifico si es una solucion valida
						if (nuevo.solucion()){
							
							this.correcto=true;
							
							return new ArrayList<Node>();
							
						} else {

							//Verifico que no se haya procesado ya ese nodo, sino se entra en un looop
							procesados.add(nuevo);
							
							//Agrego el nodo a los nodos del nivel para seguir recorriendo a partir de el.
							proximoNivel.add(nuevo);
							
						}
					}				
					
				}					
				
			}
		
			return proximoNivel;
		}	
		
	}	
}
