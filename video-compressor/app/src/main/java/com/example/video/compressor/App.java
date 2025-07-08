package com.example.video.compressor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.javacv.OpenCVFrameConverter;
import java.util.List;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.opencv.global.opencv_core;

/**
 * Comprime un video usando una predicción del pixel futuro en base a una función.
 * Por ahora usa solo una función lineal para todo el video, para el mismo pixel.
 * Cambiarlo a que use todo tipo de funciones distintas según el tipo de datos de la columna de pixels
 * y cortarlo por segundos.
 * 
 * @author fersca
 */
public class App {

    public static void main(String[] args) throws FFmpegFrameGrabber.Exception, FFmpegFrameRecorder.Exception {
                
        App app = new App();
        app.run();
        
    }
    
    private void run() throws FFmpegFrameGrabber.Exception, FFmpegFrameRecorder.Exception{
        
        ArrayList<Mat> frames = new ArrayList<>();       
        String videoPath = "/home/fersca/Downloads/javi.mp4";
                
        //obtiene un array de frames del video
        Resolution resolution = processVideo(videoPath, frames);
        System.out.println("Resolution: "+resolution.width+","+resolution.height + ", fps: "+resolution.fps);
                
        int segundos = 5;
        
        //obtiene las columnas de pixels del video
        ArrayList<PixelColumn> pixels = new ArrayList<>();               
        processPixelColumns(resolution, frames, pixels, segundos*resolution.fps);
        
        //Por cada columna de pixels.
        processRegression(pixels);
           
        //recrear el video
        rebuildVideo(pixels, resolution, segundos);
        
        
    }

    private void processPixelColumns(Resolution resolution, ArrayList<Mat> frames, ArrayList<PixelColumn> pixels, int maxFrames) {
                        
        for (int width = 0; width<resolution.width;width++){
            System.out.println("x:"+width);                    
            for (int height = 0; height<resolution.height;height++){
                
                PixelColumn pixelCol = new PixelColumn();
                pixelCol.x = width;
                pixelCol.y = height;
                
                int cant = 0;
                //procesa cada pixel en todos los frames                
                for(Mat frame : frames){
                                       
                    // Ejemplo: obtener el valor de un píxel
                    int gray = getGrayValue(frame, width, height);
                    pixelCol.values.add(gray);
                                        
                    cant++;
                    if (cant==maxFrames) break;                    
                }
                pixels.add(pixelCol);
                //System.out.print(".");                
            }            
        }
        
        
    }

    private void rebuildVideo(ArrayList<PixelColumn> pixels, Resolution resolution, int segundos) throws FFmpegFrameRecorder.Exception {
        
        
        List<Mat> frames = new LinkedList<Mat>();
        
        for (int frameCount = 0;frameCount<(resolution.fps*segundos);frameCount++){
            
            // Imagen escala de grises de 8 bits
            Mat mat = new Mat(resolution.height, resolution.width, opencv_core.CV_8UC1);

            //recorro cada columna de pixels
            for (PixelColumn column : pixels){            

                //calculo el valor del pixel para el frame actual
                double value_pred = column.regression.slope * frameCount + column.regression.intercept;            

                byte[] v = new byte[]{(byte) value_pred};
                mat.ptr(column.y, column.x).put(v);            

            }
            
            //agrega la matriz a la lista de frames
            frames.add(mat);

        }
        
        if (frames.isEmpty()) {
            throw new RuntimeException("No hay frames para grabar.");
        }

        // Parámetros de video
        int width = frames.get(0).cols();
        int height = frames.get(0).rows();
        int fps = resolution.fps;

        // Inicializar el recorder
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("/home/fersca/Downloads/frames/salida.mp4", width, height);
        recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setFrameRate(fps);
        recorder.setVideoBitrate(4000000);

        // Convertidor entre Mat y Frame
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

        // Iniciar
        recorder.start();

        for (Mat mat : frames) {
            Frame frame = converter.convert(mat);
            recorder.record(frame);
        }

        // Finalizar
        recorder.stop();
        recorder.release();

        System.out.println("Video creado con " + frames.size() + " frames.");                        
        
    }
    // Clase contenedora del resultado
    record RegressionResult(double slope, double intercept) {}

    private void processRegression(ArrayList<PixelColumn> pixels) {

        String archivoSalida = "/home/fersca/Downloads/frames/salida.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {
                
            for (PixelColumn column : pixels){            
                column.regression = linearRegression(column.values);            

                //guarda archivo
                String linea = column.x+","+column.y+","+column.regression.slope+","+column.regression.intercept;
                writer.write(linea);
                writer.newLine();             
            }
            
        }  catch (IOException e) {
            e.printStackTrace();
        }   
                        
        System.out.println("Regresiones finalizadas");               
        
    }
    
    class PixelColumn {
        
        public int x;
        public int y;
        public ArrayList<Integer> values = new ArrayList<>();
        public RegressionResult regression;
        
    }
    
    public record Resolution(int width, int height, int fps) {}

    private Resolution processVideo(String videoPath, ArrayList<Mat> frames) throws FFmpegFrameGrabber.Exception {

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.start();

        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Resolution res = null;

        boolean first = true;
        while (true) {
            Frame frame = grabber.grabImage();
            if (frame == null) {
                break; // No más frames
            }
            

            if (first){
                int fps = (int) grabber.getFrameRate();
                res = new Resolution(grabber.getImageWidth(),grabber.getImageHeight(), fps);
                first = false;
            }
                            

            // Convertir a Mat en color
            Mat matColor = converter.convert(frame);

            // Crear Mat para grises
            Mat matGray = new Mat();
            opencv_imgproc.cvtColor(matColor, matGray, opencv_imgproc.COLOR_BGR2GRAY);
            frames.add(matGray);
          
        }

        grabber.stop();
        System.out.println("Extracción completada.");
        return res;
        
    }

    public int getGrayValue(Mat matGray, int x, int y) {
        if (matGray.channels() != 1) {
            throw new IllegalArgumentException("La imagen no es en grises.");
        }
        byte[] pixel = new byte[1];
        matGray.ptr(y, x).get(pixel);
        return pixel[0] & 0xFF;
    }
    
    
    
    public int getGray(Mat matGray, int x, int y) {
        if (matGray.channels() != 1) {
            throw new IllegalArgumentException("La imagen no es de un canal (grises).");
        }

        int width = matGray.cols();
        int height = matGray.rows();

        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Coordenadas fuera de rango.");
        }

        byte[] pixel = new byte[1];
        matGray.ptr(y, x).get(pixel);
        return pixel[0] & 0xFF;
    }    
    
    private RegressionResult linearRegression(List<Integer> yValues) {
        int N = yValues.size();

        if (N < 2) {
            throw new IllegalArgumentException("Se requieren al menos dos puntos.");
        }

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (int i = 0; i < N; i++) {
            double x = i;
            double y = yValues.get(i);

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double denominator = N * sumX2 - sumX * sumX;
        if (denominator == 0) {
            throw new IllegalArgumentException("No se puede calcular la regresión (denominador 0).");
        }

        double a = (N * sumXY - sumX * sumY) / denominator;
        double b = (sumY - a * sumX) / N;

        return new RegressionResult(a, b);
    }

}
