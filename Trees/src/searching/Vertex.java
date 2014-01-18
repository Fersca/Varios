package searching;

//clase que representa un vértice
public class Vertex implements Comparable<Vertex>{
	Object value;
	public Vertex(Object value){
		this.value=value;
	}
	@Override
	public int compareTo(Vertex arg0) {
		if (arg0.value.equals(this.value)) return 0; else return 1;
	}
}
