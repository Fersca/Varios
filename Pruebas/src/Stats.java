
import java.util.ArrayList;

public class Stats {
	
	private ArrayList<Long> tiempos = new ArrayList<Long>(); 
	private long terminados = 0;
	private long tamanio = 0;
	public long init = 0;
	public long end = 0;

	public synchronized void calculateMaxMin(long time, long tamanio){
		
		tiempos.add(time);
		this.tamanio = this.tamanio + tamanio;
		terminados++;
		
	}

	public void printStats() throws InterruptedException {
		
		//cantidad de threads terminados
		System.out.println("Terminan los threads: "+ terminados);
	    
		long promedio=0;
		long max=0;
		long min=0;
		
		double more0ms=0;
		double more1ms=0;
		double more10ms=0;
		double more20ms=0;
		double more50ms=0;
		double more100ms=0;
		double more200ms=0;
		double more500ms=0;
		double more1000ms=0;
		double more5000ms=0;
		
		//esperando 3 segundos.
		Thread.sleep(3000);
		
		for (Long tiempo : tiempos) {
		
			if (tiempo>max) max = tiempo;
			if (tiempo<min) min = tiempo;
			
			promedio = promedio + tiempo;

			if (tiempo>5000) more5000ms++;
			else if (tiempo>1000) more1000ms++;
			else if (tiempo>500) more500ms++;
			else if (tiempo>200) more200ms++;
			else if (tiempo>100) more100ms++;
			else if (tiempo>50)  more50ms++;
			else if (tiempo>20)  more20ms++;
			else if (tiempo>10)  more10ms++;
			else if (tiempo>1)   more1ms++;
			else more0ms++;
			
		}
		
		//calcula el promedio
		promedio = promedio / terminados;
		double terminadosDouble = (double)terminados;
		
		System.out.println("Tiempo Total de la prueba: " + (end-init) + "ms");
		System.out.println("Cantidad de Operaciones por ms: " + terminadosDouble/(double)(end-init));
		System.out.println("Cantidad promedio de items hit: " + 100*(double)tamanio/(terminadosDouble*10) + "%");
		System.out.println("Promedio de tiempos de respuesta: " + promedio + "ms");
	    
		//Calculo m�ximos y m�nimos
	    System.out.println("Min Time: "+min+"ms, Max Time: "+max+"ms");
	    
	    //Imprime las stats de tiempo:
	    System.out.println("Menos de 1ms:  " + 100*more0ms/terminadosDouble + "% - " + more0ms);
	    System.out.println("Mas de 1ms:    " + 100*more1ms/terminadosDouble + "% - " + more1ms);
	    System.out.println("Mas de 10ms:   " + 100*more10ms/terminadosDouble + "% - " + more10ms);
	    System.out.println("Mas de 20ms:   " + 100*more20ms/terminadosDouble + "% - " + more20ms);
	    System.out.println("Mas de 50ms:   " + 100*more50ms/terminadosDouble + "% - " + more50ms);
	    System.out.println("Mas de 100ms:  " + 100*more100ms/terminadosDouble + "% - " + more100ms);
	    System.out.println("Mas de 200ms:  " + 100*more200ms/terminadosDouble + "% - " + more200ms);
	    System.out.println("Mas de 500ms:  " + 100*more500ms/terminadosDouble + "% - " + more500ms);
	    System.out.println("Mas de 1000ms: " + 100*more1000ms/terminadosDouble + "% - " + more1000ms);
	    System.out.println("Mas de 5000ms: " + 100*more5000ms/terminadosDouble + "% - " + more5000ms);
	   
	    
	}

}
