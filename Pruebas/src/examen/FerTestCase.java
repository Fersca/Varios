package examen;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class FerTestCase {

	@Test
	public void testRun() throws Exception {
		
		ExamenFer examen = new ExamenFer();
		
		ArrayList<ArrayList<Integer>> matriz = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> fila1 = new ArrayList<Integer>();
		fila1.add(1);
		fila1.add(2);
		fila1.add(1);
		fila1.add(2);
		fila1.add(2);
		ArrayList<Integer> fila2 = new ArrayList<Integer>();
		fila2.add(1);
		fila2.add(1);
		fila2.add(1);
		fila2.add(1);
		fila2.add(1);
		ArrayList<Integer> fila3 = new ArrayList<Integer>();
		fila3.add(3);
		fila3.add(4);
		fila3.add(1);
		fila3.add(1);
		fila3.add(4);
		ArrayList<Integer> fila4 = new ArrayList<Integer>();
		fila4.add(1);
		fila4.add(1);
		fila4.add(1);
		fila4.add(3);
		fila4.add(3);
		ArrayList<Integer> fila5 = new ArrayList<Integer>();
		fila5.add(3);
		fila5.add(4);
		fila5.add(1);
		fila5.add(2);
		fila5.add(5);
		

		matriz.add(fila1);
		matriz.add(fila2);
		matriz.add(fila3);
		matriz.add(fila4);
		matriz.add(fila5);
		
		ExamenFer.matriz = matriz;
		Integer result = examen.run();
		
		if (result!=6) {
			fail("Not yet implemented: "+ result);	
		} 
	}

}
