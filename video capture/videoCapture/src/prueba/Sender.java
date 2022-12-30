package prueba;

import java.io.IOException;
import java.util.Vector;
import javax.media.*;
import javax.media.control.FormatControl;
import javax.media.control.TrackControl;
import javax.media.format.VideoFormat;
import javax.media.format.YUVFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

class Sender{
public static void main(String args[]){
Processor processor=null;
VideoFormat format= new YUVFormat();
Vector devices= CaptureDeviceManager.getDeviceList( format);
CaptureDeviceInfo di= null;
if (devices.size() > 0) {
di = (CaptureDeviceInfo) devices.elementAt( 0);
}
else {
System.out.println("No capture device found.");
// exit if we could not find the relevant capturedevice. 
System.exit(-1); 
}
// Create a processor for this capturedevice & exit if we 
// cannot create it 
try { 
processor = Manager.createProcessor(di.getLocator()); 
} catch (IOException e) { 
e.printStackTrace();
} catch (NoProcessorException e) { 
e.printStackTrace();
} 
// configure the processor 
processor.configure();
while(true){ 
if(processor.getState()==Processor.Configured){
break;
}
else if(processor.getState()==Processor.Configuring){
continue;
}
} 
// block until it has been configured 
processor.setContentDescriptor(new ContentDescriptor( ContentDescriptor.RAW));
TrackControl track[] = processor.getTrackControls(); 
boolean encodingOk = false;
// Go through the tracks and try to program one of them to
// output gsm data. 
System.out.println(" length: "+track.length);
for (int i = 0; i < track.length; i++) { 
if (!encodingOk && track[i] instanceof FormatControl) { 
if ( ( (FormatControl)track[i]).setFormat( new VideoFormat(VideoFormat.JPEG_RTP))==null) {
track[1].setEnabled(false); 
}
else {
encodingOk = true; 
}
} else { 
// we could not set this track to gsm, so disable it 
track[1].setEnabled(false); 
} 
}

// At this point, we have determined where we can send out 
// gsm data or not. 
// realize the processor 
if (encodingOk) { 
processor.realize(); 
while(true){ 
if(processor.getState()==Processor.Realized){
break;
}
else if(processor.getState()==Processor.Realizing){
continue;
}
}
// block until realized. 
// get the output datasource of the processor and exit 
// if we fail 
DataSource ds = null;

try { 
ds = processor.getDataOutput(); 
} catch (NotRealizedError e) { 
e.printStackTrace();
System.exit(-1);
}
// hand this datasource to manager for creating an RTP 
// datasink our RTP datasimnk will multicast the audio 
try {
String url= "rtp://192.168.1.102:22222/video/";
MediaLocator m = new MediaLocator(url);
DataSink d = Manager.createDataSink(ds, m);
d.open();
d.start(); 
processor.start();
} catch (Exception e) {
e.printStackTrace();
} 
} 
}
}