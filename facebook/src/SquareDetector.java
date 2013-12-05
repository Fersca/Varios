import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class SquareDetector {

	public static void main(String[] args) throws IOException {
		SquareDetector sd = new SquareDetector();
		sd.run();
	}

	private class dto {
		int[][] array;
		int tamano;
	}
	private void run() throws IOException {

		//leer el archivo
		ArrayList<dto> cases = leer();
		
		ArrayList<String> resultados = new ArrayList<String>();
		
		int i=1;
		//para cada Caso
		for (dto caso : cases) {
						
			for(int x=0;x<caso.tamano;x++){
				for(int y=0;y<caso.tamano;y++){
					System.out.print(caso.array[x][y]);
				}				
				System.out.println("");
			}
			
			if (detecta(caso.array,caso.tamano)){
				resultados.add(resultado("YES",i));
			} else {
				resultados.add(resultado("NO",i));
			}
			
			System.out.println("Fin caso");
			
			i++;
		}
		
		grabarResultado(resultados);
				
	}	
	

	private boolean detecta(int[][] array, int tam) {

		for(int x=0;x<tam;x++){
			for(int y=0;y<tam;y++){
				//recorre hasta llegar al primero pintado
				if(array[x][y]==2){
					
					boolean empieza=true;
					int cant=1; //cant es el lado del cudrado
					int y1=y+1;
					while(empieza && (y1<tam)){
						
						if(array[x][y1]==1){
							empieza=false;
						} else {
							cant++;
						}
						y1++;
					}
					//hay un solo blank en la linea, mal
					if (cant==1)
						return false;
					
					for(int x2=0;x2<cant;x2++){
						for(int y2=0;y2<cant;y2++){
							
							if(array[x+x2][y+y2]==1){
								return false; //porque seria incorrecto
							} else {
								array[x+x2][y+y2]=1;
							}
							
						}
					}
					
					for(int x3=0;x3<tam;x3++){
						for(int y3=0;y3<tam;y3++){
							if(array[x3][y3]==2){
								return false;
							}
						}
					}

					return true;
					
				}
			}				
		}

		return false;
		
	}


	private String resultado(String resut, int i) {
		
		return "Case #"+i+": "+resut;	
		
	}	
	
	private ArrayList<dto> leer() throws IOException {
						
	    FileInputStream fstream = new FileInputStream("/home/fersca/code/Varios/facebook/square_detector.txt");
	    
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    
	    //linea 1
	    int cantTests = Integer.parseInt(br.readLine());
	   
	    //casos
	    ArrayList<dto> input = new ArrayList<dto>();
	    
	    //procesa los test cases
	    for (int o=0;o<cantTests;o++){

	    	//tananio de larray
    		int tamanioArray = Integer.parseInt(br.readLine());    		
    		int[][] array = new int[tamanioArray][tamanioArray];

	    	//genera todo el array de caso
    		for(int i=0;i<tamanioArray;i++){
    			String linea = br.readLine();
    			//recore las lineas y las pone en el array
    			for(int j=0;j<linea.length();j++){
    				if ((""+linea.charAt(j)).equals("#"))
    					array[i][j]=2;
    				else
    					array[i][j]=1;	    				
    			}
    		}
	    			    
    		dto d = new dto();
    		d.array=array;
    		d.tamano=tamanioArray;
	    	input.add(d);
	    	
	    }
	   
	    in.close();

	    return input;

	}
	
	private void grabarResultado(ArrayList<String> resultados) throws FileNotFoundException {
		
		PrintStream out = null;
		try {
		    out = new PrintStream(new FileOutputStream("/home/fersca/code/Varios/facebook/square_detector1.txt"));
		    for (String string : resultados) {
		    	System.out.println(string);
		    	out.println(string);	
			}
		}
		finally {
		    if (out != null) out.close();
		}
		
	}
	
	

}
