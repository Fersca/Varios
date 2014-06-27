package aplanar;

import java.util.LinkedList;
import java.util.List;

public class MiAplanarArbol<T> implements AplanarArbol<T>{

	public static void main(String[] args) {

		//Build the tree
        Arbol<String> root = new Arbol.Node<String>(Arbol.Leaf.leaf("1"), Arbol.Node.tree("5", "4", "9"), Arbol.Leaf.leaf("6"));
        
        MiAplanarArbol<String> flattenTree = new MiAplanarArbol<String>();
        
        System.out.println("Flattened tree: " + flattenTree.aplanarEnOrden(root));		
				
	}

	public List<T> aplanarEnOrden(Arbol<T> tree){
		
		//Checks if the tree is null
		if (tree == null)
		    throw new IllegalArgumentException();
		
		//Check if the node is a leaf, if its left is leaf
		if (tree.get().isLeft()) {
			//Create an array with the only element
			List<T> array = new LinkedList<T>();
			array.add(tree.get().ifLeft(left));
			//return the array 
			return array;
		} else {
			return tree.get().ifRight(right);
		}	    
		
	}

	/**
	 * Function to go to the left on the tree
	 */
	private final Function<TripleNodo<Arbol<T>>, List<T>> right = new Function<TripleNodo<Arbol<T>>, List<T>>() {
	    
		@Override
	    public List<T> apply(TripleNodo<Arbol<T>> element){
			
			//create the final array
	        List<T> array = new LinkedList<T>();
	        
	        //get the elements form each branch
	        List<T> lefts = aplanarEnOrden(element.left());
	        List<T> middles = aplanarEnOrden(element.middle());
	        List<T> rights = aplanarEnOrden(element.right());
	        
	        //add all elements to the final array
	        for (T t : lefts) array.add(t);
	        for (T t : middles) array.add(t);
	        for (T t : rights) array.add(t);
	        
	        //return the array
	        return array;
	    }
	};	

	/**
	 * Function to go to the left on the tree
	 */
	private final Function<T, T> left = new Function<T, T>(){
	    @Override
	    public T apply(T element){
	    	//return the only element of the node
	        return element;
	    }
	};
	
	
}
