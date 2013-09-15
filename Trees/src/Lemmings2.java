/**
 * Mueve fichas por el trablero, dependiendo si hay un lugar libre
 * de hacer todas las combinaciones habria 4 a la 100 opciones, asi que muevo solo donde hay lugar
 * @author fersca
 *
 */
public class Lemmings2 {

	public static void main(String[] args) {

		Lemmings2 l;
		//Lo pruebo para 6, para 10 tarda una eternidad
		for (int i=1;i<=10;i++){
			l = new Lemmings2();
			l.run(i);			
		}

		
	}

	//cantidad de opciones
	int cant=0;
	
	//cantidad de fichas
	int tam;
	int lado; 
	 	 
	private void run(int lado) {

		this.lado=lado;
		
		//cantidad de fichas
		this.tam = lado * lado;
		
		//Fichas a mover 
		int[] tablero = new int[tam];   
		
		//Fichas movidas
		int[] usadas = new int[tam];

		//llena el tablero
		llenaTablero(tablero);
		
		//recorre el grafo
		recorre(tablero, usadas);
		
		System.out.println("Cantidad de movimientos lado "+this.lado+": "+ this.cant);
		
	}

	private void llenaTablero(int[] tablero) {
		for (int i = 0; i < tam; i++) {
		    tablero[i] = i+1;
		}
	}

	private void recorre(int[] tablero, int[] usadas) {
	    
		//verifica todas las fichas a ver cual mover
		for (int i = 0; i < tam; i++) {
			
			//si en la posicion hay ficha
	        if (tablero[i] > 0) {
	        	
	        	//intenta mover la ficha
	            derecha(tablero, usadas, i);
	            
	            izquierda(tablero, usadas, i);
	            
	            arriba(tablero, usadas, i);
	            
	            abajo(tablero, usadas, i);
	            
	            break;
	        }
	    }
		
	    //si queda alguna posicion vacia esta mal y corta
	    for (int j = 0; j < tam; j++) {
	        if (usadas[j] == 0) {
	            return;
	        }
	    }

	    this.cant++;
	}

	private void derecha(int[] tablero, int[] usadas, int i) {
		//mueve derecha, si no esta en el borde y la pos futura está vacia y (es el primero o no intercabia con otro)
		if ((i % lado < lado - 1) && (usadas[i + 1] == 0) && ((usadas[i] == 0) || (usadas[i] != tablero[i] + 1))) {
		    //usa ficha
			usadas[i + 1] = tablero[i];
		    //blanquea
			tablero[i] = 0;
		    //proxima ficha
		    recorre(tablero, usadas);
		    //revierte
		    tablero[i] = usadas[i + 1];
		    usadas[i + 1] = 0;
		}
	}

	private void abajo(int[] tablero, int[] usadas, int i) {
		//abajo
		if ((i < tam - lado) && (usadas[i + lado] == 0) && ((usadas[i] == 0) || (usadas[i] != tablero[i] + lado))) {
		    usadas[i + lado] = tablero[i];
		    tablero[i] = 0;
		    recorre(tablero, usadas);
		    tablero[i] = usadas[i + lado];
		    usadas[i + lado] = 0;
		}
	}

	private void arriba(int[] tablero, int[] usadas, int i) {
		//arriba
		if ((i >= lado) && (usadas[i - lado] == 0) && ((usadas[i] == 0) || (usadas[i] != tablero[i] - lado))) {
		    usadas[i - lado] = tablero[i]; //resto lado porque esta en el otro renglon
		    tablero[i] = 0;
		    recorre(tablero, usadas);
		    tablero[i] = usadas[i - lado];
		    usadas[i - lado] = 0;
		}
	}

	private void izquierda(int[] tablero, int[] usadas, int i) {
		//lo mismo para izq
		if ((i % lado > 0) && (usadas[i - 1] == 0) && ((usadas[i] == 0) || (usadas[i] != tablero[i] - 1))) {
		    usadas[i - 1] = tablero[i];
		    tablero[i] = 0;
		    recorre(tablero, usadas);
		    tablero[i] = usadas[i - 1];
		    usadas[i - 1] = 0;
		}
	}
	
}
