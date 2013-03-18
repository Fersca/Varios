package xuggler;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


/**
 * Detecta colores en imagenes
 * @author fscasserra
 */
public class ColorDetector {
	
	public ImageInfo detectColors(File file) throws Exception {
					
		int c;
		int red=0;
		int green=0;
		int blue=0;
				
		try {

			//Ontiene la imagen a calcular
			BufferedImage image = ImageIO.read(file);
			
			//Calcula los anchos y altos de los bloques
			int ancho = image.getWidth();
			int alto = image.getHeight();
						
			//Resetea los colores
			red=0;
			green=0;
			blue=0;				
			//obtiene el color de cada pixel y acumula las cantidades
			for (int x=0; x<ancho;x++){
				for (int y=0; y<alto;y++){
					c = image.getRGB(x,y);
					red = red + ((c & 0x00ff0000) >> 16);
					green = green + ((c & 0x0000ff00) >> 8);
					blue = blue +(c & 0x000000ff);
				}
			}
			//guarda el promedio de colores
			int superficieBloque = ancho*alto;
			red=red/superficieBloque;
			green=green/superficieBloque;
			blue=blue/superficieBloque;
				
			//Devuelve el resultado
			ImageInfo imageInfo = new ImageInfo();
			imageInfo.red=red;
			imageInfo.green=green;
			imageInfo.blue=blue;
			imageInfo.filename=file.getName();
			
			return imageInfo;
		} catch (Exception e){
			throw e;
		}
	}
	
}