package rover.command;

import rover.Orientation;
import rover.PositionException;
import rover.Rover;
import rover.RoverCommander;

/**
 * This class has the procedure to move the rover forward
 * @author Fernando Scasserra
 *
 */
public class Move implements Command{
	
	@Override
	/**
	 * Calculate the future position, check is these positions are availables and move the rover
	 */
	public void executeCommand(Rover rover) throws PositionException {

		//Calculate the future position after make the rover to move
		int futureX, futureY;
		futureX = rover.getPosX();
		futureY = rover.getPosY();
		
		if (rover.getOrientation().equals(Orientation.EAST))
			futureX = rover.getPosX()+1;
		else if (rover.getOrientation().equals(Orientation.NORTH))
			futureY = rover.getPosY()+1;
		else if (rover.getOrientation().equals(Orientation.WEST))
			futureX = rover.getPosX()-1;
		else 
			futureY = rover.getPosY()-1;
		
		//Verify if the final position is valid and it's not busy
		checkInvalidPosition(futureX, futureY, rover.getCommander());
		checkBusyPosition(futureX, futureY,rover.getCommander());
			
		//Set the new rover position
		rover.setPosX(futureX);
		rover.setPosY(futureY);
		
	}

	/**
	 * Check if the future position is inside the limits of the map
	 * @param futureX
	 * @param futureY
	 * @param commander
	 * @throws PositionException
	 */
	private void checkInvalidPosition(int futureX, int futureY, RoverCommander commander) throws PositionException{
		if (futureX<0 || futureY<0 || futureX>commander.getMaxX() || futureY>commander.getMaxY())
			throw new PositionException(futureX,futureY,"Invalid position, the rover is out of the planet");
	}
	
	/**
	 * Check if the future position is not in use by another rover taking the list of rovers from the Rover Commander
	 * @param futureX
	 * @param futureY
	 * @param commander
	 * @throws PositionException
	 */
	private void checkBusyPosition(int futureX, int futureY, RoverCommander commander) throws PositionException{
				
		for (Rover r : commander.getRovers()){
			if (r.getPosX()==futureX && r.getPosY() == futureY)
				throw new PositionException(futureX,futureY,"The position is busy by another rover");
		}
		
	}

}
