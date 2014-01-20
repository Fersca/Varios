package searching;

import java.util.ArrayList;
import java.util.LinkedList;

public class ShortestPath {

	//matriz de adyasencia
	ArrayList<Edge> edges;
	
	public LinkedList<Vertex> find(Vertex root, Vertex solution){
	
		//creo la cola para visitar
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		//lista de soluciones
		LinkedList<Vertex> solutions = new LinkedList<Vertex>();
		
		//encolo el root en ambos
		queue.add(root);
		
		//mientras tenga por visitar
		while(!queue.isEmpty()){
			
			//obtengo el vertice a visitar
			Vertex v1 = queue.poll();
			
			//verifico si es la condici—n final
			if (verifConfition(v1, solution)){
				solutions.add(v1);
				continue;
			} 
			
			//obtengo las aristas
			ArrayList<Edge> edges= getEdges(v1); 
			
			//para cada una obtengo su vertice
			for (Edge edge : edges) {
				//obtengo el vertice
				Vertex v2 = nextVertex(edge, v1);
				//lo agrego a la cola a visitar si no estaba visitado
				if (!contiene(v1.history,v2)){
					Vertex v2clone = new Vertex(v2.value);
					//copia la historia del nodo
					for (Vertex vh : v1.history) {
						v2clone.history.add(vh);
					}
					v2clone.history.add(v1);
					v2clone.cost=edge.dist;
					queue.add(v2clone);
				}									
			}
			//limpia la historia del nodo anterior para hacer espacio
			v1.history=null;
			
		}
		
		//devuelvo null si ninguno cupli— la condici—n
		return solutions;
			
	}

	private boolean contiene(LinkedList<Vertex> visited, Vertex v2) {

		for (Vertex vertex : visited) {
			if (vertex.value.equals(v2.value))
				return true;
		}
		return false;
	}

	private Vertex nextVertex(Edge edge, Vertex v1) {
		if (edge.vertex1.value.equals(v1.value)) 
			return edge.vertex2;
		else 
			return edge.vertex1;
	}

	private ArrayList<Edge> getEdges(Vertex v1) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Edge e : this.edges) {
			if (e.vertex1.value.equals(v1.value)||e.vertex2.value.equals(v1.value))
				edges.add(e);
		}
		return edges;
	}

	//verifica si el vertice es el que buscamos
	private boolean verifConfition(Vertex v1, Vertex solution) {
		return (v1.value.equals(solution.value));
	}
		
	public static void main(String[] args){
		
		ShortestPath sp = new ShortestPath();
		
		//Creo la matriz 
		
		//vertices
		Vertex v1 = new Vertex("V1");
		Vertex v2 = new Vertex("V2");
		Vertex v3 = new Vertex("V3");
		Vertex v4 = new Vertex("V4");
		Vertex v5 = new Vertex("V5");
		Vertex v6 = new Vertex("V6");
		Vertex v7 = new Vertex("V7");
		
		//aristas
		
		Edge e1 = new Edge(v1,v2,20);
		Edge e2 = new Edge(v1,v3,10);
		Edge e3 = new Edge(v2,v4,30);
		Edge e4 = new Edge(v3,v4,40);
		Edge e5 = new Edge(v4,v5,60);
		Edge e6 = new Edge(v4,v6,70);
		Edge e7 = new Edge(v3,v5,50);
		Edge e8 = new Edge(v5,v7,80);
		Edge e9 = new Edge(v6,v7,90);
		
		/*
		 * 
		 *           v2             v6    
		 *          -   -          -  -
		 *       -20-    -30-   -70-   -90-
 		 *       -          -  -          -
		 *    v1             v4            v7
		 *       -          -  -          -
   		 *       -10-   -40-    -60-   -80-
		 *          -  -           -  -
		 *           v3  ----50---- v5
		 *
		 * 
		 */
		
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		edges.add(e1);
		edges.add(e2);
		edges.add(e3);
		edges.add(e4);
		edges.add(e5);
		edges.add(e6);
		edges.add(e7);
		edges.add(e8);
		edges.add(e9);
		
		sp.edges=edges;
		LinkedList<Vertex> solutions = sp.find(v1,v7);
		for (Vertex v : solutions) {
			System.out.println("Fin: ");
			int totalCost=0;
			for (Vertex vertex : v.history) {
				System.out.println(" -- Node: "+vertex.value);
				totalCost=totalCost+vertex.cost;
			}
			totalCost=totalCost+v.cost;
			System.out.println(" -- Cost: "+totalCost);
		}
	}
	
}
