package rover;

import java.util.ArrayList;

import rover.command.Command;
import rover.command.Left;
import rover.command.Move;
import rover.command.Right;

/**
 * This class is used to manage each rover on the map
 * @author Fernando Scasserra
 */
public class Rover {
	
	//Rover position and orientation
	private int posX;
	private int posY;
	private Orientation orientation;
	
	//Rover list of instructions
	private ArrayList<Command> commands = new ArrayList<Command>();
	
	//Pointer to the rover commander in order to get the map dimensions and the rover list
	private RoverCommander commander;	
	
	//Getters and Setters
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getPosY() {
		return posY;
	}
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	public Orientation getOrientation() {
		return orientation;
	}
	public ArrayList<Command> getCommands() {
		return commands;
	}
	public void setCommander(RoverCommander commander) {
		this.commander = commander;
	}
	public RoverCommander getCommander() {
		return commander;
	}

	/**
	 * Execute each instruction sent by Nasa in the current Rover, in order to move it in the map
	 * @return
	 */
	public String executePlan() {
		
		try {
		
			//Execute each received instruction on the current rover
			for (Command com : commands) {
				com.executeCommand(this);
			}
			
			//Return the final position for the rover
			return getFinalPosition();
			
		} catch (PositionException pe){
			return  pe.getMessage()+", X="+pe.getX()+", Y="+pe.getY();
		} 

	}

	/**
	 * Store each instruction in the commands collection for the current rover
	 * @param rawCommands
	 */
	public void setCommands(String rawCommands) {
		
		char commands[] = rawCommands.toCharArray();
		
		for (char command : commands) {

			if ('L'==command){
				this.commands.add(new Left());
			} else if ('R'==command){
				this.commands.add(new Right());
			} else if ('M'==command)
				this.commands.add(new Move());
				
		}
		
	}
		
	/**
	 * Set the initial rover position and orientation in the map
	 * @param instructions
	 */
	public void setPositions(String instructions) {

		String position[] = instructions.split(" ");
		this.posX = Integer.parseInt(position[0]);
		this.posY = Integer.parseInt(position[1]);
		
		String orientation = position[2];
		
		if ("N".equals(orientation)){
			this.orientation = Orientation.NORTH;
		} else if ("S".equals(orientation)){
			this.orientation = Orientation.SOUTH;
		} else if ("E".equals(orientation)){
			this.orientation = Orientation.EAST;
		} else if ("W".equals(orientation)){
			this.orientation = Orientation.WEST;
		}
		
	}
	
	/**
	 * Calculate the final position and orientation 
	 * @return
	 */
	public String getFinalPosition() {
	
		return this.getPosX()+""+this.getPosY()+transformOrientation(this.orientation);
		
	}
	
	/**
	 * Decode the orientation codes in human style format
	 * @param orientation
	 * @return
	 */
	private String transformOrientation(Orientation orientation) {

		switch (orientation) {
		case EAST:
			return "E";
		case WEST:
			return "W";
		case NORTH:
			return "N";
		case SOUTH:
			return "S";
		default:
			return "";
		}
		
	}
	
}
