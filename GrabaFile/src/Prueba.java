
public class Prueba extends PruebaPadre{

	public static void main(String[] args) {
	
		Prueba p = new Prueba();
		p.ejecutar();
		
	}
	
	private int ejecutar(){
		
		int a = 200;
		String cadena = "El valor de a es: "+a;			
		System.out.println(cadena);
		return a;
		
	}
	
}
