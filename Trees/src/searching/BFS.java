package searching;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public class BFS {
				
	//matriz de adyasencia
	ArrayList<Edge> edges;
	
	public Vertex find(Vertex root, Vertex solution){
	
		//creo la cola para visitar
		LinkedList<Vertex> queue = new LinkedList<Vertex>();
		
		//me guardo los vertices ya visitados
		Set<Vertex> visited = new TreeSet<Vertex>();

		//encolo el root en ambos
		queue.add(root);
		visited.add(root);
		
		//mientras tenga por visitar
		while(!queue.isEmpty()){
			
			//obtengo el vertice a visitar
			Vertex v1 = queue.poll();
			
			//verifico si es la condici—n final
			if (verifConfition(v1, solution)){
				return v1;
			}
			
			//obtengo las aristas
			ArrayList<Edge> edges= getEdges(v1); 
			
			//para cada una obtengo su vertice
			for (Edge edge : edges) {
				//obtengo el vertice
				Vertex v2 = nextVertex(edge, v1);
				//lo agrego a la cola a visitar si no estaba visitado
				if (!visited.contains(v2))
					queue.add(v2);
			}
			
		}
		
		//devuelvo null si ninguno cupli— la condici—n
		return null;
			
	}

	private Vertex nextVertex(Edge edge, Vertex v1) {
		if (edge.vertex1.equals(v1)) 
			return edge.vertex2;
		else 
			return edge.vertex1;
	}

	private ArrayList<Edge> getEdges(Vertex v1) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Edge e : this.edges) {
			if (e.vertex1.equals(v1)||e.vertex2.equals(v1))
				edges.add(e);
		}
		return edges;
	}

	//verifica si el vertice es el que buscamos
	private boolean verifConfition(Vertex v1, Vertex solution) {
		return (v1.equals(solution));
	}
		
	public static void main(String[] args){
		
		BFS bfs = new BFS();
		
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
		
		bfs.edges=edges;
		Vertex fin = bfs.find(v1,v7);
		System.out.println("Fin: "+fin.value);
	}
	
}