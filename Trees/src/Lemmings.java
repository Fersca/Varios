import java.util.ArrayList;



public class Lemmings {

	public static void main(String[] args) {

		Lemmings lem = new Lemmings();
		long a = System.currentTimeMillis();
		lem.run();
		long b = System.currentTimeMillis();
		System.out.println("ms: "+(b-a));

	}
		
	private void run() {
	
		//total de caminos
		int total=0;
		
		for(int i = 1;i<=25;i++){
			
			//obtengo la combinacion de tableros 
			ArrayList<ArrayList<Tablero>> combinaciones = partirTableroEnPartes(i);
			
			//por cada una de las combiaciones
			for (ArrayList<Tablero> tableros : combinaciones) {
				
				//obtengo el tablero
				for (Tablero t : tableros) {
					
					//calculo los caminos
					int caminos = calcularCaminos(t);
					
					total = total + caminos;
					
				}
				
			}
			
		}
		
		System.out.println("Total: "+total);
		
	}

	private int calcularCaminos(Tablero t) {
		// TODO Auto-generated method stub
		return 0;
	}

	private ArrayList<ArrayList<Tablero>> partirTableroEnPartes(int i) {

		ArrayList<ArrayList<Tablero>> combinaciones = new ArrayList<ArrayList<Tablero>>();
		
		
		
		
		return combinaciones;
	}		

}
