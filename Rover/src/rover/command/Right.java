package rover.command;

import rover.Orientation;
import rover.Rover;

/**
 * This class has the procedure to turn right the rover
 * @author Fernando Scasserra
 *
 */
public class Right implements Command{

	@Override
	public void executeCommand(Rover rover) {
	
		if (rover.getOrientation().equals(Orientation.EAST))
			rover.setOrientation(Orientation.SOUTH);
		else if (rover.getOrientation().equals(Orientation.SOUTH))
			rover.setOrientation(Orientation.WEST);
		else if (rover.getOrientation().equals(Orientation.WEST))
			rover.setOrientation(Orientation.NORTH);
		else 
			rover.setOrientation(Orientation.EAST);
		
	}

}
