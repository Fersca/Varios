/**
 * @author Fernando Scasserra @fersca
 */
public class Vulcano {
	
	public static void main(String[] args) {
		Vulcano vul = new Vulcano();
		vul.run();
	}

	private void run() {

		//Posición angular de planetas
		int p1 = 0, p2 = 0, p3 = 0;
		
		//Velocidad de rotación: grados por día
		int v1 = 1, v2 = 3, v3 = 5;
		
		//Variables a calcular
		int cantSequia = 0, maxPer=0, maxPerDia=0, alineadoSinSon=0, cantLluvia = 0;

		//Posiciones en el plano
		int p1x=0, p1y=0, p1h = 500;
		int p2x=0, p2y=0, p2h = 2000;
		int p3x=0, p3y=0, p3h = 1000;
						
		//Para 10 años, 3650 días...
		for(int i =0;i<3650;i++){
		
			//Calcula posición angular
			p1 = suma(v1,p1);
			p2 = suma(v2,p2);
			p3 = resta(v3,p3);
			
			//Verifica si están alineados
			if (((p1==p2)||(p1+180)==p2)&&(p1==p3||(p1+180==p3))){
				cantSequia++;
			}
			
			//Calcula posicion X, Y de los planetas
			p1x = calcularX(p1,p1h);
			p1y = calcularY(p1,p1h);
			p2x = calcularX(p2,p2h);
			p2y = calcularY(p2,p2h);
			p3x = calcularX(p3,p3h);
			p3y = calcularY(p3,p3h);
			
			//Calcula que el sol esté en el medio
			if  ((p1x>0 && p2x<0) || (p1x>0 && p3x<0) || (p1x<0 && p2x>0) || (p1x<0 && p3x>0)){
				
				//Cantidad de días de lluvia
				cantLluvia++;
				
				//Distancias entre planetas
				int dist12 = dist(p1x, p2x, p1y, p2y);
				int dist13 = dist(p1x, p3x, p1y, p3y);
				int dist23 = dist(p2x, p3x, p2y, p3y);
 
				//Calcula el perímetro máximo
				int perimetro = dist12+dist13+dist23;
				if (perimetro>maxPer){
					maxPer = perimetro;
					maxPerDia = i;
				}
				
			} 
			
			//Calcula si los planetas están alineados sin sol
			//Calcula la tangente
			int x= p1x-p2x;
			int y= p1y-p2y;
			double tan = ((double)y)/((double)x);
			
			//Calculo b.
			int b = p1y-(int)(tan*p1x);
			
			//Con la tangente se calcula el punto que debería tener el tercer planeta
			//Si b=0 quiere decir que pasa por el origen, o sea, está alineado al sol
			if (b!=0){
				
				int y2 = (int)(tan*p3x)+b; //y=mx+b
				
				//Si es igual al que tiene, están alineados.
				if ((y2==p3y)){
					alineadoSinSon++;
				}				
			} 
			
		}
		
		System.out.println("Sequias: "+cantSequia);
		System.out.println("Lluvias: "+cantLluvia);
		System.out.println("MaxPer : "+maxPer+", día: "+maxPerDia);
		System.out.println("Óptimas: "+alineadoSinSon);
	
	}

	private int dist(int x1, int x2, int y1, int y2){				
		int dist12x= Math.abs(x1-x2);
		int dist12y= Math.abs(y1-y2);
		
		return (int)Math.sqrt((dist12x*dist12x)+(dist12y*dist12y));
	}
	
	private int calcularY(int p1, int p1h) {
		return  (int)(Math.sin(Math.toRadians(p1)) * p1h);
	}

	private int calcularX(int p1, int p1h) {
		return (int)(Math.cos(Math.toRadians(p1)) * p1h);
	}

	private int resta(int v3, int p3) {
		p3 = p3-v3;
		if (p3<0) p3=p3+360;
		return p3;
	}

	private int suma(int v3, int p3) {
		p3 = p3+v3;
		if (p3>=360) p3=p3-360;
		return p3;
	}
}
