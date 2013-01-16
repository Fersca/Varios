package rubik;

import engine.BFSEngine;
import engine.Node;

public class RubikCubeSolver {

	public static void main(String[] args) {

		RubikCubeSolver bfs = new RubikCubeSolver();
		bfs.run();
		
	}
			
	private void run() {
	
		//Agrego el primer nodo a la lista
		Node raiz = new Cube();
		raiz.init();
		BFSEngine engine = new BFSEngine();
		engine.procesar(raiz, 6);
		
	}

}
