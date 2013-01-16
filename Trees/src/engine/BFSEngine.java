package engine;
import java.util.ArrayList;


/**
 * Engine para procesar un arbol con el algoritm Breadth First
 * @author fersca
 *
 */
public class BFSEngine {

	public boolean guardaHistoria = true;
	public boolean printResult = true;
	
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
				
				//si esta seteago para guarda, hace la copia de la historia
				if (guardaHistoria){
					nuevo.setHistorial(nodo);
				}
				
				//Verifico si el nodo es valido, sino lo descarto
				if (nuevo.valido() && !procesados.contains(nuevo)){
					
					//Verifico si es una solucion valida
					if (nuevo.solucion() && printResult){
						nuevo.print();
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
