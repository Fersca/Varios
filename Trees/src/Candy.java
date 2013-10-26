import java.util.ArrayList;

public class Candy
{
	public static void main ( String args [])
	{
		String p ="1112223334"; 
		int pr = getFactorial(p.length()); 
		String[] ne = permutar(p,pr);

		System.out.println("fin");
		//ArrayList<String> ne2 = sacaRepeticiones(ne);
		//mostrar( ne2);
	}
 
	private static ArrayList<String> sacaRepeticiones(String[] ne) {

		for (int i = 0;i<ne.length;i++) {
			
			String frase = ne[i];
			
			if (frase==null) continue;
		
			for (int j = i+1;j<ne.length;j++){
				if (frase.equals(ne[j]))
					ne[j]=null;
			}
			
		}
		
		ArrayList<String> array = new ArrayList<String>();
		for (int i = 0;i<ne.length;i++) {
			if (ne[i]!=null)
				array.add(ne[i]);
				
		}

		return array;
	}

	public static String[]  permutar(String cadena,int p)
	{
		String[] per=new String[p];
		int l = cadena.length();
		int d=p/l;
		String[] aux = permutacion(cadena);
		int pos =0;
 
		if(p==1||l==1)
		{
			per[0] = cadena;
			return per;
		}
 
		for(int i=0;i<aux.length;i++)
		{
			String[] auxiliar = permutar(aux[i].substring(1),getFactorial(l-1)); 
			for(int j=0;j<auxiliar.length;j++)
			{
				per[pos]=aux[i].charAt(0)+auxiliar[j];
				pos++;
			}			
		}
		return per;
 
	}
	public static String[] permutacion(String cadena)
	{
		int n = cadena.length();
		String temporal="";
		String[] vector = new String[n];
		vector[0]=cadena;
		for(int i=1;i<n;i++)
		{
			for(int j=0;j<n;j++)
			{
				if(j==n-1)
					temporal = cadena.charAt(j)+temporal;
				else 
					temporal += cadena.charAt(j);
			}
			cadena=temporal;
			vector[i]=temporal;
			temporal="";
		}
		return vector;
	}
	
	public static int getFactorial (int n)
	{
		int result;
		if(n==1||n==0)
			return 1;
 
		result = getFactorial(n-1)*n;
		return result;
	}
	
	public static void mostrar (ArrayList<String> vector)
	{
		for(int i= 0; i< vector.size();i++)
		{
			System.out.println(vector.get(i));
		}
		System.out.println("tamanio: "+vector.size());
	}
}