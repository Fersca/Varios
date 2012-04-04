package rover;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This Class is the Main class of the system, it's used to manage the connections if the system
 * is started as remote control or process the instruction file from Nasa. 
 * @author Fernando Scasserra
 *
 */
public class Nasa {

	/**
	 * Manage the Mars Rover System, in Remote control or by sending an instruction File
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		if (args.length<1){
			System.out.println("Invalid parameters. Ej: Avalilable parameters: -remote | -file file.txt");
			return;
		}

		Nasa nasa = new Nasa();
		
		//Starts the Mars Rover system in remote mode
		if (args[0].equals("-remote")){
			nasa.runRemote();
		}

		//Starts the Mars Rover reading the instructions from a file
		if (args[0].equals("-file")){
			nasa.runFile(args[1]);
		}

	}
	
	/**
	 * Manage the Mars Rover system remotely, listening at port 5555
	 */
	private void runRemote() {
	
		try {
				        
			//Create server socket listening at port 5555
	        ServerSocket socket = new ServerSocket(5555);
	        Socket connection;
	        
	        //Print help in the server
	        System.out.println("Listening at port 5555, Example instruction set:");
	        System.out.println("----------------------");
	        System.out.println("5 5");
	        System.out.println("1 2 N");
	        System.out.println("RRLLRRLLMM");
	        System.out.println("END");
	        System.out.println("----------------------");
	        System.out.println("");
	        while(true){
	        	
	        	//Wait fir the connection to be accepted
		        connection = socket.accept();
		    
		        System.out.println("New connection accepted");
		        
		        //Process the new connection in a separate thread 
		        NasaRemoteWorker remote = new NasaRemoteWorker(connection);
		        Thread t = new Thread(remote);
		        t.run();
	        }
	        
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Error reading from socket");
		}
		
	}
	
	/**
	 * Manage the Mars Rover system by sending processing instructions from a file
	 * @param file
	 */
	private void runFile(String file) {
	
		//Create a config objet to manage the instruction file
		Config conf = new Config(file);
		ArrayList<String> rawInstructions = null;
		
		try {
			
			//Read the instructions from Nasa file
			rawInstructions = conf.readInstructionsFromFile();
						
		} catch (IOException e) {
			System.out.println("Error reading file, finishing program");
			return;
		}
		
		//Create a RoverCommander in order to manage the exploration
		RoverCommander commander = new RoverCommander();
		ArrayList<String> positions = commander.explore(rawInstructions);

		//Print the final rover positions
		for (String pos : positions) {
			System.out.println(pos);
		}
	
	}	
		
}
