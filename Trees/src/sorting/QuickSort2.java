package sorting;

/**
 * Fersca QuickSort
 * @author fscasserra
 *
 */
public class QuickSort2 {
	
	public static void main(String[] args){
		
		QuickSort2 q = new QuickSort2();
		
		int[] numbers = {32,2,5,65,4,6,9,32,16,10,99,99,99,3,54,12,33,0,14,87,77,21,3,8,7};
		
		q.sort(numbers);
		
		for (int i : numbers) {
			System.out.print(i+",");	
		} 
		
	}
	
	//funci—n de sort
	public void sort(int[] numbers){
		
		//llama al sort con limites
		sort(numbers, 0,numbers.length-1);
		
	}
	
	private void sort(int[] numbers, int init, int end){

		//si es una lista de 1 o 0, retorno,
		if ((end-init)<1) return;
		
		//asigno los punteros a los extremos
		int p1 = init;
		int p2 = end;
		
		//elijo un pibote (el del medio)
		int posSpace = ((end-init)/2)+init;
		
		//lo pongo en una variable
		int pibot = numbers[posSpace];
		numbers[posSpace]=-1;

		//se mantiene en loop
		for (;;){		
			
			//empezando por la derecha voy recorriendo hasta que encuentro un numero menor al pibote
			while (numbers[p2]>=pibot && p1<p2){
				p2--;
			}
			
			if (p1<p2){
				//pongo ese numero en el lugar vacio
				numbers[posSpace] = numbers[p2];
				
				//as’gno el lugar vac’o al cual estaba el nœmero
				posSpace = p2;
				numbers[posSpace]=-1;				
			} else {
				break;
			}
			
			//voy por la izquierda hasta que el numero sea mayor al pibote
			while (numbers[p1]<=pibot && p1<p2){
				p1++;
			}
			
			if (p1<p2){
				//pongo el numero en el espacio en blanco
				numbers[posSpace] = numbers[p1];
				
				//as’gno el lugar vac’o al cual estaba el nœmero
				posSpace = p1;
				numbers[posSpace]=-1;
				//empiezo de nuevo hasta que sean iguales los punteros				
			} else {
				break;
			}
		}
		
		//pongo el valor del pibote en donde qued— el puntero del espacio en blanco
		numbers[posSpace] = pibot;
		
		//llamo a sort con los dos subarrays que quean de cada lado del pibote
		sort(numbers,init,posSpace-1);
		sort(numbers,posSpace+1,end);
	}
	
}