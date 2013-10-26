import java.util.ArrayList;
import java.util.HashMap;

class Candy2 {

	private static void printPermutationsIterative(String string){
        
		HashMap<String,String> elementos = new HashMap<String, String>();
		int canti=0;
		int [] factorials = new int[string.length()+1];
        factorials[0] = 1;
        for (int i = 1; i<=string.length();i++) {
            factorials[i] = factorials[i-1] * i;
        }

        for (int i = 0; i < factorials[string.length()]; i++) {
            String onePermutation="";
            String temp = string;
            int positionCode = i;
            for (int position = string.length(); position > 0 ;position--){
                int selected = positionCode / factorials[position-1];
                onePermutation += temp.charAt(selected);
                positionCode = positionCode % factorials[position-1];
                temp = temp.substring(0,selected) + temp.substring(selected+1);
            }
            
            if (!elementos.containsKey(onePermutation)){
            	System.out.println("- "+onePermutation);
            	if (premio(onePermutation))
            		canti++;
            	elementos.put(onePermutation, onePermutation);
            	//elementos.add(onePermutation);
            	    
            	String copia = onePermutation;
            	for(int l=0;l<9;l++){
            		
            		String[] frase = copia.split("");            	            		            		
            		frase[l+1] = "X";
            		
            		StringBuilder builder = new StringBuilder();
            		boolean primero=true;
            		for(String s : frase) {
            			if (primero)
            				primero=false;
            			else 
            				builder.append(s);
            		}
            		            		
            		String nuevo = builder.toString();
            		System.out.println("| "+builder.toString());
            		
            		if (!elementos.containsKey(nuevo)){
                    	if (premio(nuevo))
                    		canti++;
                    	elementos.put(nuevo, nuevo);
            		}
            		
            	}
            	
            }
                        
        	//elementos.put(onePermutation, onePermutation);
            if (elementos.size()%10000==0)
            	System.out.println("tam: "+canti);
        }
        
        System.out.println("tam: "+elementos.size());
        System.out.println("premio: "+canti);
                
    }
	
    private static boolean premio(String a) {

    	if (a.charAt(0)==a.charAt(1) && a.charAt(1)==a.charAt(2)) return true;
    	if (a.charAt(3)==a.charAt(4) && a.charAt(4)==a.charAt(5)) return true;
    	if (a.charAt(6)==a.charAt(7) && a.charAt(7)==a.charAt(8)) return true;
    	if (a.charAt(0)==a.charAt(3) && a.charAt(3)==a.charAt(6)) return true;
    	if (a.charAt(1)==a.charAt(4) && a.charAt(4)==a.charAt(7)) return true;
    	if (a.charAt(2)==a.charAt(5) && a.charAt(5)==a.charAt(8)) return true;
    	
    	//con comodin
    	
    	if (a.charAt(0)==a.charAt(1) && a.charAt(2)=='X') return true;
    	if (a.charAt(3)==a.charAt(4) && a.charAt(5)=='X') return true;
    	if (a.charAt(6)==a.charAt(7) && a.charAt(8)=='X') return true;
    	
    	if (a.charAt(1)==a.charAt(2) && a.charAt(0)=='X') return true;
    	if (a.charAt(4)==a.charAt(5) && a.charAt(3)=='X') return true;
    	if (a.charAt(7)==a.charAt(8) && a.charAt(6)=='X') return true;

    	if (a.charAt(0)==a.charAt(2) && a.charAt(1)=='X') return true;
    	if (a.charAt(3)==a.charAt(5) && a.charAt(4)=='X') return true;
    	if (a.charAt(6)==a.charAt(8) && a.charAt(7)=='X') return true;
    	
    	if (a.charAt(5)==a.charAt(8) && a.charAt(2)=='X') return true;
    	if (a.charAt(2)==a.charAt(8) && a.charAt(5)=='X') return true;
    	if (a.charAt(2)==a.charAt(5) && a.charAt(8)=='X') return true;
    	
    	if (a.charAt(3)==a.charAt(6) && a.charAt(0)=='X') return true;
    	if (a.charAt(0)==a.charAt(6) && a.charAt(3)=='X') return true;
    	if (a.charAt(0)==a.charAt(3) && a.charAt(6)=='X') return true;

    	if (a.charAt(4)==a.charAt(7) && a.charAt(1)=='X') return true;
    	if (a.charAt(1)==a.charAt(7) && a.charAt(4)=='X') return true;
    	if (a.charAt(1)==a.charAt(4) && a.charAt(7)=='X') return true;
    	    	
    	return false;
    	
	}

	public static void main(String[] args) {
		printPermutationsIterative("AAACCCDDD");
	}
}
