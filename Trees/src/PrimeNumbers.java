/**
 * Calcula numeros primos
 * @author fscasserra
 *
 */
public class PrimeNumbers {

	public static void main(String[] args){
		PrimeNumbers pm = new PrimeNumbers();
		pm.run(10000);
	}
	
	private void run(int capacity){
	
		int[] numbers = new int[capacity];
		int cant=0;
		
		for (int i = 2;i<capacity;i++){
			
			boolean primo = true;
			for (int j : numbers){
				
				if (j==0)
					break;
				
				if (j>Math.sqrt(i))
					break;
				
				if (i%j==0){
					primo=false;
					break;
				}
			}
			
			if (primo){
				numbers[cant] = i;
				cant++;
			}
			
		}

		for (int j : numbers){
			if (j==0)
				break;
			System.out.println("Primo: "+j);
		}
		
		System.out.println("Cant: "+cant);
		System.out.println("Porc: "+(((double)cant/(double)capacity)*100)+"%");
	}
	
}
