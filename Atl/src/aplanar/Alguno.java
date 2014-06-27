package aplanar;

/**
 * A type which stores one of either of two types of value, but not both.
 *
 */
public class Alguno<A,B> {
	
	/**
	 * Constructs a left-type Either
	 */
	public static <A> Alguno left(A a) {
		if (a == null) throw new IllegalArgumentException();
		return new Alguno(a, null);
	}
	
	/**
	 * Constructs a right-type Either
	 */
	public static <B> Alguno right(B b) {
		if (b == null) throw new IllegalArgumentException();
		return new Alguno(null, b);
	}
	
	
	private final A a;
	private final B b;
	
	private Alguno(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	/**
	 * Applies function f to the contained value if it is a left-type and returns the result. Throws an IllegalStateException if this is a right-type Either.
	 */
	public<T> T ifLeft(Function<A,T> f) {
		if (!this.isLeft()) {
			throw new IllegalStateException();
		}
		return f.apply(a);
		
	}

	/**
	 * Applies function f to the contained value if it is a right-type and returns the result. Throws an IllegalStateException if this is a left-type Either.
	 */
	public<T> T ifRight(Function<B,T> f) {
		if (this.isLeft()) {
			throw new IllegalStateException();
		}
		return f.apply(b);
		
	}
	
	/**
	 * @return true if this is a left, false if it is a right
	 */
	public boolean isLeft() {
		return b == null;
	}

}
