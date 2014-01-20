package searching;

import java.util.ArrayList;
import java.util.LinkedList;

public class Dijkstra {

	//matriz de adyasencia
	ArrayList<Edge> edges;
	
	public LinkedList<Vertex> calculate(Vertex root){
	
		//creo la cola para visitar
		LinkedList<Vertex> queue = new LinkedList<Vertex>();

		//creo la cola de visitados
		LinkedList<Vertex> visited = new LinkedList<Vertex>();

		//encolo el root en ambos
		queue.add(root);
		
		//mientras tenga por visitar
		while(!queue.isEmpty()){
			
			//obtengo el vertice a visitar
			Vertex v1 = queue.poll();
						
			//obtengo las aristas
			ArrayList<Edge> edges= getEdges(v1); 
			
			//para cada una obtengo su vertice
			for (Edge edge : edges) {
				
				//obtengo el vertice
				Vertex v2 = nextVertex(edge, v1);
				
				//distancia desde inicio
				int desdeInicio = v1.cost+edge.dist;
				
				//si la distancia desde el root al elemento es menor a la que ya tiene seteada
				if (desdeInicio<v2.cost){
					//cambia el costo, asigna un nuevo elemento previo y lo encola para procesar de nuevo
					v2.cost=desdeInicio;
					v2.previous=v1;
					//si ya no fue visitado, se encola para procesar
					if (!contiene(visited,v2))
						queue.add(v2);
				}
				
			}
			//agrega el nodo procesado a los visitados, para no procesar de nuevo
			visited.add(v1);
		}
		
		return visited;
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
		
	public static void main(String[] args){
		
		Dijkstra sp = new Dijkstra();
		
		//Creo la matriz 
		
		//vertices
		Vertex v1 = new Vertex("V1");
		Vertex v2 = new Vertex("V2");
		Vertex v3 = new Vertex("V3");
		Vertex v4 = new Vertex("V4");
		Vertex v5 = new Vertex("V5");
		Vertex v6 = new Vertex("V6");
		Vertex v7 = new Vertex("V7");

		//setea los costos en maximos para ir minimizando
		v1.cost=0;
		v2.cost=Integer.MAX_VALUE;
		v3.cost=Integer.MAX_VALUE;
		v4.cost=Integer.MAX_VALUE;
		v5.cost=Integer.MAX_VALUE;
		v6.cost=Integer.MAX_VALUE;
		v7.cost=Integer.MAX_VALUE;
		
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
		LinkedList<Vertex> solutions = sp.calculate(v1); 
		for (Vertex v : solutions) {
			System.out.println("Node: "+v.value+": "+v.cost);
			System.out.print("Recorrido:");
			for(Vertex h=v;h!=null;h=h.previous){
				System.out.print(h.value+"-");
			}
			System.out.println("");
			
		}
	}
	
}
