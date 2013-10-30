package xuggler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ArraySerialization {

	public static void main(String[] args) throws Exception {
		
		ArrayList<ImageInfo> a = new ArrayList<ImageInfo>();

		ImageInfo b = new ImageInfo();
		ImageInfo c = new ImageInfo();
		ImageInfo d = new ImageInfo();
		
		b.red=10;
		c.red=20;
		d.red=30;
		
		a.add(b);
		a.add(c);
		a.add(d);
		
		ArraySerialization.serialiceArray(a,"/home/fersca/array.ser");
	
		ArrayList<ImageInfo> e = ArraySerialization.deSerialiceArray("/home/fersca/array.ser");
		
		for (ImageInfo imageInfo : e) {
			System.out.println("Rojo: "+imageInfo.red);	
		}
		
		System.out.println("Fin");
	}
	
	public static void serialiceArray(ArrayList<ImageInfo> fotos, String filename) throws Exception {
		
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(fotos);
        oos.close();

        System.out.println("Guardado");
        
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<ImageInfo> deSerialiceArray(String filename) {
		
		ArrayList<ImageInfo> fotos = null;
		
		try {
	        FileInputStream fis = new FileInputStream(filename);
	        ObjectInputStream ois = new ObjectInputStream(fis);
	        
			fotos = (ArrayList<ImageInfo>) ois.readObject();
	        ois.close();
	        
	        System.out.println("Cargado: "+fotos.size());
	        
			
		} catch (Exception e){
	        System.out.println("Error cargando archivo");
		}
		
		return fotos;
	}
}
