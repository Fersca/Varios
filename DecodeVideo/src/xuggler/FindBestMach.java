package xuggler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class FindBestMach {

	public static void main(String[] args) throws Exception {

		FindBestMach fbm = new FindBestMach();
		ColorDetector cd = new ColorDetector();
		
		//imagen a calcular
		String foto = "https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-prn1/946499_385532184890466_1567347820_n.jpg";
		
		//obtiene la imagen
		URL url = new URL(foto);
		BufferedImage image = ImageIO.read(url);
		
		//cantidad de imagenes de lado
		int cantFotosAncho = 60;
		repetitions=200;

		//obtiene la conf de la pelicula
		Movie movie = MovieConfig.getMovie("Monster");
		array = ArraySerialization.deSerialiceArray(movie.colorFile);		
		int anchoFotos = movie.ancho; 
		int altoFotos = movie.alto;

		//tamanio de las imagenes ejemplo con 80 y 60
		int ancho =image.getWidth();
		int alto  =image.getHeight();
		double pixelsEnAncho = ancho/cantFotosAncho; //48 
		double proporcios    = anchoFotos/pixelsEnAncho;// 1.6666
		double pixelsEnAlto  = altoFotos/proporcios;//36
		int cantFotosAlto  = (int)(alto/pixelsEnAlto);//30
		
		System.out.println("Calcular cuadros: "+cantFotosAncho*cantFotosAlto);
		
		//dividir la foto en array con color promedio
		ArrayList<Bloque> bloques = cd.obtieneMatriz(image,cantFotosAncho, cantFotosAlto); //3x3
		
		//para cada cuadradito, buscar la imagen mas parecida y la deja en el bloque
		for (Bloque bloque : bloques) {
			fbm.getBestImage(bloque);			
		}
		
		//genera una imagen en blanco
		BufferedImage off_Image =new BufferedImage(anchoFotos*cantFotosAncho, altoFotos*cantFotosAlto, BufferedImage.TYPE_INT_RGB);

		//define el centro de coordenadas
		int ceroX = 0;
		int ceroY = 0;
		
		System.out.println("Calculando Imagenes");
		
		//para cada cuadradito, buscar la imagen mas parecida y la deja en el bloque
		for (Bloque bloque : bloques) {

			//obtiene la imagen del archivo
			File file = new File(movie.directory+bloque.imagenSimilar.filename);
			BufferedImage image2 = ImageIO.read(file);

			//obtiene el ancho y el alto
			int ancho2 = image2.getWidth();
			int alto2 = image2.getHeight();

			//incrementa el centro del coordenadas
			ceroX=ancho2*bloque.col;
			ceroY=alto2*bloque.row;
			
			//copia cada pixel de la imagen en las nuevas coordenadas
			for(int i=0;i<ancho2;i++){
				for(int j=0;j<alto2;j++){
					
					//obtiene el color del pixel
					int color = image2.getRGB(i,j);
					
					//lo pega en la imagen destino
					try {
						off_Image.setRGB(ceroX+i, ceroY+j, color);
					} catch (Exception e){
						System.out.println(ceroX+i+" - "+ceroY+j);
						System.out.println(ceroX+i+" - "+ceroY+j);
					}
				}
			}
			
		}
		
		System.out.println("Grabando Archivo");
		
		//pasa la imagen al archivo final
	    File outputfile = new File("/home/fersca/pelis/peques/prueba4.png");
	    ImageIO.write(off_Image, "png", outputfile);
	    System.out.println("Fin");
				
	}

	//array con la info de las imagenes serializadas
	static ArrayList<ImageInfo> array;
	static int repetitions;
	
	//busca la imagen serializada que mas se parece al bloque
	public void getBestImage(Bloque image) throws Exception {
		
		double min = 100000;
		double diff;
		ImageInfo best=new ImageInfo();
		
		int bestPos = 0;
		
		for (int arrayPos=0;arrayPos<array.size();arrayPos++) {
		
			ImageInfo imageInfo = array.get(arrayPos);
			
			//si repitio mas de X veces usa otra
			if (imageInfo.repetitions==repetitions)
				continue;
			
			//busca la imagen mas parecida
			diff = difference(image.red, image.green, image.blue, imageInfo.red, imageInfo.green, imageInfo.blue);
			//System.out.println(diff+" - "+imageInfo.filename);
			
			if (diff<min){
				min=diff;
				best=imageInfo;
				bestPos = arrayPos;
			}
			
			arrayPos++;
					
		}

		array.get(bestPos).repetitions++;
		
		image.imagenSimilar=best;
		
	}

	//calcula la diferencia de colores
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
