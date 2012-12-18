package colores;

import java.util.List;

class Bloque {
	public Bloque(int row, int col) {
		this.row=row;
		this.col=col;
	}
	public int row;
	public int col;
	public int red=0;
	public int green=0;
	public int blue=0;
	public String status;
	public String statusAprox;
	public int color=0;
	public Color colorAprox;
	
	private static List<Color> listaColores = Colors.createListaColores();
	
	public void verificar(boolean createFiles) {

		colorAprox = getColorAprox(red, green, blue);
		
		if (createFiles){
			status="<td style=\"background-color: rgb("+red+","+green+","+blue+"); color: rgb("+red+","+green+","+blue+")\">#</td>";
			String colorHexa = completar(Integer.toHexString(red))+completar(Integer.toHexString(green))+completar(Integer.toHexString(blue));		
			color = convert(colorHexa.toUpperCase()); 
			statusAprox="<td style=\"background-color: rgb("+colorAprox.red+","+colorAprox.green+","+colorAprox.blue+"); color: rgb("+colorAprox.red+","+colorAprox.green+","+colorAprox.blue+")\">#</td>";
		}

	}

	private String completar(String s){
		if (s.length()==2) 
			return s;
		else
			return "0"+s;
	}
	
	private int convert(String colorH){

		//convierto a entero el valor en hexa
		int valor = 0;
		int tamanio = colorH.length();
		
		for (int i =0;i<tamanio;i++){
			valor = valor + Integer.parseInt(""+colorH.charAt(tamanio-1-i), 16) * potencia16(i);
		}
		return valor;

	}

	private int potencia16(int i) {

		if (i==0) return 1;
		
		int valor = 16;
		
		for(int j = i;j>1;j--){
			valor = valor * 16;
		}
		
		return valor;  //no se porque pero hay que multiplicarlo por menos 1
	}
	
	private Color getColorAprox(int r, int g, int b) {

		Color menor=null;
		double dif=1000000;
		for (Color c : listaColores) {
			double diferencia = c.difference(r, g, b);
			if (diferencia<dif){
				dif = diferencia;
				menor = c;
			}
			
		}
		return menor;
	}
}
