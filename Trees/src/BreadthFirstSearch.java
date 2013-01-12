import engine.BFSEngine;
import engine.Node;

/**
 * Fersca implementation of Breaf - First - Search Algorithm
 * 
 */
public class BreadthFirstSearch {


	public static void main(String[] args) {

		BreadthFirstSearch bfs = new BreadthFirstSearch();
		bfs.run();
		
	}
			
	private void run() {
	
		//Agrego el primer nodo a la lista
		Node raiz = new Nodo(0,0);
		raiz.init();
		BFSEngine.procesar(raiz, 6);
		
	}


	
}
