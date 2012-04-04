package rover;

/**
 * This class is used to process the invalid positions as positions out of the map limits 
 * or busy positions by other rovers
 * @author Fernando Scasserra
 */
public class PositionException extends Exception{

	private static final long serialVersionUID = -286602553593085931L;

	//Position in conflict
	private int X;
	private int Y;
	//Message explaining the conflict for this position
	private String message;

	public PositionException(int x, int y, String message){
		this.X=x;
		this.Y=y;
		this.message=message;
	}
	
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	public String getMessage() {
		return message;
	}

	
}
