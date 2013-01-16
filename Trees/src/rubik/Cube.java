package rubik;

import java.util.ArrayList;

import engine.AbstractNode;
import engine.Node;

public class Cube extends AbstractNode implements Node {

	public char[] sideA = new char[4];
	public char[] sideB = new char[4];
	public char[] sideC = new char[4];
	public char[] sideD = new char[4];
	public char[] sideE = new char[4];
	public char[] sideF = new char[4];
	
	@Override
	public boolean valido() {
		return true;
	}

	private Cube cloneCube(){

		Cube cube = new Cube();
		System.arraycopy(this.sideA, 0, cube.sideA, 0, 4);
		System.arraycopy(this.sideB, 0, cube.sideB, 0, 4);
		System.arraycopy(this.sideC, 0, cube.sideC, 0, 4);
		System.arraycopy(this.sideD, 0, cube.sideD, 0, 4);
		System.arraycopy(this.sideE, 0, cube.sideE, 0, 4);
		System.arraycopy(this.sideF, 0, cube.sideF, 0, 4);
		
		return cube;

	}	
	
	@Override
	public boolean solucion() {
		
		if 
		(
		sideA[0] == 'R' &&
		sideA[1] == 'R' &&
		sideA[2] == 'R' &&
		sideA[3] == 'R' &&

		sideB[0] == 'G' &&
		sideB[1] == 'G' &&
		sideB[2] == 'G' &&
		sideB[3] == 'G' &&

		sideC[0] == 'Y' &&
		sideC[1] == 'Y' &&
		sideC[2] == 'Y' &&
		sideC[3] == 'Y' &&

		sideD[0] == 'O' &&
		sideD[1] == 'O' &&
		sideD[2] == 'O' &&
		sideD[3] == 'O'	&&
		
		sideE[0] == 'W' &&
		sideE[1] == 'W' &&
		sideE[2] == 'W' &&
		sideE[3] == 'W'	&&	 

		sideF[0] == 'B' &&
		sideF[1] == 'B' &&
		sideF[2] == 'B' &&
		sideF[3] == 'B'			 	
		) 
		{
			System.out.println("Solucion");
			return true;
		}
			 
		else 
			return false;
		
	}

	@Override
	public ArrayList<Node> generarNuevos() {

		ArrayList<Node> lista = new ArrayList<Node>();
		
		Cube c1 = this.cloneCube();
		c1.rotateSideC();
		lista.add(c1);

		Cube c2 = this.cloneCube();
		c2.rotateSideC();
		c2.rotateSideC();
		c2.rotateSideC();
		lista.add(c2);
		
		return lista;
		
	}

	public Cube rotateSideB(){

		Cube cube = cloneCube();

		char bak1 = cube.sideB[0];

		cube.sideB[0] = cube.sideB[2]; 
		cube.sideB[2] = cube.sideB[3];
		cube.sideB[3] = cube.sideB[1]; 
		cube.sideB[1] = bak1;

		char bak2 = cube.sideC[0];
		char bak3 = cube.sideC[2];

		cube.sideC[0] = cube.sideA[0]; 
		cube.sideC[2] = cube.sideA[2];
		cube.sideA[0] = cube.sideF[0]; 
		cube.sideA[2] = cube.sideF[2]; 
		cube.sideF[0] = cube.sideE[0];
		cube.sideF[2] = cube.sideE[2]; 
		cube.sideE[0] = bak2;
		cube.sideE[2] = bak3; 

		return cube;

	}
	
	public void rotateSideC(){

		char bak1 = sideC[0];

		sideC[0] = sideC[2]; 
		sideC[2] = sideC[3];
		sideC[3] = sideC[1]; 
		sideC[1] = bak1;

		char bak2 = sideD[0];
		char bak3 = sideD[2];

		sideD[2] = sideA[3]; 
		sideD[0] = sideA[2];
		sideA[3] = sideB[1]; 
		sideA[2] = sideB[3]; 
		sideB[1] = sideE[0];
		sideB[3] = sideE[1]; 
		sideE[0] = bak3;
		sideE[1] = bak2; 

	}
	
	public Cube rotateSideF(){

		Cube cube = cloneCube();

		char bak1 = cube.sideF[0];

		cube.sideF[0] = cube.sideF[2]; 
		cube.sideF[2] = cube.sideF[3];
		cube.sideF[3] = cube.sideF[1]; 
		cube.sideF[1] = bak1;

		char bak2 = cube.sideD[3];
		char bak3 = cube.sideD[1];

		cube.sideD[1] = cube.sideE[3]; 
		cube.sideD[3] = cube.sideE[2];
		cube.sideE[3] = cube.sideB[2]; 
		cube.sideE[2] = cube.sideB[0]; 
		cube.sideB[2] = cube.sideA[0];
		cube.sideB[0] = cube.sideA[1]; 
		cube.sideA[0] = bak3;
		cube.sideA[1] = bak2; 

		return cube;

	}	
	
	@Override
	public void init() {
		
		//Red=1, Green=2, Yellow=3, Orange=4, White=5, Blue=6
		sideA[0] = 'R';
		sideA[1] = 'R';
		sideA[2] = 'R';
		sideA[3] = 'R';

		sideB[0] = 'G';
		sideB[1] = 'G';
		sideB[2] = 'G';
		sideB[3] = 'G';

		sideC[0] = 'Y';
		sideC[1] = 'Y';
		sideC[2] = 'Y';
		sideC[3] = 'Y';

		sideD[0] = 'O';
		sideD[1] = 'O';
		sideD[2] = 'O';
		sideD[3] = 'O';	
		
		sideE[0] = 'W';
		sideE[1] = 'W';
		sideE[2] = 'W';
		sideE[3] = 'W';		

		sideF[0] = 'B';
		sideF[1] = 'B';
		sideF[2] = 'B';
		sideF[3] = 'B';	
				
		rotateSideC();
		rotateSideC();
		
	}
	
	@Override
	public boolean equals(Object obj) {

		Cube otro = (Cube)obj;
		if (
			this.sideA==otro.sideA && 
			this.sideB==otro.sideB &&
			this.sideC==otro.sideC &&
			this.sideD==otro.sideD &&
			this.sideE==otro.sideE &&
			this.sideF==otro.sideF
		) 
			return true; 
		else 
			return false;
		
	}

	@Override
	public void printNode() {

		StringBuilder result = new StringBuilder();
		for (char num : sideA) {result.append(num);} result.append('-');
		for (char num : sideB) {result.append(num);} result.append('-');
		for (char num : sideC) {result.append(num);} result.append('-');
		for (char num : sideD) {result.append(num);} result.append('-');
		for (char num : sideE) {result.append(num);} result.append('-');
		for (char num : sideF) {result.append(num);}

		System.out.println(result.toString());
		
	}
}
