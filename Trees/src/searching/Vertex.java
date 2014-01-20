package searching;

import java.util.LinkedList;

//clase que representa un vértice
public class Vertex {
	String value;
	public Vertex(String value){
		this.value=value;
	}
	//used in shortest path to store the history 
	LinkedList<Vertex> history = new LinkedList<Vertex>();
	
	//used in dijkstra to store the previous vertex in the path
	Vertex previous;
	
	//cost to actual vertex
	int cost;
	
}
