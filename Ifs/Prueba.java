import java.util.ArrayList;
import java.util.Collection;

public class Prueba {

	public static void main(String[] args){
		Prueba p = new Prueba();
		p.calcular(14);
		p.calcular2(14);
		p.calcular3(14);
		p.calcular4(14);
		p.calcular5(14);
		p.calcular6(14);
		p.calcular7(14);
		p.calcular8(14);
		
	}
	
	/*
	 * Calcula en que estapa escolar esta cada chico
	 */
	public void calcular(int edad){
		
		if (edad<6){
			print("Jardin");
		} else {  
			if (edad<13) {
				print("Primaria");
			} else { 						
				print("Secundaria");
			}
		}
		
	}

	public void calcular2(int edad){
		
		if (edad<6){
			print("Jardin");
		}
		
		if (edad>=6 && edad<13) {
			print("Primaria");
		}  			
		
		if (edad>=13){
			print("Secundaria");
		}
		
	}
	
	
	public void calcular3(int edad){
		
		if (edad<6){
			print("Jardin");
			return;
		}
		
		if (edad<13) {
			print("Primaria");
			return;
		}  			
				
		print("Secundaria");
		
	}

	public void calcular4(int edad){
		
		jardin1(edad);
		primaria1(edad);
		secundaria1(edad);
		
	}

	private void jardin1(int edad) {
		if (edad<6){
			print("Jardin");
		}
	}

	private void primaria1(int edad) {
		if (edad>=6 && edad<13) {
			print("Primaria");
		}  			
	}

	private void secundaria1(int edad) {
		if (edad>=13){
			print("Secundaria");
		}
	}

	public void calcular5(int edad){
		if (jardin(edad) || primaria(edad) || secundaria()); 
	}

	private boolean jardin(int edad) {
		
		if (edad<6){
			print("Jardin");
			return true;
		}
		return false;
	}

	private boolean primaria(int edad) {
		
		return edad<13?print("Primaria"):Boolean.FALSE;

	}
	
	private boolean secundaria() {
		print("Secundaria");
		return true;
	}

	public void calcular6(int edad){
		
		Estrategia e1 = new Jardin();
		Estrategia e2 = new Primaria();
		Estrategia e3 = new Secundaria();
		
		Collection<Estrategia> estrategias = new ArrayList<Estrategia>();
		estrategias.add(e1);
		estrategias.add(e2);
		estrategias.add(e3);
		
		for (Estrategia e:estrategias){
			if (e.condicionOK(edad)) e.ejecutar();
		}
		
	}

	public void calcular7(int edad){
		
		for (Estrategia e:cargarEstrategias()){
			if (e.condicionOK(edad)) {e.ejecutar(); return;}
		}
		
	}

	public void calcular8(int edad){
		
		for (Estrategia2 e:cargarEstrategias2()) e.run(edad);

	}

	
	public Collection<Estrategia> cargarEstrategias(){
		Estrategia e1 = new Jardin();
		Estrategia e2 = new Primaria();
		Estrategia e3 = new Secundaria();
		
		Collection<Estrategia> estrategias = new ArrayList<Estrategia>();
		estrategias.add(e1);
		estrategias.add(e2);
		estrategias.add(e3);
		return estrategias;
	}

	public Collection<Estrategia2> cargarEstrategias2(){
		Estrategia2 e1 = new Jardin2();
		Estrategia2 e2 = new Primaria2();
		Estrategia2 e3 = new Secundaria2();
		
		Collection<Estrategia2> estrategias = new ArrayList<Estrategia2>();
		estrategias.add(e1);
		estrategias.add(e2);
		estrategias.add(e3);
		return estrategias;
	}

	interface Estrategia {
		public boolean condicionOK(int edad);
		public void ejecutar();
	}

	abstract class Estrategia2 {
		abstract public boolean condicionOK(int edad);
		abstract public boolean ejecutar();
		public boolean run(int edad){
			return condicionOK(edad)?ejecutar():Boolean.FALSE;
		}
	}

	class Jardin2 extends Estrategia2 {
		public boolean condicionOK(int edad){
			return edad<6;
		}
		public boolean ejecutar(){
			return print("Primaria");
		}		
	}
	
	class Jardin implements Estrategia{
		public boolean condicionOK(int edad){
			return edad<6;
		}
		public void ejecutar(){
			print("Primaria");
		}
	}

	class Primaria implements Estrategia {
		public boolean condicionOK(int edad){
			return edad<13;
		}
		public void ejecutar(){
			print("Secundaria");
		}
	}

	class Primaria2 extends Estrategia2 {
		public boolean condicionOK(int edad){
			return edad<13;
		}
		public boolean ejecutar(){
			return print("Secundaria");
		}
	}
	
	class Secundaria implements Estrategia {
		public boolean condicionOK(int edad){
			return edad>=13;
		}
		public void ejecutar(){
			print("Secundaria");
		}
	}

	class Secundaria2 extends Estrategia2 {
		public boolean condicionOK(int edad){
			return edad>=13;
		}
		public boolean ejecutar(){
			return print("Secundaria");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Boolean print(String s){
		System.out.println(s);
		return true;
	}
	
}
