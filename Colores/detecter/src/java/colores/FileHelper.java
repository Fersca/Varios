package colores;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class FileHelper {

	public static void createFile(String[][] matriz,String[][] matrizColoAprox, String[][] matrizColoAproxColor,String[][] matrizColoAproxColorNum, Map<String, Integer> colores, List<ColorDetected> detectados) throws IOException{
		
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
		bu.append("<table style=\"border:0;\">");
		for (String[] strings : matrizColoAprox) {
			bu.append("<tr>");
			for (String pos : strings) {
				bu.append(pos);
			}
			bu.append("</tr>");
		}
		bu.append("</table>");	
		bu.append("<table style=\"border:0;\">");
		for (String[] strings : matrizColoAproxColor) {
			bu.append("<tr>");
			for (String pos : strings) {
				bu.append("<td>"+pos+"</td>");
			}
			bu.append("</tr>");
		}
		bu.append("</table>");	
		bu.append("<table style=\"border:0;\">");
		bu.append("<table style=\"border:0;\">");
		for (String[] strings : matrizColoAproxColorNum) {
			bu.append("<tr>");
			for (String pos : strings) {
				bu.append("<td>"+pos+"</td>");
			}
			bu.append("</tr>");
		}
		bu.append("</table>");	
		bu.append("<table style=\"border:0;\">");
	    
		Iterator<Entry<String, Integer>> it = colores.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
			bu.append("<tr><td>");
			bu.append(pairs.getKey() + " = " + pairs.getValue());
			bu.append("</td></tr>");
	    }
		
		bu.append("</table>");
		bu.append("<table style=\"border:0;\">");

		for (ColorDetected colorDetected : detectados) {
			bu.append("<tr><td>");
			bu.append(colorDetected.orden+" - "+colorDetected.nombre + " - " + colorDetected.porcentage+"%");
			bu.append("</td></tr>");				
		}
		
		bu.append("</table>");
		bu.append("<table style=\"border:0;\">");
		int num=42;
		int col=0;
		for (int i=0;i<256;i=i+num) {
			for (int j=0;j<256;j=j+num) {
				for (int k=0;k<256;k=k+num) {
					bu.append("<tr>");
					bu.append("<td style=\"background-color: rgb("+i+","+j+","+k+"); color: rgb("+i+","+j+","+k+")\">#</td>");
					bu.append("<td>Color col"+col+" = new Color("+i+","+j+","+k+",\"\""+");</td>");
					bu.append("</tr>");	
					col++;
				}
			}
		}
		
		bu.append("</table>");		
		for (int i = 0;i<343;i++){
			bu.append("lista.add(col"+i+");<br>");	
		}
		bu.append("</body>");
		bu.append("</html>");
					
		Writer output = null;
		File file = new File("/home/fersca/result.html");
		output = new BufferedWriter(new FileWriter(file));
		output.write(bu.toString());
		output.close();
		
	}

	public static void createSummary(ArrayList<ImageInfo> imagenes, String file) throws IOException{
		
		StringBuilder bu = new StringBuilder(); 
		bu.append("<!DOCTYPE html>");
		bu.append("<html>");
		bu.append("<head>");
		bu.append("<title>Title of the document</title>");
		bu.append("</head>");
		bu.append("<body>");
		bu.append("<table style=\"border:0;\">");
		
		for (ImageInfo foto : imagenes) {
			bu.append("<tr>");
			bu.append("<td>");
			bu.append("<img src=\""+foto.foto+"\" height=\"250\" width=\"250\">");
			bu.append("</td>");
			bu.append("<td>"+foto.detectadosProducto.get(0).nombre+"</td>");
			bu.append("</tr>");
		}
		
		bu.append("</table>");
		bu.append("</body>");
		bu.append("</html>");
					
		Writer output = null;
		File file1 = new File(file);
		output = new BufferedWriter(new FileWriter(file1));
		output.write(bu.toString());
		output.close();
		
	}
	
	
	public static void createImgage(int[][] matriz, int TAMANO_BLOQUE) throws IOException {

		BufferedImage off_Image =new BufferedImage(TAMANO_BLOQUE, TAMANO_BLOQUE, BufferedImage.TYPE_INT_RGB);
		//El formato a continuacion es 0xTTRRGGBB, tt es transparency, va en FF, el resto son los colores
		//ver esta pagina:  http://world-editor-tutorials.thehelper.net/cat_usersubmit.php?view=40844
		//int color = 0xFF00FF00; //verde
		
		//hacer un metodo que me multipleque por base 16 cada uno de los valores... asi tengo el integer.
		
		//int color = convert("FF00FF00"); //verde
		
		for (int i=0;i<TAMANO_BLOQUE;i++){
			for (int j=0;j<TAMANO_BLOQUE;j++){
				off_Image.setRGB(i, j, matriz[j][i]);
			}
		}
	    File outputfile = new File("/home/fersca/prueba4.png");
	    ImageIO.write(off_Image, "png", outputfile);
		
	}
	

}
