package rover;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class process each connection accepted by the remote control
 * @author Fernando Scasserra
 *
 */
public class NasaRemoteWorker implements Runnable {

	//Current connection
	Socket connection;
	
	public NasaRemoteWorker(Socket connection){
		this.connection=connection;
	}
	
	@Override
	/**
	 * Process each connection, read the instructions, send the response to the client
	 */
	public void run() {

        BufferedReader reader=null;
        DataOutputStream output=null;

		try {
			
			//Objects to store the list of instructions
			String instruction;
			ArrayList<String> instructions= new ArrayList<String>();
	
			//Reader and Writer channels to the socket
	        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        output = new DataOutputStream(connection.getOutputStream());
	       
	        //Capture the instructions until the END command is sent
	        boolean reading=true;
	        while (reading){
	        	
	        	instruction = reader.readLine();
	        	if (instruction!=null && !instruction.equals("END")){
	        		instructions.add(instruction);
	        	} else {
	        		reading = false;
	        	}
	        	
	        }
	       	       
	        //Create a RoverCommander to explore with the instructions captured
	        RoverCommander commander = new RoverCommander();
	        ArrayList<String> finalPositions = commander.explore(instructions);
	
	        //Print the result in the client terminal
	        output.writeBytes("Result\n");
	        output.writeBytes("------\n");
	        for (String pos : finalPositions) {
	        	output.writeBytes(pos+"\n");
			}
	        output.writeBytes("------\n");
	
	        System.out.println("Result sent to client");
	        
		} catch (IOException io){
			System.out.println("Error reading from socket");
		} finally{
	        //Close the reader, writer and connections objects.
	        try{
	        	reader.close();
	        	output.close();
	        	connection.close();
	        } catch (IOException e){}
		}
	}

	
}
