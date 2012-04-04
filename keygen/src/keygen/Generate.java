package keygen;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Generate {

	String[] array1 = new String[15];
	
	String[] array2 = array1;
	String[] array3 = array1;
	String[] array4 = array1;
	String[] array5 = array1;
	String[] array6 = array1;
	String[] array7 = array1;
	
	
	public static void main(String[] args) {
		
		Generate g = new Generate();
		g.load();
		g.run();

	}

	private void run() {

     				
		StringBuilder sb = new StringBuilder();

		System.out.println("Generando...1 caracter ");
		for (int i = 0;i<15;i++){
			sb.append(array1[i]+"\n");
		}
		graba(sb.toString(), false);
		sb = new StringBuilder(); 	



		System.out.println("Generando...2 caracters ");
		for (int m = 0;m<15;m++){
			for (int n = 0;n<15;n++){
				sb.append(array5[m]+array6[n]+"\n");
			}
		}
		graba(sb.toString(), true);
		sb = new StringBuilder(); 	
		
		System.out.println("Generando...3 caracters ");		
		for (int l = 0;l<15;l++){
			for (int m = 0;m<15;m++){
				for (int n = 0;n<15;n++){
					sb.append(array4[l]+array5[m]+array6[n]+"\n");
				}
			}
		}					
		graba(sb.toString(), true);
		sb = new StringBuilder(); 	
		
		System.out.println("Generando...4 caracters ");
		for (int k = 0;k<15;k++){
			for (int l = 0;l<15;l++){
				for (int m = 0;m<15;m++){
					for (int n = 0;n<15;n++){
						sb.append(array3[k]+array4[l]+array5[m]+array6[n]+"\n");
					}
				}
			}					
		}				
		graba(sb.toString(), true);
		sb = new StringBuilder(); 	
		
		System.out.println("Generando...5 caracters ");		
		for (int j = 0;j<15;j++){
			for (int k = 0;k<15;k++){
				for (int l = 0;l<15;l++){
					for (int m = 0;m<15;m++){
						for (int n = 0;n<15;n++){
							sb.append(array2[j]+array3[k]+array4[l]+array5[m]+array6[n]+"\n");
						}
					}
				}					
			}				
		}			
		graba(sb.toString(), true);
		sb = new StringBuilder(); 	
		

		System.out.println("Generando...6 caracters ");
		for (int i = 0;i<15;i++){
			for (int j = 0;j<15;j++){
				for (int k = 0;k<15;k++){
					for (int l = 0;l<15;l++){
						for (int m = 0;m<15;m++){
							for (int n = 0;n<15;n++){
								sb.append(array1[i]+array2[j]+array3[k]+array4[l]+array5[m]+array6[n]+"\n");
							}
						}
					}					
				}				
			}			
		}
		
		graba(sb.toString(), true);
		sb = new StringBuilder(); 	

		int cant=0;
		System.out.println("Generando...7 caracters ");
		for (int i = 0;i<15;i++){
			for (int j = 0;j<15;j++){
				for (int k = 0;k<15;k++){
					for (int l = 0;l<15;l++){
						for (int m = 0;m<15;m++){
							for (int n = 0;n<15;n++){
								for (int o = 0;o<15;o++){
									sb.append(array1[i]+array2[j]+array3[k]+array4[l]+array5[m]+array6[n]+array7[o]+"\n");
									cant++;
									if (cant%3000000==0){										
										graba(sb.toString(), true);
										sb = new StringBuilder(); 											
									}
								}
							}
						}
					}					
				}				
			}			
		}
		graba(sb.toString(), true);
		sb = new StringBuilder(); 	
		
		System.out.println("Fin.");
	
	}
	
	private void graba (String st, boolean append){

		try {
			System.out.println("Grabando...");
		    FileWriter fstream = new FileWriter("write6.txt",append);
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(st);	    
		    out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void load() {

		array1[0]="e";
		array1[1]="a";
		array1[2]="o";
		array1[3]="i";
		array1[4]="s";
		array1[5]="n";
		array1[6]="d";
		array1[7]="r";
		array1[8]="u";
		array1[9]="l";
		array1[10]="t";
		array1[11]="c";
		array1[12]="p";
		array1[13]="m";
		array1[14]="y";
				
		array2[0]="e";
		array2[1]="a";
		array2[2]="o";
		array2[3]="i";
		array2[4]="s";
		array2[5]="n";
		array2[6]="d";
		array2[7]="r";
		array2[8]="u";
		array2[9]="l";
		array2[10]="t";
		array2[11]="c";
		array2[12]="p";
		array2[13]="m";
		array2[14]="y";
		
		array3[0]="e";
		array3[1]="a";
		array3[2]="o";
		array3[3]="i";
		array3[4]="s";
		array3[5]="n";
		array3[6]="d";
		array3[7]="r";
		array3[8]="u";
		array3[8]="l";
		array3[10]="t";
		array3[11]="c";
		array3[12]="p";
		array3[13]="m";
		array3[14]="y";

		array4[0]="e";
		array4[1]="a";
		array4[2]="o";
		array4[3]="i";
		array4[4]="s";
		array4[5]="n";
		array4[6]="d";
		array4[7]="r";
		array4[8]="u";
		array4[9]="l";
		array4[10]="t";
		array4[11]="c";
		array4[12]="p";
		array4[13]="m";
		array4[14]="y";

		array5[0]="e";
		array5[1]="a";
		array5[2]="o";
		array5[3]="i";
		array5[4]="s";
		array5[5]="n";
		array5[6]="d";
		array5[7]="r";
		array5[8]="u";
		array5[9]="l";
		array5[10]="t";
		array5[11]="c";
		array5[12]="p";
		array5[13]="m";
		array5[14]="y";

		array6[0]="e";
		array6[1]="a";
		array6[2]="o";
		array6[3]="i";
		array6[4]="s";
		array6[5]="n";
		array6[6]="d";
		array6[7]="r";
		array6[8]="u";
		array6[9]="l";
		array6[10]="t";
		array6[11]="c";
		array6[12]="p";
		array6[13]="m";
		array6[14]="y";
		
		array7[0]="e";
		array7[1]="a";
		array7[2]="o";
		array7[3]="i";
		array7[4]="s";
		array7[5]="n";
		array7[6]="d";
		array7[7]="r";
		array7[8]="u";
		array7[9]="l";
		array7[10]="t";
		array7[11]="c";
		array7[12]="p";
		array7[13]="m";
		array7[14]="y";	
			
	}

	/*
	 
e	16.78	16.78
a	28.74	11.96
o	37.43	8.69
l	45.8	8.37
s	53.68	7.88
n	60.69	7.01
d	67.56	6.87
r	72.5	4.94
u	77.3	4.80
I	81.45	4.15
t	84.76	3.31
c	87.68	2.92
p	90.45	2.77
m	92.57	2.12
y	94.11	1.54
------------------ 95%
q	95.64	1.53
b	96.56	0.92
h	97.45	0.89
g	98.18	0.73
f	98.7	0.52
v	99.09	0.39
j	99.39	0.30
ñ	99.68	0.29
z	99.83	0.15
x	99.89	0.06
k	99.89	0.00
w	99.89	0.00
	 		
	 */
}
