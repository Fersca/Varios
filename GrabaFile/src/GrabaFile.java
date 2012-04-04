import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GrabaFile {

	public static void main(String[] args) {
	
		Long numero = Long.parseLong(args[1]);
		String file = args[0];
		String accion = args[2];
		
		GrabaFile gf = new GrabaFile();
		
		if (accion.equals("graba")){
			gf.graba(numero, file);
		} else if (accion.equals("lee")){
			gf.read(file);
		} else {
			System.out.println("nada");
		}
		
	}
	
	private void graba(Long numero, String archivo) {

		ObjectOutputStream ois = null;
		try {
			//ois = new ObjectOutputStream(new FileOutputStream("/netapp4/atlas/Atlas/CustIDRanges/secuence.serialize"));
			ois = new ObjectOutputStream(new FileOutputStream(archivo));
			ois.writeLong(numero);
			ois.close();
			System.out.println("LISTO");
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	public void read(String path)  {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(path));
			Long lon = ois.readLong();
			System.out.println("Numero:" + lon);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}	
	
}
