package xuggler;

import java.io.File;
import java.util.ArrayList;

/**
 * Esta clase calcula el color de las imagenes
 *
 * @author fersca
 *
 */
public class ProcessImages {

	public static void main(String[] args) throws Exception {
		ProcessImages pi = new ProcessImages();
		pi.run();
	}
	
	private void run() throws Exception{
		
		//obtiene los archivos de imagenes.
		File folder = new File("/home/fersca/pelis/monster2/mivi/low");
		File[] files = folder.listFiles(); 		 

		ArrayList<ImageInfo> array = new ArrayList<ImageInfo>();
		
		ColorDetector cd = new ColorDetector();
		int cont=0;
		
		//para cada uno de ellos detecta los colores
		for (File file : files) {
			
			ImageInfo image = cd.detectColors(file,null);
			array.add(image);
			cont++;
			System.out.println("Listo: "+cont+" - "+file.getName());
		}
						
		//guarda el array en disco
		ArraySerialization.serialiceArray(array, "/home/fersca/pelis/monster2/mivi/low/monster.ser");
		
	}
	
}
