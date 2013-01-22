package maps;

import java.util.ArrayList;

import engine.AbstractNode;
import engine.Node;

public class SPNode extends AbstractNode implements Node {

	private static int[][] matriz;
	
	int numero;
	int costo;

	@Override
	public boolean equals(Object obj) {

		SPNode otro = (SPNode)obj;
		if (
			this.numero==otro.numero
		) 
			return true; 
		else 
			return false;
		
	}

	public SPNode(int num){
		numero=num;
	}
	
	@Override
	public boolean valido() {
		return true;
	}

	@Override
	public boolean solucion() {
		if (numero==7)
			return true;
		else
			return false;
	}

	@Override
	public ArrayList<Node> generarNuevos() {

		ArrayList<Node> futurosNodos = new ArrayList<Node>();
		
		for(int i=0;i<8;i++){
			if (matriz[numero][i]>0){
				Node node = new SPNode(i);
				node.setCosto(matriz[numero][i]);
				futurosNodos.add(node);
			}
		}
		
		return futurosNodos;
		
	}

	@Override
	public void init() {
		
		matriz = new int[8][8];
		matriz[0][1] = 3;
		matriz[0][2] = 5;
		matriz[1][0] = 3;
		matriz[1][2] = 6;
		matriz[1][3] = 2;
		matriz[1][4] = 7;
		matriz[2][0] = 5;
		matriz[2][1] = 6;
		matriz[2][4] = 8;
		matriz[2][5] = 9;
		matriz[3][1] = 2;
		matriz[3][4] = 9;
		matriz[3][6] = 1;
		matriz[4][1] = 7;
		matriz[4][2] = 8;
		matriz[4][3] = 9;
		matriz[4][5] = 3;
		matriz[4][6] = 9;
		matriz[4][7] = 3;
		matriz[5][2] = 9;
		matriz[5][4] = 3;
		matriz[5][7] = 6;
		matriz[6][3] = 1;
		matriz[6][4] = 9;
		matriz[6][7] = 4;
		matriz[7][4] = 3;
		matriz[7][5] = 6;
		matriz[7][6] = 4;
		
	}

	@Override
	public void printNode() {
		System.out.println("Nodo: "+numero);
		
	}

}
