package maps;

import java.util.ArrayList;
import java.util.LinkedList;

public class HashMap {

	public static void main(String[] args) {

		HashMap hm = new HashMap();
		hm.put("hola", "hola");
		String nombre = hm.get("hola");
		System.out.println(nombre);
		hm.delete("hola");

	}

	class Nodo {
		String clave;
		String valor;
	}
	
	//array fijo de 20 elementos
	ArrayList<LinkedList<Nodo>> array = new ArrayList<LinkedList<Nodo>>(20); 
	
	public void put(String clave, String valor){
	
		//calcula el hash de la clave
		int bucket = hash(clave)%20;
		LinkedList<Nodo> list = array.get(bucket);
		
		//crea el arraylist si est‡ vacio
		if (list==null){
			//creo el array list
			list = new LinkedList<Nodo>();
			//le agrego el nodo
			Nodo n = new Nodo();
			n.clave = clave;
			n.valor = valor;
			list.add(n);
			//lo agrego en el bucket que corresponde
			array.add(bucket,list);
		} else {
			//le agrego el nodo
			Nodo n = new Nodo();
			n.clave = clave;
			n.valor = valor;
			list.add(n);			
		}
			
	}

	public String get(String clave){
		
		int bucket = hash(clave)%20;
		
		LinkedList<Nodo> list = array.get(bucket);
		
		for (Nodo nodo : list) {
			if (nodo.clave==clave)
				return nodo.valor;
		}
		
		return null;
	}

	public void delete(String clave){
		
		int bucket = hash(clave)%20;
		
		LinkedList<Nodo> list = array.get(bucket);

		int i = 0;
		for (Nodo nodo : list) {
			if (nodo.clave==clave)
				list.remove(i);
			i++;
		}
		
	}
	
	//Calcula la funci—n de hash de un string
	private int hash(String clave){
		return 0;
	}
	
}
