package aplanar;

public interface Arbol<T> {
	
	Alguno<T, TripleNodo<Arbol<T>>> get();


	static final class Leaf<T> implements Arbol<T> {
	 
		public static <T> Leaf<T> leaf (T value) {
			return new Leaf<T>(value);
		}
		
		private final T t;
		
		public Leaf(T t) {
			this.t = t;
		}
		
		@Override
		public Alguno<T, TripleNodo<Arbol<T>>> get() {
			return Alguno.left(t);
		}
	}
		
	static final class Node<T> implements Arbol<T> {
		public static <T> Arbol<T> tree (T left, T middle, T right) {
			return new Node<T>(Leaf.leaf(left), Leaf.leaf(middle), Leaf.leaf(right));
		}

		private final TripleNodo<Arbol<T>> branches;

		public Node(Arbol<T> left, Arbol<T> middle, Arbol<T> right) {
			this.branches = new TripleNodo<Arbol<T>>(left, middle, right);
		}
	
		@Override
		public Alguno<T, TripleNodo<Arbol<T>>> get() {
			return Alguno.right(branches);
		}
	}
}
