package prueba;

import java.awt.Component;
import java.awt.Dimension;
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
import javax.media.Control;
import javax.media.DataSink;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Processor;
import javax.media.ProcessorModel;
import javax.media.control.FrameGrabbingControl;
import javax.media.control.TrackControl;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import javax.media.util.BufferToImage;

public class Main3 {

    public static void main(String[] args) throws Exception {
        new VidCap3();
    }
}

class Grabador2 extends Thread {

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

class VidCap3 {

    CaptureDeviceInfo device = null;
    MediaLocator ml = null;
    Player player = null;
    Component videoScreen = null;
    Processor processor = null;
    DataSink dataSink = null;
    DataSource ods;
    Frame frm = new Frame("Display of Webcam");
    Grabador g;
    DataSource CaptureVidDS;
    DataSource cloneableDS;
    FileTypeDescriptor outputType;
    Format outputFormat[] = new Format[1];
    
    VidCap3() throws Exception {

        // setup output file format to msvideo
    	//outputType = new FileTypeDescriptor(FileTypeDescriptor.MSVIDEO);
        outputType = new FileTypeDescriptor(FileTypeDescriptor.QUICKTIME);
    	//outputFormat[0] = new VideoFormat(VideoFormat.YUV);
        outputFormat[0] = new VideoFormat(VideoFormat.CINEPAK);
        
        @SuppressWarnings("rawtypes")
		Vector deviceList = CaptureDeviceManager.getDeviceList(new javax.media.format.RGBFormat());

        // get video device - the first one is almost always the only available camera
        device = (CaptureDeviceInfo) deviceList.firstElement();
        ml = device.getLocator();

        //create a source from the device
        ods = Manager.createDataSource(ml);

        cloneableDS = Manager.createCloneableDataSource(ods);

        player = Manager.createRealizedPlayer(cloneableDS);
        player.start();
        
        videoScreen = player.getVisualComponent();

        frm.add(videoScreen);
        frm.setVisible(true);
        
        frm.setBounds(10, 10, 640, 480);

        /*
        g = new Grabador();
        g.start();
       
        DataSource dsRT = ((javax.media.protocol.SourceCloneable) cloneableDS).createClone();
        processor = Manager.createProcessor(dsRT);
        processor.realize();
        
    	TrackControl [] tracks = processor.getTrackControls();
    	// Search through the tracks for a video track
		for (int i = 0; i < tracks.length; i++) {
		    Format format = tracks[i].getFormat();
		    if (tracks[i].isEnabled() && format instanceof VideoFormat) {	
				// Found a video track. Try to program it to output JPEG/RTP
				// Make sure the sizes are multiple of 8's.
		    	Format[] supported = tracks[i].getSupportedFormats();
		    	Dimension size = ((VideoFormat)format).getSize();
				float frameRate = ((VideoFormat)format).getFrameRate();
				int w = (size.width % 8 == 0 ? size.width :(int)(size.width / 8) * 8);
				int h = (size.height % 8 == 0 ? size.height : (int)(size.height / 8) * 8);
				VideoFormat jpegFormat = new VideoFormat(VideoFormat.JPEG_RTP,new Dimension(w, h), Format.NOT_SPECIFIED,Format.byteArray,frameRate);
				tracks[i].setFormat(jpegFormat);
		    } else {
		    	tracks[i].setEnabled(false);
		    }
		}
		// Set the output content descriptor to RAW_RTP
		ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
		processor.setContentDescriptor(cd);

		DataSource dataOutput = processor.getDataOutput();
        
        String rtpURL = "rtp://@127.0.0.1:2222/video";
        MediaLocator outputLocator = new MediaLocator(rtpURL);
        DataSink rtptransmitter = Manager.createDataSink(dataOutput, outputLocator);
        rtptransmitter.open();
        rtptransmitter.start();
        dataOutput.start();
        processor.start();
        
        Thread.sleep(10000);
        
    	processor.stop();
        processor.close();
        processor = null;
        rtptransmitter.close();
        rtptransmitter = null;
        */
        /*
        String url= "rtp://localhost:49150/audio/1";
        MediaLocator m = new MediaLocator(url);
        DataSink d = Manager.createDataSink(((javax.media.protocol.SourceCloneable) cloneableDS).createClone(), m);
        d.open();
        d.start();
        System.out.println("transmitting...");
        processor.start();
        */
        
        VideoTransmit vt = new VideoTransmit(((javax.media.protocol.SourceCloneable) cloneableDS).createClone(), "192.168.1.255", "22222"); 
    	String result = vt.start();

    	// result will be non-null if there was an error. The return
    	// value is a String describing the possible error. Print it.
    	if (result != null) {
    	    System.err.println("Error : " + result);
    	    System.exit(0);
    	}

    	System.err.println("Start transmission for 60 seconds...");
    	
    	// Transmit for 60 seconds and then close the processor
    	// This is a safeguard when using a capture data source
    	// so that the capture device will be properly released
    	// before quitting.
    	// The right thing to do would be to have a GUI with a
    	// "Stop" button that would call stop on VideoTransmit
    	
    	/*
    	pru(1);
    	pru(2);
    	pru(3);
    	pru(4);
    	pru(5);
    	*/
    	try {
    	    Thread.currentThread().sleep(3600000);
    	} catch (InterruptedException ie) {
    	}

    	// Stop the transmission
    	vt.stop();

    	System.err.println("...transmission ended.");
    	
    	System.exit(0);

                
        /*

        System.exit(0);
        */
    }

    private void prueba() {
    	MediaLocator mediaLocator = null;
    	DataSink dataSink = null;
    	Processor mediaProcessor = null;
		
	}

	void pru(int num) {
        try { //gets a list of devices how support the given video format

            CaptureVidDS = ((javax.media.protocol.SourceCloneable) cloneableDS).createClone();
    
            ProcessorModel processorModel = new ProcessorModel(CaptureVidDS, outputFormat, outputType);
            processor = Manager.createRealizedProcessor(processorModel);

            // get the output of the processor to be used as the datasink input 
            DataSource source = processor.getDataOutput();

            // create a File protocol MediaLocator with the location
            // of the file to which bits are to be written
            MediaLocator mediadestination = new MediaLocator("file:.\\vidcap"+num+".mov");
            
            // create a datasink to create the video file
            dataSink = Manager.createDataSink(source, mediadestination);

            dataSink.open();

            // now start the datasink and processor
            dataSink.start();
            processor.start();
                
           /*
            g.setPlayer(player);
            g.setNum(num);
            g.setCant(0);
            g.setGrabar(true);
            */
            Thread.sleep(5500);
                        
            //g.setGrabar(false);
            
            processor.stop();
            processor.close();

            //dataSinkListener.waitEndOfStream(10);
            dataSink.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
}
