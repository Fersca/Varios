package rover;

import java.util.ArrayList;

/**
 * This class manage the list of rovers in the map and execute each rover plan
 * @author Fernando Scasserra
 *
 */
public class RoverCommander {

	//List of rovers in the mission
	private ArrayList<Rover> rovers;
	
	//Map limits
	private int maxX;
	private int maxY; 

	/**
	 * Process each instruction, create the rovers and explore the map
	 * @param rawInstructions
	 * @return
	 */
	public ArrayList<String> explore(ArrayList<String> rawInstructions){
		
		//Create the list of rovers
		rovers = createRovers(rawInstructions);
		ArrayList<String> finalPositions = new ArrayList<String>();
		
		//Execute each robot execution plan
		for (Rover rover : rovers) {
						
			//Execute the robot plan
			finalPositions.add(rover.executePlan());
				
		}
					
		//Return the final position of each rover
		return finalPositions;
		
	}
	
	/**
	 * Set the map limits and create the rovers
	 * @param rawInstructions
	 * @return
	 */
	private ArrayList<Rover> createRovers(ArrayList<String> rawInstructions) {
		
		//Set the map limits
		processMapLimit(rawInstructions.get(0));		
		rawInstructions.remove(0);

		//Create the collection of rovers
		return buidRovers(rawInstructions);
	}

	/**
	 * Set the map limits
	 * @param mapLimits
	 */
	private void processMapLimit(String mapLimits) {
		String limits[] = mapLimits.split(" ");
		this.setMaxX(Integer.parseInt(limits[0]));
		this.setMaxY(Integer.parseInt(limits[1]));
	}
	
	/**
	 * Create the rovers and upload the instructions for each one
	 * @param rawInstructions
	 * @return
	 */
	private ArrayList<Rover> buidRovers(ArrayList<String> rawInstructions) {

		//Create the rovers
		ArrayList<Rover> rovers = new ArrayList<Rover>();
			
		Rover rover=null;
		boolean newRover = true;
		
		for (String instructions : rawInstructions) {
		
			//Different actions for different lines
			if (newRover){
				//Create the rover and set the initial positions
				rover = new Rover();
				rover.setCommander(this); 
				rover.setPositions(instructions);
				newRover = false;
			} else {
				//The the list of instructions in the rover memory
				rover.setCommands(instructions);
				rovers.add(rover);
				newRover = true;
			}
			
		}
		
		//Return the list of created rovers
		return rovers;
		
	}

	//Getters and Setters
	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMaxY() {
		return maxY;
	}

	public ArrayList<Rover> getRovers(){
		return rovers;
	}
	
}
