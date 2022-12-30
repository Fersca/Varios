

import java.applet.Applet;
import java.awt.*;
import java.lang.String;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import javax.media.*;

/**
 * This is a Java Applet that demonstrates how to create a simple
 * media player with a media event listener.  It will play the
 * media clip right away and continuously loop.
 *
 * <!-- Sample HTML 
 * <applet code=TypicalPlayerApplet width=320 height=300> 
 * <param name=file value="Astrnmy.avi">
 * </applet>
 * -->
 */    
 
public class TypicalPlayerApplet extends Applet implements 
ControllerListener
{
   // media player    
   Player player = null;  
   // component in which video is playing             
   Component visualComponent  = null;   
   // controls gain, position, start, stop
   Component controlComponent = null;   
   // displays progress during download
   Component progressBar      = null;  
    
   /**
    * Read the applet file parameter and create the media
    * player.
    */
    
   public void init() 
   {
      setLayout(new BorderLayout());
      // input file name from html param
      String mediaFile = null;        
      // URL for our media file
      URL url = null;  
      // URL for doc containing applet               
      URL codeBase = getDocumentBase();  

      // Get the media filename info.
      // The applet tag should contain the path to the
      // source media file, relative to the html page.

      if ((mediaFile = getParameter("FILE")) == null)
         Fatal("Invalid media file parameter");
      try 
      {
         // Create an url from the file name and the url to the 
         // document containing this applet.

         if ((url = new URL(codeBase, mediaFile)) == null)
            Fatal("Can't build URL for " + mediaFile);

         // Create an instance of a player for this media
         if ((player = Manager.createPlayer(url)) == null)
            Fatal("Could not create player for "+url);

         // Add ourselves as a listener for player's events
            player.addControllerListener(this);
      } 
      catch (MalformedURLException u) 
      {
         Fatal("Invalid media file URL!");
      } 
      catch(IOException i) 
      {
         Fatal("IO exception creating player for "+url);
      } catch (NoPlayerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

      // This applet assumes that its start() calls  
      // player.start().This causes the player to become
      // Realized. Once Realized, the Applet will get
      //  the visual and control panel components and add 
      // them to the Applet.  These components are not added
      // during init() because they are long operations that
      // would make us appear unresposive to the user.
   }

   /**
    * Start media file playback.  This function is called the 
    * first time that the Applet runs and every
    * time the user re-enters the page.
    */    
    
   public void start() 
   {
      // Call start() to prefetch and start the player.
      
      if (player != null) player.start();
   }

   /**
    * Stop media file playback and release resources before
    * leaving the page.
    */
    
   public void stop() 
   {
      if (player != null)
      {
         player.stop();
         player.deallocate();
      }
   }

   /**
    * This controllerUpdate function must be defined in order
    * to implement a ControllerListener interface.  This
    * function will be called whenever there is a media event.
    */
    
   public synchronized void controllerUpdate(ControllerEvent event) 
   {
      // If we're getting messages from a dead player, 
      // just leave
      
      if (player == null) return;

      // When the player is Realized, get the visual
      // and control components and add them to the Applet
      
      if (event instanceof RealizeCompleteEvent) 
      {
         if ((visualComponent = player.getVisualComponent()) != null)
            add("Center", visualComponent);
         if ((controlComponent = player.getControlPanelComponent()) != null)
            add("South",controlComponent);
            // force the applet to draw the components
            validate();
      }
      else if (event instanceof CachingControlEvent) 
      {

         // Put a progress bar up when downloading starts,
         // take it down when downloading ends.

         CachingControlEvent  e = (CachingControlEvent) event;
         CachingControl      cc = e.getCachingControl();
         long cc_progress       = e.getContentProgress();
         long cc_length         = cc.getContentLength(); 
    
         // Add the bar if not already there ...
         
         if (progressBar == null)  
            if ((progressBar = cc.getProgressBarComponent()) != null) 
            {
               add("North", progressBar);
               validate();
            }
    
         // Remove bar when finished ownloading 
         if (progressBar != null)  
            if (cc_progress == cc_length) 
            {
               remove (progressBar);
               progressBar = null;
               validate();
            }
      }
      else if (event instanceof EndOfMediaEvent) 
      {
         // We've reached the end of the media; rewind and
         // start over
         
         player.setMediaTime(new Time(0));
         player.start();
      }
      else if (event instanceof ControllerErrorEvent) 
      {
         // Tell TypicalPlayerApplet.start() to call it a day
         
         player = null;
         Fatal (((ControllerErrorEvent)event).getMessage());
      }
   }

   void Fatal (String s) 
   {
      // Applications will make various choices about what
      // to do here.  We print a message and then exit
      
      System.err.println("FATAL ERROR: " + s);
      throw new Error(s);  // Invoke the uncaught exception
                           // handler System.exit() is another 
                           // choice
   }
}