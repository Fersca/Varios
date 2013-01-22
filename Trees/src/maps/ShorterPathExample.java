package maps;

import engine.Node;
import engine.ShorterPathEngine;

public class ShorterPathExample {


	public static void main(String[] args) {

		ShorterPathExample sp = new ShorterPathExample();
		sp.run();
		
	}
			
	private void run() {
			
		//Agrego el primer nodo a la lista
		Node raiz = new SPNode(0); //se pone el numero del nodo desde el cual se parte
		raiz.init();
		ShorterPathEngine engine = new ShorterPathEngine();
		engine.procesar(raiz, 6);
		Node solucion= engine.mejorSolucion;
		solucion.print();
		System.out.println("Costo: "+solucion.costoTotal());
	}
		
	
}
