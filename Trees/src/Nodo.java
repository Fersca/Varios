import java.util.ArrayList;

import engine.AbstractNode;
import engine.Node;

class Nodo extends AbstractNode implements Node{
	
	//Creo un mapa de 5x5
	static String[][] mapa;
	
	int x;
	int y;
	public Nodo(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	//Verifica si es valido el nodo
	public boolean valido(){
		
		//verifica si las dimensiones son correctas
		if (x<0 || x>4 || y<0 || y > 4) return false;
					
		return true;
				
	}
	
	//Verifica si es una solucion valida
	public boolean solucion(){
		if (mapa[x][y]=="X"){
			System.out.println("Encuentro Marca en: X:"+x+" Y:"+y);
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	//Se implementa para comparar contra los que ya estan procesados
	public boolean equals(Object obj) {

		Nodo otro = (Nodo)obj;
		if (this.x==otro.x && this.y==otro.y) return true; else return false;
		
	}

	@Override
	public ArrayList<Node> generarNuevos() {
		
		ArrayList<Node> nuevos = new ArrayList<Node>(); 
		
		Nodo h1 = new Nodo(x+1,y);
		Nodo h2 = new Nodo(x+1,y+1);
		Nodo h3 = new Nodo(x,y+1);
		
		nuevos.add(h1);
		nuevos.add(h2);
		nuevos.add(h3);
		
		return nuevos;
	}

	@Override
	public void init() {
		
		mapa = new String[5][5];  
			
		//Le pongo un par de X en el mapa
		mapa[3][1] = "X";
		mapa[4][4] = "X";
		mapa[4][1] = "X";
					
	}

	@Override
	public void printNode() {

		for(int i = 0;i<5;i++){
			for(int j = 0;j<5;j++){
				if (i==x && j==y){
					System.out.print("*");
				} else {
					System.out.print("-");
				}
			}			
			System.out.println("");
		}

		System.out.println("--------------------");
	}
}