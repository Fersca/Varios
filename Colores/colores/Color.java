package colores;

class Color {
	int red;
	int green;
	int blue;
	int cantidad;
	String nombre;
	int numero;
	
	public Color(int r, int g, int b, String n, int nu){
		red = r;
		green = g;
		blue = b;
		nombre = n;
		numero = nu;
	}
	
	public double difference(int r, int g, int b){
		
		int difRed = red - r;
		int difGreen = green - g;
		int difBlue = blue - b;
		
		if (difRed<0) difRed = difRed * -1;
		if (difGreen<0) difGreen = difGreen * -1;
		if (difBlue<0) difBlue = difBlue * -1;
		
		return Math.sqrt((difRed*difRed)+(difGreen*difGreen)+(difBlue*difBlue));
	}
}