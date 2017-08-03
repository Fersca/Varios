package xuggler;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageToString {

	
	HashMap<Integer, String> caracteres = new HashMap<Integer, String>();
	Random rnd = new Random();
	
	public static void main(String[] args) throws Exception {
						
		ImageToString i = new ImageToString();
		
		//Completar Mapa
		i.completarMapa();

		//Carga la foto
		File f1 = new File("/Users/fscasserra/video/resize/rsz_2.png");
		File f2 = new File("/Users/fscasserra/video/resize/rsz_3.png");
		File f3 = new File("/Users/fscasserra/video/resize/rsz_4.png");
		File f4 = new File("/Users/fscasserra/video/resize/rsz_5.png");
		File f5 = new File("/Users/fscasserra/video/resize/rsz_6.png");
		File f6 = new File("/Users/fscasserra/video/resize/rsz_8.png");
		File f7 = new File("/Users/fscasserra/video/resize/rsz_9.png");
		
		ArrayList<String> l1 = i.detectColors(f1);
		System.out.println("File 1");
		ArrayList<String> l2 = i.detectColors(f2);
		System.out.println("File 2");
		ArrayList<String> l3 = i.detectColors(f3);
		System.out.println("File 3");
		ArrayList<String> l4 = i.detectColors(f4);
		System.out.println("File 4");
		ArrayList<String> l5 = i.detectColors(f5);
		System.out.println("File 5");
		ArrayList<String> l6 = i.detectColors(f6);
		System.out.println("File 6");
		ArrayList<String> l7 = i.detectColors(f7);
		System.out.println("File 7");

		PrintWriter out = new PrintWriter("/Users/fscasserra/video/resize/transmission.txt");
		
		String s1 = "中国电视系统。视频格式320x240 RGB，色彩强度基于汉字频率。从下一行开始。连续7帧。分手：中国电视 - 中国电视 - 中国电视台。最后一行是问候语和电视格式规范";		
		out.println(s1);
		out.println("中国电视 - 中国电视 - 中国电视");		
		for (String e:l1) out.println(e);
		out.println("中国电视 - 中国电视 - 中国电视");		
		for (String e:l2) out.println(e);
		out.println("中国电视 - 中国电视 - 中国电视");
		for (String e:l3) out.println(e);
		out.println("中国电视 - 中国电视 - 中国电视");
		for (String e:l4) out.println(e);
		out.println("中国电视 - 中国电视 - 中国电视");
		for (String e:l5) out.println(e);
		out.println("中国电视 - 中国电视 - 中国电视");
		for (String e:l6) out.println(e);
		out.println("中国电视 - 中国电视 - 中国电视");
		for (String e:l7) out.println(e);
		out.println("中国电视 - 中国电视 - 中国电视");
		String s2 = "传输端，电视规格：http://bit.ly/ChineseTV";
		out.println(s2);
		out.close();
		
	}
	
	private void completarMapa() throws IOException {
		//Lee letra por letra y lo deja en el mapa
		List<String> lines=Files.readAllLines(Paths.get("/Users/fscasserra/video/resize/frecuencia.txt"), Charset.forName("UTF-8"));
		int counter = 0;
		for(String line:lines){
			this.caracteres.put(counter, line);
			counter++;
			System.out.println(line);
		}
		
	}

	public ArrayList<String> detectColors(File file) throws Exception {
		
		rnd.setSeed(134134);
		
		int c;
		int red=0;
		int green=0;
		int blue=0;
				
		try {
			
			//Ontiene la imagen a calcular
			BufferedImage image = ImageIO.read(file);
		
			//Calcula los anchos y altos de los bloques
			int ancho;
			int alto;
			try {
				ancho = image.getWidth();
				alto = image.getHeight();
			} catch (Exception e){
				System.out.println(file.getName());
				e.printStackTrace();
				throw e;
			}
			
			//Resetea los colores
			red=0;
			green=0;
			blue=0;
			
			String rgb = "";
			
			ArrayList<String> lineas = new ArrayList<String>();
			
			//obtiene el color de cada pixel y acumula las cantidades
			for (int x=0; x<ancho;x++){
				
				String linea = "";
				for (int y=0; y<alto;y++){
					
					c = image.getRGB(x,y);
										
					red = ((c & 0x00ff0000) >> 16);
					green = ((c & 0x0000ff00) >> 8);
					blue = (c & 0x000000ff);
					
					red = red + rnd.nextInt(6);
					green = green + rnd.nextInt(6);
					blue = blue + rnd.nextInt(6);
					
					
					String cRed = caracteres.get(red);
					String cGreen = caracteres.get(green);
					String cBlue = caracteres.get(blue);
					
					linea = linea + cRed + cGreen+ cBlue;
					
				}				
				//System.out.println(linea);
				lineas.add(linea);
				
			}
			
						
			return lineas;
		} catch (Exception e){
			throw e;
		}
	}
	
	
}
