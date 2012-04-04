package rubik;

/**
 * Representa un cubo armado de una determinada forma
 * @author Fernando Scasserra @fersca
 */
public class Cube {
	
	//Red=1, Green=2, Yellow=3, Orange=4, White=5, Blue=6
	public char[] sideA = {'R','R','R','R'};
	public char[] sideB = {'G','G','G','G'};
	public char[] sideC = {'Y','Y','Y','Y'};
	public char[] sideD = {'O','O','O','O'};
	public char[] sideE = {'W','W','W','W'};
	public char[] sideF = {'B','B','B','B'};
	
	public Cube rotateSideC(){
		
		Cube cube = cloneCube();
		
		char bak1 = cube.sideC[0];
			
		cube.sideC[0] = cube.sideC[2]; 
		cube.sideC[2] = cube.sideC[3];
		cube.sideC[3] = cube.sideC[1]; 
		cube.sideC[1] = bak1;
		
		char bak2 = cube.sideD[0];
		char bak3 = cube.sideD[2];
		
		cube.sideD[2] = cube.sideA[3]; 
		cube.sideD[0] = cube.sideA[2];
		cube.sideA[3] = cube.sideB[1]; 
		cube.sideA[2] = cube.sideB[3]; 
		cube.sideB[1] = cube.sideE[0];
		cube.sideB[3] = cube.sideE[1]; 
		cube.sideE[0] = bak3;
		cube.sideE[1] = bak2; 
		
		return cube;
		
	}
	
	public Cube rotateSideA(){
		return this;
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
	
	public Cube rotateSideD(){
		return this;
	}

	public Cube rotateSideE(){
		return this;
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
	
	private Cube cloneCube(){

		Cube cube = new Cube();
		cube.sideA = this.sideA;
		cube.sideB = this.sideB;
		cube.sideC = this.sideC;
		cube.sideD = this.sideD;
		cube.sideE = this.sideE;
		cube.sideF = this.sideF;
		return cube;
		
	}
	
	public String signature(){
		
		StringBuilder result = new StringBuilder();
		for (char num : sideA) {result.append(num);} result.append('-');
		for (char num : sideB) {result.append(num);} result.append('-');
		for (char num : sideC) {result.append(num);} result.append('-');
		for (char num : sideD) {result.append(num);} result.append('-');
		for (char num : sideE) {result.append(num);} result.append('-');
		for (char num : sideF) {result.append(num);}
		
		return result.toString();
		
	}
	

}
