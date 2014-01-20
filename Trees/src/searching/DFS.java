package searching;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFS {

	//matriz de adyasencia
	ArrayList<Edge> edges;
	
	private boolean run(Vertex root){
						
		//verify if vertex is a solution, finish
		if (root.value=="V7"){
			System.out.println(root.value);
			return true;
		} 
					
		//if not, get edges from vertex
		ArrayList<Edge> edges = getEdges(root);
		
		//for each edge process into it
		for (Edge e : edges){
			//get future vertex
			Vertex v2 = nextVertex(e, root);
			//if it wasnt visited previously			
			if (!contiene(root.history,v2)){
				//add root history to new node
				for (Vertex v:root.history){
					v2.history.add(v);
				}
				//add root to the new node
				v2.history.add(root);
				//process vertex calling recursively to DFS
				if (run(v2)){
					System.out.println(root.value);
					return true;
				}
			}
		}
		return false;
		
	}
	
	private boolean contiene(LinkedList<Vertex> visited, Vertex v2) {

		for (Vertex vertex : visited) {
			if (vertex.value.equals(v2.value))
				return true;
		}
		return false;
	}

	private ArrayList<Edge> getEdges(Vertex v1) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Edge e : this.edges) {
			if (e.vertex1.value.equals(v1.value)||e.vertex2.value.equals(v1.value))
				edges.add(e);
		}
		return edges;
	}

	private Vertex nextVertex(Edge edge, Vertex v1) {
		if (edge.vertex1.value.equals(v1.value)) 
			return edge.vertex2;
		else 
			return edge.vertex1;
	}

	public static void main(String[] args){
		
		DFS d = new DFS();
		
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
		
		d.edges=edges;
		d.run(v1);
		/*
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
		*/
	}	
	
}
