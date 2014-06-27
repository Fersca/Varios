package ancestroComun;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AncestroComun implements BuscarAncestroComun
{
	
	public static void main(String[] args) {
	
		AncestroComun anc = new AncestroComun();
		
		//test 1
		String[] commits =   {"G7","F6","E5","D4","C3","B2", "A1"};
        String[][] hashesPadres = {{"F6", "D4"},{"E5"},{"B2"},{"C3"}, {"B2"},{"A1"}, null};
        String commit1="G7";
        String commit2="G7";
        
		String result = anc.buscarAncestroComun(commits, hashesPadres, commit1, commit2);
		
		System.out.println(result);

		//test 2
		String[] commits2 =   {"G7","F6","E5","D4","C3","B2", "A1"};
        String[][] hashesPadres2 = {{"F6", "D4"},{"E5"},{"B2"},{"C3"}, {"B2"},{"A1"}, null};
        String commit21="E5";
        String commit22="G7";
        
		String result2 = anc.buscarAncestroComun(commits2, hashesPadres2, commit21, commit22);
		
		System.out.println(result2);

	}
	
    public String buscarAncestroComun(String[] commits, String[][] hashesPadres, String commit1, String commit2){

    	//check if parameters are null
    	if (commits==null || hashesPadres==null || commit1==null || commit2==null)
    		return null;

    	//check if parameters are empty
    	if (commits.length==0 || hashesPadres.length==0)
    		return null;

    	//Map that contains the children of each commit
    	HashMap<String, List<String>> hijosMap = new HashMap<String, List<String>>();
    	HashMap<String, List<String>> padresMap = new HashMap<String, List<String>>();
    	
    	int padrePos = 0;
    	//verify each parent
    	for (String[] padres : hashesPadres) {
			
    		if (padres!=null) {
    			//for each parent check the children
	    		for (String padre : padres) {
	    			    			
	    			//child of parent
	    			String hijo = commits[padrePos];
	
	    			//verify is exists the child in the parents map, if not, add it
	    			if (!padresMap.containsKey(hijo)) {
	    				List<String> padresArray = new LinkedList<String>();
	    				padresArray.add(padre);
	    				padresMap.put(hijo, padresArray);
	    			} else {
	        			//if exists, add only the child to the map
	    				padresMap.get(hijo).add(padre);
	    			}
	    			
	    			//if not exists the parent in the children map, add it with its child
	    			if (!hijosMap.containsKey(padre)) {
	    				List<String> hijosArray = new LinkedList<String>();
	    				hijosArray.add(hijo);
	    				hijosMap.put(padre, hijosArray);
	    			} else {
	        			//if exist, only add the child to the map
	    				hijosMap.get(padre).add(hijo);
	    			}
	    			
	    			//search for the children of the children
	    			List<String> hijosDelHijo = hijosMap.get(hijo);
	
	    			//if it has children
	    			if (hijosDelHijo!=null) {
		    			//for each children
		    			for (String hijoDelHijo : hijosDelHijo) {
		    				//add these children (if there is) to the parent map
		    				hijosMap.get(padre).add(hijoDelHijo);
						}
	    			}
	
	    		}
    		}
    		
    		//increment the position to search in the children array
    		padrePos++;
		}
    	
    	//search for the first parent to check
    	LinkedList<String> padresNodo = (LinkedList<String>)padresMap.get(commit1);

    	//check that the commit wont be the root one
    	if (padresNodo==null)
    		return null;

    	List<String> padresEnComun = new LinkedList<String>();
    	    	
    	//verify each parent
    	while(!padresNodo.isEmpty()) {
    		
    		//get a parent from the list
    		String padre = padresNodo.poll();
    		
    		//get the children from this parent
    		List<String> hijosDelPadre = hijosMap.get(padre);
    		
    		//verify if the other commit is in the 
    		if (hijosDelPadre.contains(commit2)) {
    			padresEnComun.add(padre);
    		} else {
    			//keen on verifying the parents
    			LinkedList<String> padresNuevos = (LinkedList<String>)padresMap.get(padre);
    			//if it is not null (has parents, keep on verifying)
    			if (padresNuevos!=null)
    				padresNodo.addAll(padresNuevos);
    		}
    		
    	}
    	
    	//search for the parent with the lowest position in the map (the newest one)
    	String minNode=null;
    	int pos=-1;
    	for (String padre : padresEnComun) {
    		
    		//search for the original position
    		int i=0;
    		for (String hash:commits) {
    			if (hash.equals(padre))
    				break;
    			i++;
    		}
    		
    		//save the minimun
			if (pos==-1 || i<pos) {
				minNode = padre;
				pos=i; 
			}
		}
    	
        return minNode;
        
    }

}