package rover.command;

import rover.PositionException;
import rover.Rover;

/**
 * Define the methods for the different commands
 * @author Fernando Scasserra
 *
 */
public interface Command {

	public abstract void executeCommand(Rover rover) throws PositionException;
	
}
