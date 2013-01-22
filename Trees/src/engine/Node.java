package engine;
import java.util.ArrayList;

public interface Node {
	
	//devuelve el historial de nodos
	public void setHistorial(Node nodo);
	public ArrayList<Node> getHistorial();
	
	//Verifica si es un nodo valido
	public boolean valido();
	
	//Verifica si es una solucion valida
	public boolean solucion();
	
	//Genera la lista de nodos hijos
	public ArrayList<Node> generarNuevos();
	
	@Override
	//Se implementa para comparar contra los que ya estan procesados
	public boolean equals(Object obj);

	//Inicializa el nodo, por ejemplo los datos para saber si es solucion.
	public void init();

	//Imprime al nodo
	public void print();
	public void printNode();
	public int costoTotal();
	public int getCosto();
	public void setCosto(int c);
}
