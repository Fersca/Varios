package maps;

/**
 * Hashmap de fersca
 * @author fscasserra
 *
 */
public class HashMap<K,V> {
		
	//nodo que contiene los elementos del mapa
	class Nodo<K2,V2> {
		K2 clave;
		V2 valor;
		Nodo<K2,V2> next;
	}

	Nodo<K,V>[] array;
	
	@SuppressWarnings("unchecked")
	public HashMap(){
		this.array = (Nodo<K,V>[])new Nodo[10];
	}
	@SuppressWarnings("unchecked")
	public HashMap(int initialCapacity){
		this.array = (Nodo<K,V>[])new Nodo[initialCapacity];
	}
		
	/**
	 * Agrega un elemento al mapa
	 * @param clave
	 * @param valor
	 */
	public void put(K clave, V valor){
	
		if (clave==null) return;
		if (valor==null) return;
		
		//calcula el hash de la clave
		int bucket = hash(clave)%array.length;
		Nodo<K,V> n = array[bucket];
		
		//crea el arraylist si est‡ vacio
		if (n==null){
			//le agrego el nodo
			n = new Nodo<K,V>();
			n.clave = clave;
			n.valor = valor;
			array[bucket] = n;
		} else {
			//le agrego el nodo
			Nodo<K,V> n2 = new Nodo<K,V>();
			n2.clave = clave;
			n2.valor = valor;
			while(n.next!=null){
				n = n.next;		
			}
			n.next=n2;
		}
			
	}

	/**
	 * Obtiene el elemento definido con la clave especificada
	 * @param clave
	 * @return
	 */
	public V get(K clave){
		
		if (clave==null) return null;
		
		//encuentro el bucket
		int bucket = hash(clave)%array.length;
		
		//obtengo el nodo
		Nodo<K,V> n = array[bucket];
				
		//mientras haya objeto verifico la clave
		while(n!=null){
			if (n.clave.equals(clave))
				return n.valor;
			n=n.next;
		}
		
		return null;
		
	}

	/**
	 * Elimina el elemento con la clave especificada
	 * @param clave
	 */
	public void delete(K clave){
		
		if (clave==null) return;
		
		//encuentro el bucket
		int bucket = hash(clave)%array.length;
		
		//obtengo el nodo
		Nodo<K,V> n = array[bucket];
		Nodo<K,V> ant = null;
		
		//mientras haya objeto verifico la clave
		while(n!=null){
			if (n.clave.equals(clave)){
				if (ant!=null){
					ant.next=n.next;					
				} else {
					array[bucket]=n.next;
				}
				return;
			}
			ant = n;
			n=n.next;
		}
		
	}
	
	/**
	 * Devuelve la cantidad de elementos de todo el mapa
	 * @return
	 */
	public int getCountElements(){
		
		int i=0;
		for (Nodo<K,V> nodo : array) {
			if (nodo!=null){
				do {
					i++;	
					nodo=nodo.next;
				} while ((nodo!=null));
			}
		}
		return i;
	}

	//Calcula la funci—n de hash de un string
	private int hash(K clave){
		return clave.hashCode();
	}
	
	public static void main(String[] args) {

		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		hm.put("hola", 1);
		hm.put("chau", 2);
		hm.put("fer",  3);
		hm.put("vale", 4);
		hm.put(null, 5);
		Integer nombre = hm.get("hola");
		System.out.println(nombre);
		nombre = hm.get("chau");
		nombre = hm.get(null);
		System.out.println(nombre);
		hm.delete("hola");
		System.out.println(hm.getCountElements());
		hm.delete("fer");
		System.out.println(hm.getCountElements());

	}

	
}
