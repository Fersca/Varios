package prueba;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.media.*;
import javax.media.control.*;
import javax.media.format.*;
import javax.media.util.*;


/**
 * Grabs a frame from a Webcam, overlays the current date and time, and saves the frame as a PNG to c:\webcam.png
 *
 * @author David
 * @version 1.0, 16/01/2004
 */
public class FrameGrab
{
    public static void main(String[] args) throws Exception
    {
        // Create capture device
        CaptureDeviceInfo deviceInfo = CaptureDeviceManager.getDevice("vfw:Microsoft WDM Image Capture (Win32):0");
        Player player = Manager.createRealizedPlayer(deviceInfo.getLocator());
        player.start();

        // Wait a few seconds for camera to initialise (otherwise img==null)
        Thread.sleep(2500);

        // Grab a frame from the capture device
        FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
        Buffer buf = frameGrabber.grabFrame();

        // Convert frame to an buffered image so it can be processed and saved
        Image img = (new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
        BufferedImage buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        g.drawImage(img, null, null);
        
        // Save image to disk as PNG
        ImageIO.write(buffImg, "png", new File(".\\webcam.png"));

        // Stop using webcam
        player.close();
        player.deallocate();
        System.exit(0);
    }
}