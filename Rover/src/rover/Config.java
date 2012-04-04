package rover;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This Class in used to manage the instruction File in order to get the instruction list for the mission. 
 * @author Fernando Scasserra
 *
 */
public class Config {

	String file;
	
	public Config(String file) {
		this.file=file;
	}

	/**
	 * Read each line from file and return the mission instructions
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> readInstructionsFromFile() throws IOException {

		ArrayList<String> instructions = new ArrayList<String>();

	    BufferedReader in = new BufferedReader(new FileReader(this.file));
	    String str;
	    while ((str = in.readLine()) != null) {
	        instructions.add(str);
	    }
	    in.close();
		    
		return instructions;

	}
	
}
