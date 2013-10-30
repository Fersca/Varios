package xuggler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Detecta colores en imagenes
 * @author fscasserra
 */
public class ColorDetector {
	
	public ArrayList<Bloque> obtieneMatriz(BufferedImage image, int fotosAncho, int fotosAlto) throws Exception {
				
		int c;
		int red=0;
		int green=0;
		int blue=0;
		
		//Crea las matrices para trabajar
		ArrayList<Bloque> bloques = new ArrayList<Bloque>();
		String[][] matrizColorNet = new String[fotosAncho][fotosAlto];
				
		try {
			
			//Calcula los anchos y altos de los bloques
			int ancho = image.getWidth();
			int alto = image.getHeight();
			int anchoBloque = ancho/fotosAncho;
			int altoBloque = alto/fotosAlto;
			
			//Llena la lista de bloques
			Bloque bloque;
			for (int row=0;row<=(fotosAlto-1);row++){
				for (int col=0;col<=(fotosAncho-1);col++){
					bloque = new Bloque(row, col);
					bloques.add(bloque);
				}
			}
			
			//Calcula el promedio de color de cada bloque
			for (Bloque blo : bloques) {
				//Resetea los colores
				red=0;
				green=0;
				blue=0;				
				//obtiene el color de cada pixel y acumula las cantidades
				for (int x=(anchoBloque*blo.col); x<((anchoBloque*blo.col)+anchoBloque);x++){
					for (int y=(altoBloque*blo.row); y<((altoBloque*blo.row)+altoBloque);y++){
						c = image.getRGB(x,y);
						red = red + ((c & 0x00ff0000) >> 16);
						green = green + ((c & 0x0000ff00) >> 8);
						blue = blue +(c & 0x000000ff);
					}
				}
				//guarda el promedio de colores
				int superficieBloque = anchoBloque*altoBloque;
				blo.red=red/superficieBloque;
				blo.green=green/superficieBloque;
				blo.blue=blue/superficieBloque;
								
			}
											
			return bloques;
		} catch (Exception e){
			throw e;
		}
			
		
	}
	
	
	public ImageInfo detectColors(File file, String fotoURL) throws Exception {
					
		int c;
		int red=0;
		int green=0;
		int blue=0;
				
		try {
			
			//Ontiene la imagen a calcular
			BufferedImage image;
			if (fotoURL!=null){
				URL url = new URL(fotoURL);
				image = ImageIO.read(url);
			}
			else {
				image = ImageIO.read(file);
			}
			
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
			if (fotoURL!=null){
				imageInfo.filename=fotoURL;
			} else {
				imageInfo.filename=file.getName();
			}
			
			return imageInfo;
		} catch (Exception e){
			throw e;
		}
	}
	
}