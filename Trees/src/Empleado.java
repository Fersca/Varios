
public class Empleado {

	private String nombre;
	private int experiencia;
	
	public Empleado(String nombre, int exp) {
		this.nombre=nombre;
		this.experiencia=exp;
	}

	public String getInfo() {
		return nombre+" - Experiencia: "+experiencia;
	}

	@Override
	public int hashCode() {
		return experiencia;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof Empleado))
			return false;

		if (((Empleado)obj).experiencia==this.experiencia && ((Empleado)obj).nombre.equals(this.nombre))
			return true;
		
		return false;
		
	}
	
	
}
	