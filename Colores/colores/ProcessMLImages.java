package colores;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author fscasserra
 *
 */
public class ProcessMLImages {

	public static void main(String[] args) {

		ProcessMLImages proc = new ProcessMLImages();
		proc.go();
		System.exit(0);

	}

	private void go() {
		
		String [] ext = {"jpg"};
		File dir = new File("c:\\fotpic");
		Collection<File> files = (Collection<File>)FileUtils.listFiles(dir, ext, true);
		
		ColorDetector bd = new ColorDetector();
		int result;
		File feas = new File("c:\\foto\\feas");
		File lindas = new File("c:\\foto\\lindas");
		File regulares = new File("c:\\foto\\regulares");
		
		try {
			for (File foto : files) {
				/*
				result = bd.detectColors(foto.getPath(), false, false);
				if (result==1)
					FileUtils.copyFileToDirectory(foto, lindas);
				if (result==2)
					FileUtils.copyFileToDirectory(foto, regulares);
				if (result==3)
					FileUtils.copyFileToDirectory(foto, feas);
				*/
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
