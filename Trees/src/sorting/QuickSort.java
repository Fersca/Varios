package sorting;

public class QuickSort {

	//Ejemplo 1: http://aspyct.org/blog/2012/07/18/quicksort-brilliantly-explained/
	//Ejemplo 2: http://www.vogella.com/articles/JavaAlgorithmsQuicksort/article.html
	//Teoria: http://discrete.gr/complexity/
	
	public static void main(String[] args) {
		
		//Probando que es un algoritmo n*log(n)
		
		QuickSort q = new QuickSort();		
		int[] array = {0,0,0,0,1,7,5,-9,9,3,34,8,0,2,1,65,76,6,0,4};
		q.calcular(array);
		
		///////////////////////////////////////
			
		int[] array2 = {0,0,0,0,1,7,5,-9,9,3};
		q.calcular(array2);
	
		///////////////////////////////////////
		
		int[] array3 = {0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3};
		q.calcular(array3);

		///////////////////////////////////////
		
		int[] array4 = {0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3,0,0,0,0,1,7,5,-9,9,3};
		q.calcular(array4);

	}

	public void calcular(int[] valores){

		ins=0;		
		quickSort(valores,0,valores.length);
		
		for(int i=0;i<valores.length;i++)
			System.out.print(","+valores[i]);
		
		System.out.println("");
		System.out.println("Elementos: "+valores.length+" - instrucciones: "+ins);

	}
	
	int ins=0;
	
	
	
	public void quickSort(int[] valores, int inicio, int fin){

		ins++;
		//si el array es de 1 o cero elementos, lo devuelvo.
		if (fin-inicio<2) return;
		
		ins++;ins++;
		//obtengo el pivot
		int pivot = valores[inicio];
		
		ins++;ins++;
		//punteros
		int p0 = inicio;
		int p1 = fin;
		
		//mientras no apunten a la misma celda
		while(p0<p1){
			ins++;
			
			ins++;
			//decremento el punteo derecho
			p1--;
			while(valores[p1]>pivot && p0<p1){
				ins++;ins++;ins++;ins++;
				p1--;
			}
			
			ins++;ins++;
			valores[p0]=valores[p1];
			
			//incremento el puntero izquierdo
			while(valores[p0]<pivot && p0<p1){
				ins++;ins++;ins++;ins++;
				p0++;
			}
			
			ins++;ins++;
			valores[p1]=valores[p0];
			
		}

		ins++;
		//grabo el valor del pivote
		valores[p0]=pivot;
		
		ins++; //la suma de p0+1
		//llamo recursivamente a la parte izquierda del pivote
		quickSort(valores,inicio,p0+1);
		ins++; //la suma de p0+1
		//llamo recursivamente a la parte derecha del pivote
		quickSort(valores,p0+1,fin);
		
	}
	
}
