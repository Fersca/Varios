import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Pruebas {

	public static void main(String[] args) {
		Pruebas p = new Pruebas();
		p.run();
	}
	
	private void run(){
		
		
		
		String[] empleados = {"Mati","Nacho","Pablo","Pelito","Mariano", "Silvina","Cristian"};

		String listado = "Listado de Empleados: ";

		for(String empleado : empleados){
		    listado = listado +empleado+ ", ";
		}
		
		System.out.println(listado);
		
		//////////////////////////////////////////////////////////

		StringBuilder sb = new StringBuilder();
		
		sb.append("Listado de Empleados: ");
		
		for(String empleado : empleados){
			sb.append(empleado);
			sb.append(", ");
		}
		
		System.out.println(sb.toString());

		//////////////////////////////////////////////////////////

		ArrayList<String> lista = new ArrayList<String>();
		
		for(String empleado : empleados){
			lista.add(empleado);
		}

		for(String empleado : lista){
			System.out.println(empleado);
		}

		//////////////////////////////////////////////////////////

		LinkedList<String> linkeado = new LinkedList<String>();
		
		for(String empleado : empleados){
			linkeado.add(empleado);
		}

		for(String empleado : linkeado){
			System.out.println(empleado);
		}

		//////////////////////////////////////////////////////////

		HashMap<String, Empleado> mapa = new HashMap<String, Empleado>();
		
		for(String empleado : empleados){
			Empleado em = new Empleado(empleado,empleado.length());
			mapa.put(empleado, em);
		}

		System.out.println(mapa.get("Mariano").getInfo());
		
	}
	
}
