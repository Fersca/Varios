package sorting;

public class MergeSort2 {

	public static void main(String[] args){
		
		int[] numbers = {32,2,5,65,4,6,9,32,16,10,99,99,99,3,54,12,33,0,14,87,77,21,3,8,7};
		
		MergeSort2 m = new MergeSort2();
		
		int[] ordenados = m.sort(numbers);
		
		for (int i : ordenados) {
			System.out.print(i+",");
		}
		
	}
	
	public int[] sort(int[] numbers){

		if (numbers.length==1) return numbers;
		
		//divido la lista de numeros en dos
		int[] list1 = new int[numbers.length/2];
		int[] list2;
		if (numbers.length%2==1)
			list2 = new int[list1.length+1];
		else 
			list2 = new int[list1.length];
		
		int i = 0;
		
		while(i<list1.length){
			list1[i]=numbers[i];
			i++;
		}

		int j = 0;
		while(j<list2.length){
			list2[j]=numbers[i+j];
			j++;
		}
		
		//ordeno esas listas (con esre mismo metodo de forma recursiva)
		int[] list1Ordenada = sort(list1);
		int[] list2Ordenada = sort(list2);
		
		//las uno
		int[] unidas = merge(list1Ordenada,list2Ordenada);

		//devuelvo la unida
		return unidas;

	}
	
	private int[] merge(int[] list1, int[] list2){
		
		if (list1.length==0) return list2;
		if (list2.length==0) return list1;
		
		//creo una lista nueva con el total de elementos
		int[] unidas = new int[list1.length+list2.length];
		
		int[] list1nueva;
		int[] list2nueva;
		
		//comparo los primeros elementos y el menos lo pongo como menor en la lista
		if (list1[0]<=list2[0]){
			unidas[0]=list1[0];
			
			//genero una nueva lista con los elementos menos el que puse
			list1nueva = new int[list1.length-1];
			for (int i = 1; i<list1.length;i++)
				list1nueva[i-1]=list1[i];
			
			list2nueva = list2;
		} else { 
			unidas[0]=list2[0];
			
			//genero una nueva lista con los elementos menos el que puse
			list2nueva = new int[list2.length-1];
			for (int i = 1; i<list2.length;i++)
				list2nueva[i-1]=list2[i];
			
			list1nueva = list1;
		}
		
		//mergeo la nueva lista con la que no saque elemento (con este mismo metodo)
		int mergResto[] = merge(list1nueva, list2nueva);
		
		//pongo esos elementos atras del que acabo de poner
		for (int i =0;i<mergResto.length;i++)
			unidas[i+1]=mergResto[i];
		
		return unidas;
	}
	
}
