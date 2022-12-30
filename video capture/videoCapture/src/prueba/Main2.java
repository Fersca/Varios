package prueba;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.DataSink;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Processor;
import javax.media.ProcessorModel;
import javax.media.control.FrameGrabbingControl;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.util.BufferToImage;

public class Main2 {

    public static void main(String[] args) throws Exception {
        new VidCap2();
    }
}

class Grabador extends Thread {

	Player player;
	int num;
	int cant;
	boolean grabar = false;
	
	void setPlayer(Player play){
		this.player=play;
	}
	
	@Override
	public void run() {

		while(true){
			try {
				Thread.sleep(1000);
				if (grabar){
					saveImage(player,cant);
					cant++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	
	}

	private void saveImage(Player player2, int cont) {
        
		try {
        // Grab a frame from the capture device
        FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player2.getControl("javax.media.control.FrameGrabbingControl");
        Buffer buf = frameGrabber.grabFrame();

        // Convert frame to an buffered image so it can be processed and saved
        Image img = (new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
        
        //se pone este if porque a veces quiere obtener la foto y el player esta justo apagado
        if (img==null)
        	return;
        
        BufferedImage buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        g.drawImage(img, null, null);
        
        // Save image to disk as PNG
		ImageIO.write(buffImg, "png", new File(".\\webcam"+num+"-"+cont+".png"));
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setNum(int num) {
		this.num=num;
	}
	public void setCant(int c) {
		this.cant=c;
	}
	public void setGrabar(boolean graba){
		this.grabar=graba;
	}
	
}

class VidCap2 {

    CaptureDeviceInfo device = null;
    MediaLocator ml = null;
    Player player = null;
    Component videoScreen = null;
    Processor processor = null;
    DataSink dataSink = null;
    TheDataSinkListener dataSinkListener = null;
    DataSource ods;
    Frame frm = new Frame("Display of Webcam");
    Grabador g;
    
    VidCap2() throws Exception {
    	
        @SuppressWarnings("rawtypes")
		Vector deviceList = CaptureDeviceManager.getDeviceList(new YUVFormat());

        // get video device - the first one is almost always the only available camera
        device = (CaptureDeviceInfo) deviceList.firstElement();
        ml = device.getLocator();

        //create a source from the device
        ods = Manager.createDataSource(ml);
        frm.setBounds(10, 10, 640, 480);

        g = new Grabador();
        g.start();
        
    	pru(1);
    	pru(2);
    	pru(3);
    	pru(4);
    	pru(5);
        System.exit(0);

    }

    void pru(int num) {
        try { //gets a list of devices how support the given video format

            DataSource cloneableDS = Manager.createCloneableDataSource(ods);
            DataSource PlayerVidDS = cloneableDS;

            // The video capture code will use the clone which is controlled by the player
            DataSource CaptureVidDS = ((javax.media.protocol.SourceCloneable) cloneableDS).createClone();

            player = Manager.createRealizedPlayer(PlayerVidDS);
            player.start();
            
            // setup output file format to msvideo
            FileTypeDescriptor outputType = new FileTypeDescriptor(FileTypeDescriptor.MSVIDEO);

            // setup output video and audio data format
            Format outputFormat[] = new Format[1];
            //outputFormat[0] = new VideoFormat(VideoFormat.RGB);
            outputFormat[0] = new VideoFormat(VideoFormat.YUV);

            ProcessorModel processorModel = new ProcessorModel(CaptureVidDS, outputFormat, outputType);
            processor = Manager.createRealizedProcessor(processorModel);

            // get the output of the processor to be used as the datasink input 
            DataSource source = processor.getDataOutput();

            // create a File protocol MediaLocator with the location
            // of the file to which bits are to be written
            MediaLocator mediadestination = new MediaLocator("file:.\\vidcap"+num+".avi");

            // create a datasink to create the video file
            dataSink = Manager.createDataSink(source, mediadestination);

            // create a listener to control the datasink
            dataSinkListener = new TheDataSinkListener();
            dataSink.addDataSinkListener(dataSinkListener);
            dataSink.open();

            // now start the datasink and processor
            dataSink.start();
            processor.start();
                
            videoScreen = player.getVisualComponent();

            frm.add(videoScreen);
            frm.setVisible(true);

            g.setPlayer(player);
            g.setNum(num);
            g.setCant(0);
            g.setGrabar(true);
            
            Thread.sleep(5500);
            
            g.setGrabar(false);
            
            frm.setVisible(false);
            frm.remove(videoScreen);

            processor.stop();
            processor.close();

            dataSinkListener.waitEndOfStream(10);
            dataSink.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
}

/**
 * Control the ending of the program prior to closing the data sink
 *
 */
class TheDataSinkListener implements DataSinkListener {

    boolean endOfStream = false;

    // Flag the ending of the data stream
    public void dataSinkUpdate(DataSinkEvent event) {
        if (event instanceof javax.media.datasink.EndOfStreamEvent) {
            endOfStream = true;
        }
    }

    /*Cause the current thread to sleep if the data stream is still available.
     * This makes certain that JMF threads are done prior
     * to closing the data sink and finalizing the output file
     */
    public void waitEndOfStream(long checkTimeMs) {
        while (!endOfStream) {
            try {
                //Thread.currentThread().sleep(checkTimeMs);
                Thread.sleep(checkTimeMs);
            } catch (InterruptedException ie) {
                // your exception handling here
            }
        }
    }
}