package rover.command;

import rover.Orientation;
import rover.Rover;

/**
 * This class has the procedure to turn left the rover
 * @author Fernando Scasserra
 *
 */
public class Left implements Command{

	@Override
	public void executeCommand(Rover rover) {
		
		if (rover.getOrientation().equals(Orientation.EAST))
			rover.setOrientation(Orientation.NORTH);
		else if (rover.getOrientation().equals(Orientation.NORTH))
			rover.setOrientation(Orientation.WEST);
		else if (rover.getOrientation().equals(Orientation.WEST))
			rover.setOrientation(Orientation.SOUTH);
		else 
			rover.setOrientation(Orientation.EAST);
		 
	}

}
