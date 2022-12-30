package prueba;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FormatControl;
import javax.swing.JFrame;

public class MyCam{

	CaptureDeviceInfo cam;	
	MediaLocator locator;
	Player player;
	FormatControl formatControl;
	public MyCam(){
		try{
			
			// List out available Devices to Capture Video. 
			Vector<CaptureDeviceInfo> list = CaptureDeviceManager.getDeviceList ( null );
			
			// Iterating list 
			for(CaptureDeviceInfo temp : list){
				// Checking whether the current device supports VfW
				// VfW = Video for Windows
				
				System.out.println(temp.getName());
				if(temp.getName().startsWith("vfw:")){

					System.out.println("Found : "+temp.getName().substring(4));
					// Selecting the very first device that supports VfW
					cam = temp;
					System.out.println("Selected : "+cam.getName().substring(4));
					break;
				}
			}

			System.out.println("Put it on work!...");
			// Getting the MediaLocator for Selected device.
			// MediaLocator describes the location of media content
			locator = cam.getLocator();

			if(locator != null){

				// Create a Player for Media Located by MediaLocator
				player = Manager.createRealizedPlayer(locator);

				if(player != null){
					
					// Starting the player
					player.start();
					
					// Creating a Frame to display Video
					JFrame f = new JFrame();
					f.setTitle("Test Webcam");
					f.setLayout(new BorderLayout());
					// Adding the Visual Component to display Video captured by Player 
					// from URL provided by MediaLocator
					f.add(player.getVisualComponent(), BorderLayout.CENTER);
					f.pack(); 
					f.setVisible(true);

				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new MyCam();
	}
}