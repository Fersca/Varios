package colores;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;


/**
 * Detecta colores en imagenes
 * @author fscasserra
 */
public class ColorDetector {

	private static final int TAMANO_BLOQUE = 80;
	private static final double PORC_BORDE = 0.1;//0.05;
	private static final double PORC_CENTRO = 0.2;//0.125;
	
	public ImageInfo detectColors(String foto, Boolean isURL, Boolean createFiles) throws Exception {
					
		int c;
		int red=0;
		int green=0;
		int blue=0;
		
		//Crea las matrices para trabajar
		ArrayList<Bloque> bloques = new ArrayList<Bloque>();
		String[][] matriz = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		int[][] matrizColo = new int[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColoAprox = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColoAproxColor = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColoAproxColorNum = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColorBorde = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColorCentro = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		
		//limpia la matriz
		for (int i=0;i<TAMANO_BLOQUE;i++)
			for (int j=0;j<TAMANO_BLOQUE;j++)
				matriz[i][j]="-";
		
		try {

			//Ontiene la imagen a calcular
			BufferedImage image;
			if (isURL){
				URL url = new URL(foto);
				image = ImageIO.read(url);
			}
			else {
				File file = new File(foto);
				image = ImageIO.read(file);
			}
			
			//Calcula los anchos y altos de los bloques
			int ancho = image.getWidth();
			int alto = image.getHeight();
			int anchoBloque = ancho/TAMANO_BLOQUE;
			int altoBloque = alto/TAMANO_BLOQUE;
			
			//Llena la lista de bloques
			Bloque bloque;
			for (int row=0;row<=(TAMANO_BLOQUE-1);row++){
				for (int col=0;col<=(TAMANO_BLOQUE-1);col++){
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
				for (int x=(anchoBloque*blo.row); x<((anchoBloque*blo.row)+anchoBloque);x++){
					for (int y=(altoBloque*blo.col); y<((altoBloque*blo.col)+altoBloque);y++){
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
				
				//verifica los colores de cada bloque
				blo.verificar(createFiles);		
				
				//Guarda la informacion en las matrices
				if (createFiles){
					matriz[blo.col][blo.row] = blo.status;
					matrizColo[blo.col][blo.row] = blo.color;
					matrizColoAprox[blo.col][blo.row] = blo.statusAprox;
					matrizColoAproxColorNum[blo.col][blo.row] = ""+blo.colorAprox.numero;
				}
				
				//Esta matriz se necesita si o si
				matrizColoAproxColor[blo.col][blo.row] = blo.colorAprox.nombre;
				
				if (blo.col<(TAMANO_BLOQUE*PORC_BORDE) || blo.row<(TAMANO_BLOQUE*PORC_BORDE) || blo.col>=(TAMANO_BLOQUE*(1-PORC_BORDE)) || blo.row>=(TAMANO_BLOQUE*(1-PORC_BORDE))){
					matrizColorBorde[blo.col][blo.row] = blo.colorAprox.nombre;
				} else {
					if (blo.col<(TAMANO_BLOQUE*PORC_CENTRO) || blo.row<(TAMANO_BLOQUE*PORC_CENTRO) || blo.col>=(TAMANO_BLOQUE*(1-PORC_CENTRO)) || blo.row>=(TAMANO_BLOQUE*(1-PORC_CENTRO))) { // no hace nada con esta franja
					} else {
						matrizColorCentro[blo.col][blo.row] = blo.colorAprox.nombre;
					}
				}
			}
					
			//Cuenta la cantidad de colores 
			Map<String, Integer> colores 		= cuentaColores(matrizColoAproxColor);
			Map<String, Integer> coloresBorde 	= cuentaColores(matrizColorBorde);
			Map<String, Integer> coloresCentro 	= cuentaColores(matrizColorCentro);
			
			ImageInfo imageInfo = new ImageInfo();
			imageInfo.alto=alto;
			imageInfo.ancho=ancho;
			imageInfo.foto=foto;
			
			//Filtra los colores y deja solo los que aparecen mas			
			ArrayList<ColorDetected> detectados 		= detectar(colores, TAMANO_BLOQUE*TAMANO_BLOQUE);
			ArrayList<ColorDetected> detectadosBorde 	= detectar(coloresBorde, (int)(TAMANO_BLOQUE*TAMANO_BLOQUE)-(int)(TAMANO_BLOQUE*(1-(PORC_BORDE*2))*TAMANO_BLOQUE*(1-(PORC_BORDE*2)))); //576 = (40x40)-(36x36)
			ArrayList<ColorDetected> detectadosCentro 	= detectar(coloresCentro, (int)(TAMANO_BLOQUE*(1-(PORC_CENTRO*2))*TAMANO_BLOQUE*(1-(PORC_CENTRO*2)))); //30x30 del centro
			ArrayList<ColorDetected> detectadosProducto   = detectarProducto(detectadosBorde,detectadosCentro);
			
			if (createFiles){
				
				//Info de la imagen
				System.out.println(ancho+" - "+alto);
				System.out.println("anchobloque: "+anchoBloque);
				System.out.println("altobloque: "+altoBloque);
				
				//Crea el HTML de ejemplo
				FileHelper.createFile(matriz, matrizColoAprox, matrizColoAproxColor, matrizColoAproxColorNum, colores, detectados);
				
				//Crea la imagen en el disco de ejemplo
				FileHelper.createImgage(matrizColo, TAMANO_BLOQUE);
			}

			imageInfo.detectados=detectados;
			imageInfo.detectadosBorde=detectadosBorde;
			imageInfo.detectadosCentro=detectadosCentro;
			imageInfo.detectadosProducto=detectadosProducto;
			
			return imageInfo;
		} catch (Exception e){
			throw e;
		}
	}

	private ArrayList<ColorDetected> detectarProducto(
			ArrayList<ColorDetected> detectadosBorde,
			ArrayList<ColorDetected> detectadosCentro) {

		ArrayList<ColorDetected> detectadosProducto = new ArrayList<ColorDetected>(); 
			
		int queda = 100;
		
		//busca el color del centro que sea igual al maximo del borde:
		for (ColorDetected colorDetected : detectadosCentro) {
			if(colorDetected.nombre.equals(detectadosBorde.get(0).nombre)){
				queda = 100 - colorDetected.porcentage;  
				break;
			}
		}
	
		System.out.println("Color Borde: "+detectadosBorde.get(0).nombre+", queda: "+queda);
		
		//el 50% del centro es del mismo color del borde, debe ser un objeto grande, no sacar este color
		if (queda<=60){

			System.out.println("El centro es igual al borde");
			
			//Se copian los colores del centro al del producto
			for (ColorDetected colorDetected : detectadosCentro) {
				detectadosProducto.add(colorDetected);	
			}
			
		} else {

			ColorDetected color;
			int canti;
			for (ColorDetected colorDetected : detectadosCentro) {
				
				//si encuentro el mismo color del borde
				if(colorDetected.nombre.equals(detectadosBorde.get(0).nombre)){
					//Le descuento el total al color que voy a sacar
					color = new ColorDetected(colorDetected.nombre,0,0);
				} else {
					//recalcula el nuevo porcentaje
					canti = (colorDetected.porcentage*100) / queda;							
					color = new ColorDetected(colorDetected.nombre,canti,0);
				}

				detectadosProducto.add(color);
			}
			
		}
		
		Collections.sort(detectadosProducto);
		
		for (ColorDetected colorDetected : detectadosProducto) {
			System.out.println(colorDetected.nombre +" - " +colorDetected.porcentage);
		}
		
		return detectadosProducto;

	}

	private ArrayList<ColorDetected> detectar(Map<String, Integer> colores, int cant) {

		//double tamanio = TAMANO_BLOQUE*TAMANO_BLOQUE;
		double tamanio = (double)cant;
		
		ArrayList<ColorDetected> array = new ArrayList<ColorDetected>();
		
		Iterator<Entry<String, Integer>> it = colores.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();

	        if (pairs.getKey()!=null){

		        double porc = (double)pairs.getValue()/tamanio;
		        //System.out.println(cant+"--> "+pairs.getKey()+" - "+pairs.getValue());		        
	        	porc = porc * 100;
	        	ColorDetected col = new ColorDetected(pairs.getKey(),(int)porc,0);
	        	array.add(col);
	        		        	
	        }
	        
	    }
		
	    Collections.sort(array);
	    int i =1;
	    for (ColorDetected colorDetected : array) {
			colorDetected.orden=i;
			i++;
		}
	    
		return array;
	}

	private HashMap<String, Integer> cuentaColores(String[][] matrizColoAprox) {
		
		HashMap<String, Integer> tabla = new HashMap<String, Integer>();
		
		for (String[] s1 : matrizColoAprox) {
			for (String s2 : s1){
				if (s2!=null){
					if (tabla.containsKey(s2))
						tabla.put(s2, tabla.get(s2)+1);
					else
						tabla.put(s2, 1);
				}
			}
		}

		return tabla;
	}

	public static void main(String[] args) {

		ColorDetector border = new ColorDetector();
		try {
			//border.detectWhiteBorder("c:\\Users\\Fersca\\Pictures\\Colores\\prueba1.jpg",false,false);
			//border.detectWhiteBorder("http://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/GermanShep1_wb.jpg/250px-GermanShep1_wb.jpg",true,false);
			
			ImageInfo img1 = border.detectColors("http://bimg2.mlstatic.com/miralas-hermosisimas-musculosas-y-remerones-largos_MLA-F-3096619678_092012.jpg",true, false);
			ImageInfo img2 = border.detectColors("http://www.todohumor.com/UserFiles/Image/fondos/2010/Mayo/automoderno.jpg",true,true);
			ImageInfo img3 = border.detectColors("http://www.motorexperience.es/images/cars/ferrari458600.jpg",true,false);
			ImageInfo img4 = border.detectColors("http://www.w7swall.com/wp-content/wallpapers/cars/porche-carrera-porsche-510283.jpg",true,false);
			ImageInfo img5 = border.detectColors("http://img1.mlstatic.com/mini-cooper-s-hot-pepper-16-turbo-tomo-cuatri-parrillero_MLA-O-4656120149_072013.jpg",true,false);
			ImageInfo img6 = border.detectColors("http://img1.mlstatic.com/bmw-series-3-335i-m-sport-biturbo-steptronic_MLA-O-5085301356_092013.jpg",true,false);
			ImageInfo img7 = border.detectColors("http://bimg1.mlstatic.com/bmw-330-i-265cv-automatico-2010-impecable-idem-a-okm-permuto_MLA-F-4464781334_062013.jpg",true,false);
			ImageInfo img8 = border.detectColors("http://bimg2.mlstatic.com/bmw-335i-biturbo-sedan_MLA-F-5096800211_092013.jpg",true,false);
			ImageInfo img9 = border.detectColors("http://bimg1.mlstatic.com/fiat-uno-no-147-128-duna-palio-corsa-gol_MLA-F-5052769320_092013.jpg",true,false);
			ImageInfo img10 = border.detectColors("http://bimg2.mlstatic.com/hilux-sr-30-4x4-dc-ano-2008-oportunidad-_MLA-F-5075976690_092013.jpg",true,false);
			ImageInfo img11 = border.detectColors("http://bimg2.mlstatic.com/corsa-classic-16-n-super-vendo-o-permuto-_MLA-F-5053040292_092013.jpg",true,false);
			ImageInfo img12 = border.detectColors("http://bimg2.mlstatic.com/chrysler-pt-cruiser-24-classic_MLA-F-5045159418_092013.jpg",true,false);
			
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--NARANJA-----------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img1.detectadosBorde.get(0).nombre+" - "+img1.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img1.detectadosProducto.get(0).nombre.equals("NA"));
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--ROJO--------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img2.detectadosBorde.get(0).nombre+" - "+img2.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img2.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(img3.detectadosBorde.get(0).nombre+" - "+img3.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img3.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(img4.detectadosBorde.get(0).nombre+" - "+img4.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img4.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(img9.detectadosBorde.get(0).nombre+" - "+img9.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img9.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--GRIS--------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img5.detectadosBorde.get(0).nombre+" - "+img5.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img5.detectadosProducto.get(0).nombre.equals("GR"));
			System.out.print(img10.detectadosBorde.get(0).nombre+" - "+img10.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img10.detectadosProducto.get(0).nombre.equals("GR"));
			System.out.print(img11.detectadosBorde.get(0).nombre+" - "+img11.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img11.detectadosProducto.get(0).nombre.equals("GR"));			
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--NEGRO-------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img6.detectadosBorde.get(0).nombre+" - "+img6.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img6.detectadosProducto.get(0).nombre.equals("NE"));
			System.out.print(img7.detectadosBorde.get(0).nombre+" - "+img7.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img7.detectadosProducto.get(0).nombre.equals("NE"));
			System.out.print(img8.detectadosBorde.get(0).nombre+" - "+img8.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img8.detectadosProducto.get(0).nombre.equals("NE"));
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--BLANCO------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img12.detectadosBorde.get(0).nombre+" - "+img12.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img12.detectadosProducto.get(0).nombre.equals("BL"));

			ArrayList<ImageInfo> imagenes = new ArrayList<ImageInfo>();
			imagenes.add(img1);
			imagenes.add(img2);
			imagenes.add(img3);
			imagenes.add(img4);
			imagenes.add(img5);
			imagenes.add(img6);
			imagenes.add(img7);
			imagenes.add(img8);
			imagenes.add(img9);
			imagenes.add(img10);
			imagenes.add(img11);
			imagenes.add(img12);
			FileHelper.createSummary(imagenes, "/home/fersca/summary.html");

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}