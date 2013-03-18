package xuggler;

import java.util.ArrayList;

public class FindBestMach {

	public static void main(String[] args) throws Exception {

		FindBestMach fbm = new FindBestMach();
		//obtiene la imagen a comparar
		ImageInfo image = new ImageInfo();
		image.red=180;
		image.green=100;
		image.blue=140;

		fbm.getBestImage(image);
		
	}

	ArrayList<ImageInfo> array = ArraySerialization.deSerialiceArray("/home/fersca/peliculas/starwars.ser");
	
	public void getBestImage(ImageInfo image) throws Exception {
		
		double min = 100000;
		double diff;
		ImageInfo best=new ImageInfo();
		
		for (ImageInfo imageInfo : array) {
			//busca la imagen mas parecida
			diff = difference(image.red, image.green, image.blue, imageInfo.red, imageInfo.green, imageInfo.blue);
			System.out.println(diff+" - "+imageInfo.filename);
			
			if (diff<min){
				min=diff;
				best=imageInfo;
			}
					
		}
		
		System.out.println("Minimo: "+min+" - "+best.filename);
		
	}

	public double difference(int red, int green, int blue, int r, int g, int b){
		
		int difRed = red - r;
		int difGreen = green - g;
		int difBlue = blue - b;
		
		if (difRed<0) difRed = difRed * -1;
		if (difGreen<0) difGreen = difGreen * -1;
		if (difBlue<0) difBlue = difBlue * -1;
		
		return Math.sqrt((difRed*difRed)+(difGreen*difGreen)+(difBlue*difBlue));
	}
}
