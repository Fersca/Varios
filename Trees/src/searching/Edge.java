package searching;

//clase que representa una arista
public class Edge {
	Vertex vertex1;
	Vertex vertex2;
	int dist;
	public Edge(Vertex v1, Vertex v2, int dist){
		vertex1=v1;
		vertex2=v2;
		this.dist=dist;
	}
}
