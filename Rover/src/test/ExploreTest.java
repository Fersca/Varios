package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import rover.RoverCommander;

public class ExploreTest {

	@Test
	/**
	 * Test if the example instruction set returns the expected result as the description of the problem indicated 
	 */
	public void testExplorationOk(){
		
		ArrayList<String> instructions = new ArrayList<String>();
		
		instructions.add("5 5");
		instructions.add("1 2 N");
		instructions.add("LMLMLMLMM");
		instructions.add("3 3 E");
		instructions.add("MMRMMRMRRM");
		
		RoverCommander commander = new RoverCommander();
		ArrayList<String> positions = commander.explore(instructions);
		
		assertEquals("13N",positions.get(0));
		assertEquals("51E",positions.get(1));
		
	}
	
	@Test
	/**
	 * Test the message if the position is not available
	 */
	public void testBusyPosition(){
		
		ArrayList<String> instructions = new ArrayList<String>();
		
		instructions.add("5 5");
		instructions.add("1 2 N");
		instructions.add("LMLMLMLMM");
		instructions.add("3 3 E");
		instructions.add("MMRMMRMRRM");
		instructions.add("3 3 E");
		instructions.add("MMRMMRMRRM");
		
		RoverCommander commander = new RoverCommander();
		ArrayList<String> positions = commander.explore(instructions);
		
		assertEquals("13N",positions.get(0));
		assertEquals("51E",positions.get(1));
		assertEquals("The position is busy by another rover, X=5, Y=1",positions.get(2));
				
	}

	@Test
	/**
	 * Test the message then the rover try to go outside the map
	 */
	public void testInvalidPosition(){
		
		ArrayList<String> instructions = new ArrayList<String>();
		
		instructions.add("5 5");
		instructions.add("1 2 N");
		instructions.add("MMMMMMMMMM");
		
		RoverCommander commander = new RoverCommander();
		ArrayList<String> positions = commander.explore(instructions);
		
		assertEquals("Invalid position, the rover is out of the planet, X=1, Y=6",positions.get(0));
				
	}

}
