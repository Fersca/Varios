import java.util.ArrayList;

/*
 * Fersca implementation of Breaf - First - Search Algorithm
 * 
 */
public class BreadthFirstSearch {


	public static void main(String[] args) {

		BreadthFirstSearch bfs = new BreadthFirstSearch();
		bfs.run();
		
	}
	
	private class Nodo {
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
			
			//Verifica si ya no fue procesado
			if (procesados.contains(this)) return false;
			
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
	}
	
	String[][] mapa;
	
	//Esto se puede mejorar, poniendoun mapa, para no recorrer todo el array cuando se hace el contains.
	ArrayList<Nodo> procesados = new ArrayList<Nodo>();
	
	private void run() {

		//Creo un mapa de 5x5
		mapa = new String[5][5];  
		
		//Le pongo un par de X en el mapa
		mapa[3][1] = "X";
		mapa[4][4] = "X";
		mapa[4][1] = "X";
		
		//Creo la lista de nodos vacia
		ArrayList<Nodo> nodos = new ArrayList<Nodo>(); 
		
		//Agrego el primer nodo a la lista
		Nodo raiz = new Nodo(0,0);
		nodos.add(raiz);
		
		//Voy recorriendo cada uno de los niveles y chequeando los nodos de esos niveles
		for(int i=0;i<6;i++){

			//Verifica los nodos de los niveles y ademas devuelve el nuevo nivel al mismo tiempo
			nodos = VerificarYGenerarNivel(nodos);
			
		}
		
	}

	private ArrayList<Nodo> VerificarYGenerarNivel(ArrayList<Nodo> nodos) {

		//para ir guardando los nodos hijos de todo este nivel
		ArrayList<Nodo> nuevos = new ArrayList<Nodo>();
		ArrayList<Nodo> proximoNivel = new ArrayList<Nodo>();
		
		//Para cada uno de los nodos de este nivel
		for (Nodo nodo : nodos) {
			
			//vacio la lista de nuevos elementos que ya use seguro para el nodo anterior
			nuevos.clear();
			
			//Genero todos los hijos del nodo
			Nodo h1 = new Nodo(nodo.x+1,nodo.y);
			Nodo h2 = new Nodo(nodo.x+1,nodo.y+1);
			Nodo h3 = new Nodo(nodo.x,nodo.y+1);
			
			nuevos.add(h1);
			nuevos.add(h2);
			nuevos.add(h3);
			
			//Proceso cada uno de ellos
			for (Nodo nuevo : nuevos) {

				//Verifico si el nodo es valido, sino lo descarto
				if (nuevo.valido()){
					
					//Verifico si es una solucion valida
					nuevo.solucion();

					//Verifico que no se haya procesado ya ese nodo, sino se entra en un looop
					procesados.add(nuevo);
					
					//Agrego el nodo a los nodos del nivel para seguir recorriendo a partir de el.
					proximoNivel.add(nuevo);
				}				
				
			}					
			
		}
	
		return proximoNivel;
	}
	
}
