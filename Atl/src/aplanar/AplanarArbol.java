package aplanar;

import java.util.List;

public interface AplanarArbol<T> {

	/**
	 * 
	 * @param tree the Tree to flatten
	 * @return a list containing all the leaf values in t, in left-to-right order
	 * @throws IllegalArgumentException if t is null
	 */
	List<T> aplanarEnOrden(Arbol<T> tree);
	
}
