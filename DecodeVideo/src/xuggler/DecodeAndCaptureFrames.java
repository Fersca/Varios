package xuggler;

/*
 * Copyright (c) 2008-2009 by Xuggle Inc. All rights reserved.
 *
 * It is REQUESTED BUT NOT REQUIRED if you use this library, that you let 
 * us know by sending e-mail to info@xuggle.com telling us briefly how you're
 * using the library and what you like or don't like about it.
 *
 * This library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

/**
 * Takes a media container, finds the first video stream, decodes that
 * stream, and then writes video frames at some interval based on the
 * video presentation time stamps.
 *
 * @author aclarke
 */

public class DecodeAndCaptureFrames
{
  /** The number of seconds between frames. */

  public static final double SECONDS_BETWEEN_FRAMES = 1;

  /** The number of nano-seconds between frames. */

  public static final long NANO_SECONDS_BETWEEN_FRAMES = 
    (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
  
  /** Time of last frame write. */
  
  private static long mLastPtsWrite = Global.NO_PTS;

  /** Write the video frame out to a PNG file every once and a while.
   * The files are written out to the system's temporary directory.
   *
   * @param picture the video frame which contains the time stamp.
   * @param image the buffered image to write out
   */

  private static void processFrame(IVideoPicture picture, BufferedImage image)
  {
    try
    {
      // if uninitialized, backdate mLastPtsWrite so we get the very
      // first frame

      if (mLastPtsWrite == Global.NO_PTS)
        mLastPtsWrite = picture.getPts() - NANO_SECONDS_BETWEEN_FRAMES;

      // if it's time to write the next frame

      if (picture.getPts() - mLastPtsWrite >= NANO_SECONDS_BETWEEN_FRAMES)
      {
        // Make a temorary file name

        File file = File.createTempFile("frame", ".png");
    	//File file = File("/Users/fscasserra/video/frame.png")//, ".png");
    	  
        // write out PNG

        ImageIO.write(image, "png", file);            
        
        // indicate file written

        double seconds = ((double)picture.getPts()) / Global.DEFAULT_PTS_PER_SECOND;
        System.out.printf("at elapsed time of %6.3f seconds wrote: %s\n",
          seconds, file);
        
        // update last write time
        
        mLastPtsWrite += NANO_SECONDS_BETWEEN_FRAMES;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Takes a media container (file) as the first argument, opens it,
   * reads through the file and captures video frames periodically as
   * specified by SECONDS_BETWEEN_FRAMES.  The frames are written as PNG
   * files into the systems temorary directory.
   *  
   * @param args must contain one string which represents a filename
   */

  public static void main(String[] args)
  {

    String filename = "/Users/fscasserra/video/pelito.mpg";//"/home/fersca/pelis/monster.avi";

    // make sure that we can actually convert video pixel formats

    if (!IVideoResampler.isSupported(
        IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
      throw new RuntimeException(
        "you must install the GPL version of Xuggler (with IVideoResampler" + 
        " support) for this demo to work");

    // create a Xuggler container object

    IContainer container = IContainer.make();

    // open up the container

    if (container.open(filename, IContainer.Type.READ, null) < 0)
      throw new IllegalArgumentException("could not open file: " + filename);

    // query how many streams the call to open found

    int numStreams = container.getNumStreams();

    // and iterate through the streams to find the first video stream

    int videoStreamId = -1;
    IStreamCoder videoCoder = null;
    for(int i = 0; i < numStreams; i++)
    {
      // find the stream object

      IStream stream = container.getStream(i);

      // get the pre-configured decoder that can decode this stream;

      IStreamCoder coder = stream.getStreamCoder();

      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
      {
        videoStreamId = i;
        videoCoder = coder;
        break;
      }
    }

    if (videoStreamId == -1)
      throw new RuntimeException("could not find video stream in container: "+filename);

    // Now we have found the video stream in this file.  Let's open up
    // our decoder so it can do work

    if (videoCoder.open() < 0)
      throw new RuntimeException(
        "could not open video decoder for container: " + filename);

    IVideoResampler resampler = null;
    if (videoCoder.getPixelType() != IPixelFormat.Type.RGB24)
    {
      // if this stream is not in RGB32, we're going to need to
      // convert it.  The VideoResampler does that for us.

      resampler = IVideoResampler.make(
        videoCoder.getWidth(), videoCoder.getHeight(), IPixelFormat.Type.RGB24,
        videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
      if (resampler == null)
        throw new RuntimeException(
          "could not create color space resampler for: " + filename);
    }

    // Now, we start walking through the container looking at each packet.

    IPacket packet = IPacket.make();
    long firstTimestampInStream = Global.NO_PTS;
    long systemClockStartTime = 0;
    while(container.readNextPacket(packet) >= 0)
    {
      
      // Now we have a packet, let's see if it belongs to our video strea

      if (packet.getStreamIndex() == videoStreamId)
      {
        // We allocate a new picture to get the data out of Xuggle

        IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
            videoCoder.getWidth(), videoCoder.getHeight());

        int offset = 0;
        while(offset < packet.getSize())
        {
          // Now, we decode the video, checking for any errors.

          int bytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
          if (bytesDecoded < 0)
            throw new RuntimeException("got error decoding video in: " + filename);
          offset += bytesDecoded;
          
          // Some decoders will consume data in a packet, but will not
          // be able to construct a full video picture yet.  Therefore
          // you should always check if you got a complete picture from
          // the decode.

          if (picture.isComplete())
          {
            IVideoPicture newPic = picture;
            
            // If the resampler is not null, it means we didn't get the
            // video in RGB32 format and need to convert it into RGB32
            // format.

            if (resampler != null)
            {
              // we must resample
              newPic = IVideoPicture.make(
                resampler.getOutputPixelFormat(), picture.getWidth(), 
                picture.getHeight());
              if (resampler.resample(newPic, picture) < 0)
                throw new RuntimeException(
                  "could not resample video from: " + filename);
            }

            if (newPic.getPixelType() != IPixelFormat.Type.RGB24)
              throw new RuntimeException(
                "could not decode video as RGB 32 bit data in: " + filename);

            // convert the RGB32 to an Java buffered image

            BufferedImage javaImage = Utils.videoPictureToImage(newPic);

            // process the video frame

            processFrame(newPic, javaImage);
          }
        }
      }
      else
      {
        // This packet isn't part of our video stream, so we just
        // silently drop it.
      }
    }

    // Technically since we're exiting anyway, these will be cleaned up
    // by the garbage collector... but because we're nice people and
    // want to be invited places for Christmas, we're going to show how
    // to clean up.

    if (videoCoder != null)
    {
      videoCoder.close();
      videoCoder = null;
    }
    if (container !=null)
    {
      container.close();
      container = null;
    }
  }
}