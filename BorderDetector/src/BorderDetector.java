import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;


/**
 * Detecta colores en imágenes.
 * @author fscasserra
 */
public class BorderDetector {

	private static final int TAMANO_BLOQUE = 32; //1024 bloques, si lo pongo en una red neural, tendria 1024 neuronas de entrada, esta ok

	public static void main(String[] args) {

		BorderDetector border = new BorderDetector();
		try {
			border.detectWhiteBorder("http://upload.wikimedia.org/wikipedia/commons/c/c1/American-Eskimo-dog.jpg",true,false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fin.");
		System.exit(0);
	}
	
	class Bloque {
		public Bloque(int row, int col) {
			this.row=row;
			this.col=col;
		}
		public int row;
		public int col;
		public int red=0;
		public int green=0;
		public int blue=0;
		public String status;
		
		public void verificar() {
			
			//status="<td bgcolor=\"rgb("+red+","+green+","+blue+")\">#</td>";
			status="<td style=\"background-color: rgb("+red+","+green+","+blue+"); color: rgb("+red+","+green+","+blue+")\">#</td>";
			/*
			//Detecta Blanco
			if (red>=230 && green>=230 && blue>=230){
				status="<td style=\"background-color: white; color: white\">#</td>";
			} 
			//Detecta Negro
			else if (red<20 && green<20 && blue<20){
				status="<td style=\"background-color: black; color: black\">#</td>";
			}
			//Detecta rojo
			else if (red>160 && green<60 && blue <60){
				status="<td style=\"background-color: red; color: red\">#</td>";
			} 
			//Detecta verde
			else if (red<100 && green>160 && blue<100){
				status="<td style=\"background-color: green; color: green\">#</td>";
			} 
			//Detecta azul
			else if (red<150 && green<150 && blue>200){
				status="<td style=\"background-color: blue; color: blue\">#</td>";
			} 
			//Detecta Amarillo
			else if (red>180 && green>180 && blue<20){
				status="<td style=\"background-color: yellow; color: yellow\">#</td>";
			} 
			//Detecta Naranja
			else if (red>170 && green>60 && green<170 && blue<70){
				status="<td style=\"background-color: orange; color: orange\">#</td>";
			} 
			//Detecta Violeta
			else if (red>80 && green<50 && blue>80){
				status="<td style=\"background-color: violet; color: violet\">#</td>";
			} 
			//Detecta Celeste
			else if (red<70 && green>150 && blue>150){
				status="<td style=\"background-color: aqua; color: aqua\">#</td>";
			} 

			else 
				status="<td style=\"background-color: white; color: white\">#</td>";
			
			*/
		}
	}

	class httpAuthenticateProxy extends Authenticator {

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication("guest","mercadolibre".toCharArray());
		}
	} 

	public int detectWhiteBorder(String foto, boolean isURL, boolean useMLproxy) throws Exception {

		//auttenticaci�n de proxy
		if (isURL && useMLproxy){
			Authenticator.setDefault( new httpAuthenticateProxy() );
			System.setProperty("http.proxyHost", "10.200.1.19");
			System.setProperty("http.proxyPort", "3128");
		}
				
		int c;
		int red=0;
		int green=0;
		int blue=0;
		
		ArrayList<Bloque> bloques = new ArrayList<Bloque>();
		String[][] matriz = new String[(int)TAMANO_BLOQUE][(int)TAMANO_BLOQUE];

		//limpia la matriz
		for (int i=0;i<TAMANO_BLOQUE;i++)
			for (int j=0;j<TAMANO_BLOQUE;j++)
				matriz[i][j]="-";
		
		try {

			BufferedImage image;
			
			if (isURL){
				URL url = new URL(foto);
				image = ImageIO.read(url);
			}
			else {
				File file = new File(foto);
				image = ImageIO.read(file);
			}
			
			int ancho = image.getWidth();
			int alto = image.getHeight();
			
			System.out.println(ancho+" - "+alto);
			
			/*
			 * Existe un problema al calcular el ancho del bloque, si la division es con coma,
			 * lo que pasa es que se van haciendo bloquecitos chicos que al final no calcular la foto total
			 * porque no llegan al final cuando se va avanzando.
			 * Lo que termina pasando es que la imagen final es una imagen cuadrada pero que quizas le faltan los ultimos cuadros 
			 * cuando menos sean los bloques, menos pasa, porque la parte decimal influye menos. :D
			 * 
			 */
			
			int anchoBloque = ancho/TAMANO_BLOQUE;
			int altoBloque = alto/TAMANO_BLOQUE;
			
			System.out.println("anchobloque: "+anchoBloque);
			System.out.println("altobloque: "+altoBloque);
			
			//Llena la lista de bloques
			Bloque bloque;
			for (int row=0;row<=(TAMANO_BLOQUE-1);row++){
				for (int col=0;col<=(TAMANO_BLOQUE-1);col++){
					bloque = new Bloque(row, col);
					bloques.add(bloque);
					/* solo para el borde
					if (row==0||row==(TAMANO_BLOQUE-1)||col==0||col==(TAMANO_BLOQUE-1)){
						bloque = new Bloque(row, col);
						bloques.add(bloque);
					} 
					*/
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
				//Marca como correcto o no cada bloque
				blo.verificar();		
				//Guarda el blque en la matriz
				matriz[blo.col][blo.row] = blo.status;
				
			}
					
			//Imprime el archivo de ejemplo
			StringBuilder bu = new StringBuilder(); 
			bu.append("<!DOCTYPE html>");
			bu.append("<html>");
			bu.append("<head>");
			bu.append("<title>Title of the document</title>");
			bu.append("</head>");
			bu.append("<body>");
			bu.append("<table style=\"border:0;\">");
			for (String[] strings : matriz) {
				bu.append("<tr>");
				for (String pos : strings) {
					bu.append(pos);
				}
				bu.append("</tr>");
			}
			bu.append("</table>");
			bu.append("</body>");
			bu.append("</html>");
						
			Writer output = null;
			File file = new File("/home/fersca/imagen.html");
			output = new BufferedWriter(new FileWriter(file));
			output.write(bu.toString());
			output.close();
			
			/*
			int totalBloques = (4*TAMANO_BLOQUE)-4;
			int cantBlancos=totalBloques;

			for (Bloque bl : bloques) {
				if (bl.status.equals("X")){
					cantBlancos--;
				}
			}
			*/
			/* 
			//Imprime el resultado
			boolean bordeBlanco=true;
			for (Bloque bl : bloques) {
				if (bl.status.equals("X")){
					bordeBlanco=false;
					break;
				}
			}
			*/
			
			/*
			 
			//Perfectas
			if (cantBlancos==totalBloques){
				return 1;
			}
			//Regulares
			if (cantBlancos>(totalBloques/1.5)){
				return 2;
			}
			
			//Muy malas
			return 3;
			
			if (bordeBlanco){
				//System.out.println("OK - Imagen con Borde Blanco");
				return true;
			}
			else {
				//System.out.println("Fail - El borde no es totalmente Blanco");
				return false;
			}
			*/
			return 0;
		} catch (Exception e){
			throw e;
		}
	}
	
	

}