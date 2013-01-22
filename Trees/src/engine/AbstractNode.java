package engine;

import java.util.ArrayList;

public abstract class AbstractNode {

	private ArrayList<Node> historial = null;
	private int costo;
	
	public int getCosto(){
		return costo;
	}
	public void setCosto(int c){
		costo = c;
	}
	
	public ArrayList<Node> getHistorial(){
		if (this.historial==null)
			this.historial = new ArrayList<Node>();
		
		return this.historial;
	}
	
	public void print(){
		for (Node node : getHistorial()) {
			node.printNode();
		}
		printNode();
	}
	
	public int costoTotal(){
		int total=0;
		for (Node node : getHistorial()) {
			total = total + node.getCosto();
		}		
		return total + getCosto();
	}
	
	public abstract void printNode();
	
	public void setHistorial(Node nodo) {
		
		ArrayList<Node> h = new ArrayList<Node>();
		h.addAll(nodo.getHistorial());
		h.add(nodo);
		historial = h;
		
	}
}
