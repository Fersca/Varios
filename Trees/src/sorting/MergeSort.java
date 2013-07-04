package sorting;

public class MergeSort {

	public static void main(String[] args) {
	
		MergeSort m = new MergeSort();
		int[] paraMerge = {6,3,5,0,1,7,8,9,12,33};
		m.calcular(paraMerge);
		
		//////////////////////////
		
		int[] paraMerge2 = {6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33};
		m.calcular(paraMerge2);
		
		//////////////////////////
		
		int[] paraMerge3 = {6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33};
		m.calcular(paraMerge3);

		//////////////////////////
		
		int[] paraMerge4 = {6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33,6,3,5,0,1,7,8,9,12,33};
		m.calcular(paraMerge4);

	}
	
	int ins=0;
	
	public void calcular(int[] valores){

		ins=0;		
		int[] result = mergeSort(valores);
		
		for(int i=0;i<result.length;i++)
			System.out.print(","+result[i]);
		
		System.out.println("");
		System.out.println("Elementos: "+result.length+" - instrucciones: "+ins);

	}

	public int[] mergeSort(int[] valores){
		
		//si la lista de valores tiene un elemento o cero, la devuelvo
		ins++;
		if (valores.length<2) return valores;
		
		//divide a la lista de valores en 2
		ins++;ins++;
		int div = valores.length/2;
		
		int[] lista1;
		ins++;ins++;
		if (valores.length%2==1){
			ins++;ins++;ins++;
			lista1 = new int[div+1];
		}
		else {
			ins++;ins++;ins++;
			lista1 = new int[div];
		}
		
		ins++;ins++;
		int[] lista2 = new int[div];
		
		ins++;
		int contador=0;
		ins++;
		while (contador<lista1.length){
			ins++;ins++;
			lista1[contador]=valores[contador];
			ins++;
			contador++;
		}
		ins++;
		int fijo = contador;
		ins++;
		while (contador<valores.length){
			ins++;ins++;ins++;
			lista2[contador-fijo]=valores[contador];
			ins++;
			contador++;
		}			
		
		//mergeo el resultado
		ins++;
		int[] mergeo = merge(mergeSort(lista1),mergeSort(lista2));
		
		//devuelvo el mergeo
		return mergeo;
		
	}
	
	private int[] merge(int[] lista1, int[] lista2){
				
		//si las la primera esta vacia devuelvo la segunda
		ins++;
		if (lista1.length==0) return lista2;
		
		//si la segunda esta vacia devuelvo la primera
		ins++;
		if (lista2.length==0) return lista1;
		
		//ninguna de las dos esta vacia, comparo los primeros elementos 
		//y devuelvo el elemento mas chico mas el merge de lo que queda, 
		//de esa manera de forma recursiva se va ordenando
		ins++;ins++;ins++;
		int[] nueva = new int[lista1.length+lista2.length];
		
		ins++;ins++;ins++;
		if (lista1[0]<lista2[0]){

			ins++;ins++;
			nueva[0]=lista1[0];
			ins++;ins++;ins++;
			int[] quedo = new int[lista1.length-1];
			ins++;ins++;ins++;
			for (int i=1;i<lista1.length;i++){
				ins++;ins++;ins++;
				quedo[i-1]=lista1[i];
			}
			
			ins++;
			int[] nuevoRes = merge(quedo,lista2);
			
			ins++;ins++;ins++;
			for (int i = 0;i<nuevoRes.length;i++){
				ins++;ins++;ins++;
				nueva[i+1]=nuevoRes[i];
			}

		} else {

			ins++;ins++;
			nueva[0]=lista2[0];
			ins++;ins++;ins++;
			int[] quedo = new int[lista2.length-1];
			ins++;ins++;ins++;
			for (int i=1;i<lista2.length;i++){
				ins++;ins++;ins++;
				quedo[i-1]=lista2[i];
			}
			
			ins++;
			int[] nuevoRes = merge(quedo,lista1);
			
			ins++;ins++;ins++;
			for (int i = 0;i<nuevoRes.length;i++){
				ins++;ins++;ins++;
				nueva[i+1]=nuevoRes[i];
			}
			
		}
			
		return nueva;
	}
	
	
}
