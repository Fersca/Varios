package rubik;

import java.util.ArrayList;

/**
 * Resuelve el cubo mágico
 * @author Fernando Scasserra @fersca
 */
public class Rubik {
	
	private static final String ARMADO = "RRRR-GGGG-YYYY-OOOO-WWWW-BBBB";

	public static void main(String[] args) {
	
		Rubik ru = new Rubik();
		ru.run();
				
	}

	/**
	 * Inicia el cubo, lo mezcla y empieza a girarlo
	 */
	private void run() {
		
		//Estado inicial
		Cube cube = new Cube();
						
		//Mezcla el cubo
		//cube = cube.rotateSideC();
		cube = cube.rotateSideF();
		cube = cube.rotateSideB();
		
		ArrayList<String> listaCubos = new ArrayList<String>();
		ArrayList<String> listaGiros = new ArrayList<String>();
				
		System.out.println("Inicial:");
		//Gira el cubo....
		if (mueve(cube, listaCubos,listaGiros,0))
			System.out.println("fin");
		else 
			System.out.println("no hay solución");
		
	}

	/**
	 * Chequea si sea armó el cubo, o si el estado está repetido, sino sigue girando...
	 * @param cube
	 * @param listaCubos
	 * @param depth
	 * @return
	 */
	private boolean mueve(Cube cube,ArrayList<String> listaCubos,ArrayList<String> listaGiros, int depth) {
		
		//Imprime estado

		System.out.println("");
		for (String string : listaGiros) {
			System.out.print(string+"-->");
		}
		System.out.println(cube.signature());
		System.out.println("");
		
		//Se llegó a profundidad corta
		if (depth==11) {System.out.println("max depth");return false;};
		
		//Si está repetido corta
		if (listaCubos.contains(cube.signature())) {System.out.println("repetido");return false;}
						
		//Si es estado correcto, termina
		if (cube.signature().equals(ARMADO)) return true;
		
		//Genero la lista nueva
		ArrayList<String> nuevaLista = new ArrayList<String>(listaCubos); 		
		nuevaLista.add(cube.signature());
		ArrayList<String> nuevaGiro1 = new ArrayList<String>(listaGiros); 		
		ArrayList<String> nuevaGiro2 = new ArrayList<String>(listaGiros);
		ArrayList<String> nuevaGiro3 = new ArrayList<String>(listaGiros);
		
		depth++;
		
		//Pruebo las combinaciones de rotaciones
		Cube nuevoCube = cube.rotateSideC();
		nuevaGiro1.add("C");
		if (mueve(nuevoCube,nuevaLista, nuevaGiro1,depth)) {System.out.println("Cara C"); return true;};
		
		/*
		nuevoCube = cube.rotateSideA();
		if (mueve(nuevoCube,nuevaLista, depth++)) return true;
		 */
		
		nuevoCube = cube.rotateSideB();
		nuevaGiro2.add("B");
		if (mueve(nuevoCube,nuevaLista, nuevaGiro2,depth)) {System.out.println("Cara B"); return true;};
		
		/*	
		nuevoCube = cube.rotateSideD();
		if (mueve(nuevoCube,nuevaLista, depth++)) return true;
	
		nuevoCube = cube.rotateSideE();
		if (mueve(nuevoCube,nuevaLista, depth++)) return true;
		*/
		
		nuevoCube = cube.rotateSideF();
		nuevaGiro3.add("F");
		if (mueve(nuevoCube,nuevaLista, nuevaGiro3,depth)) {System.out.println("Cara F"); return true;};
		
		//Ninguna rotación exitosa
		return false;
		
	}

}
