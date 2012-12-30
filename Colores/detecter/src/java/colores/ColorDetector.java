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

	private static final int TAMANO_BLOQUE = 40;
	
	public ArrayList<Object> detectColors(String foto, Boolean isURL, Boolean createFiles) throws Exception {
					
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
			}
					
			//Cuenta la cantidad de colores 
			Map<String, Integer> colores = cuentaColores(matrizColoAproxColor);
			
			//Filtra los colores y deja solo los que aparecen mas
			ArrayList<Object> detectados = new ArrayList<Object>(); 
			
			//se agrega el ancho y el alto
			Integer ancho1 = ancho;
			Integer alto1 = alto;
			
			detectados.add(ancho1);
			detectados.add(alto1);
			
			//se agrega los colores
			ArrayList<ColorDetected> coloresDetectados = detectar(colores);
			
			detectados.addAll(coloresDetectados);
			//ArrayList<ColorDetected> detectados = detectar(colores);
			
			if (createFiles){
				
				//Info de la imagen
				System.out.println(ancho+" - "+alto);
				System.out.println("anchobloque: "+anchoBloque);
				System.out.println("altobloque: "+altoBloque);
				
				//Crea el HTML de ejemplo
				FileHelper.createFile(matriz, matrizColoAprox, matrizColoAproxColor, matrizColoAproxColorNum, colores, coloresDetectados);
				
				//Crea la imagen en el disco de ejemplo
				FileHelper.createImgage(matrizColo, TAMANO_BLOQUE);
			}

			return detectados;
		} catch (Exception e){
			throw e;
		}
	}

	private ArrayList<ColorDetected> detectar(Map<String, Integer> colores) {

		double tamanio = TAMANO_BLOQUE*TAMANO_BLOQUE;
		
		ArrayList<ColorDetected> array = new ArrayList<ColorDetected>();
		
		Iterator<Entry<String, Integer>> it = colores.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();

	        double porc = (double)pairs.getValue()/tamanio;
	        
	        //if (porc>0.15){
	        	porc = porc * 100;
	        	ColorDetected col = new ColorDetected(pairs.getKey(),(int)porc,0);
	        	array.add(col);
	        //}
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
				if (tabla.containsKey(s2))
					tabla.put(s2, tabla.get(s2)+1);
				else
					tabla.put(s2, 1);
			}
		}
		
		return tabla;
	}

	public static void main(String[] args) {

		ColorDetector border = new ColorDetector();
		try {
			//border.detectWhiteBorder("c:\\Users\\Fersca\\Pictures\\Colores\\prueba1.jpg",false,false);
			//border.detectWhiteBorder("http://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/GermanShep1_wb.jpg/250px-GermanShep1_wb.jpg",true,false);
			border.detectColors("http://bimg2.mlstatic.com/miralas-hermosisimas-musculosas-y-remerones-largos_MLA-F-3096619678_092012.jpg",true, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fin.");
		System.exit(0);
	}
	
}