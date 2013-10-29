package colores;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;


/**
 * Detecta colores en imagenes
 * @author fscasserra
 */
public class ColorDetector {

	private static final int TAMANO_BLOQUE = 20;
	private static final double PORC_BORDE = 0.1;//0.05;
	private static final double PORC_CENTRO = 0.2;//0.125;
	
	public ImageInfo detectColors(String foto, Boolean isURL, Boolean createFiles, String result) throws Exception {
					
		int c;
		int red=0;
		int green=0;
		int blue=0;
		
		//Crea las matrices para trabajar
		ArrayList<Bloque> bloques = new ArrayList<Bloque>();
		String[][] matriz = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColorNet = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		int[][] matrizColo = new int[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColoAprox = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColoAproxColor = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColoAproxColorNum = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColorBorde = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		String[][] matrizColorCentro = new String[TAMANO_BLOQUE][TAMANO_BLOQUE];
		
		//limpia la matriz
		for (int i=0;i<TAMANO_BLOQUE;i++)
			for (int j=0;j<TAMANO_BLOQUE;j++)
				matriz[i][j]="-";
		
		try {

			//Ontiene la imagen a calcular
			BufferedImage image;
			if (isURL){
				URL url = new URL(foto);
				image = ImageIO.read(url);
			}
			else {
				File file = new File(foto);
				image = ImageIO.read(file);
			}
			
			//Calcula los anchos y altos de los bloques
			int ancho = image.getWidth();
			int alto = image.getHeight();
			int anchoBloque = ancho/TAMANO_BLOQUE;
			int altoBloque = alto/TAMANO_BLOQUE;
			
			//Llena la lista de bloques
			Bloque bloque;
			for (int row=0;row<=(TAMANO_BLOQUE-1);row++){
				for (int col=0;col<=(TAMANO_BLOQUE-1);col++){
					bloque = new Bloque(row, col);
					bloques.add(bloque);
				}
			}
			
			//Calcula el promedio de color de cada bloque
			for (Bloque blo : bloques) {
				//Resetea los colores
				red=0;
				green=0;
				blue=0;				
				//obtiene el color de cada pixel y acumula las cantidades
				for (int x=(anchoBloque*blo.row); x<((anchoBloque*blo.row)+anchoBloque);x++){
					for (int y=(altoBloque*blo.col); y<((altoBloque*blo.col)+altoBloque);y++){
						c = image.getRGB(x,y);
						red = red + ((c & 0x00ff0000) >> 16);
						green = green + ((c & 0x0000ff00) >> 8);
						blue = blue +(c & 0x000000ff);
					}
				}
				//guarda el promedio de colores
				int superficieBloque = anchoBloque*altoBloque;
				blo.red=red/superficieBloque;
				blo.green=green/superficieBloque;
				blo.blue=blue/superficieBloque;
				
				//verifica los colores de cada bloque
				blo.verificar(createFiles);		
				
				//Guarda la informacion en las matrices
				if (createFiles){
					matriz[blo.col][blo.row] = blo.status;
					matrizColorNet[blo.col][blo.row] = blo.red+","+blo.green+","+blo.blue;
					matrizColo[blo.col][blo.row] = blo.color;
					matrizColoAprox[blo.col][blo.row] = blo.statusAprox;
					matrizColoAproxColorNum[blo.col][blo.row] = ""+blo.colorAprox.numero;
				}
				
				//Esta matriz se necesita si o si
				matrizColoAproxColor[blo.col][blo.row] = blo.colorAprox.nombre;
				
				if (blo.col<(TAMANO_BLOQUE*PORC_BORDE) || blo.row<(TAMANO_BLOQUE*PORC_BORDE) || blo.col>=(TAMANO_BLOQUE*(1-PORC_BORDE)) || blo.row>=(TAMANO_BLOQUE*(1-PORC_BORDE))){
					matrizColorBorde[blo.col][blo.row] = blo.colorAprox.nombre;
				} else {
					if (blo.col<(TAMANO_BLOQUE*PORC_CENTRO) || blo.row<(TAMANO_BLOQUE*PORC_CENTRO) || blo.col>=(TAMANO_BLOQUE*(1-PORC_CENTRO)) || blo.row>=(TAMANO_BLOQUE*(1-PORC_CENTRO))) { // no hace nada con esta franja
					} else {
						matrizColorCentro[blo.col][blo.row] = blo.colorAprox.nombre;
					}
				}
				
				
			}
					
			//Cuenta la cantidad de colores 
			Map<String, Integer> colores 		= cuentaColores(matrizColoAproxColor);
			Map<String, Integer> coloresBorde 	= cuentaColores(matrizColorBorde);
			Map<String, Integer> coloresCentro 	= cuentaColores(matrizColorCentro);
			
			ImageInfo imageInfo = new ImageInfo();
			imageInfo.alto=alto;
			imageInfo.ancho=ancho;
			imageInfo.foto=foto;
			
			//Filtra los colores y deja solo los que aparecen mas			
			ArrayList<ColorDetected> detectados 		= detectar(colores, TAMANO_BLOQUE*TAMANO_BLOQUE);
			ArrayList<ColorDetected> detectadosBorde 	= detectar(coloresBorde, (int)(TAMANO_BLOQUE*TAMANO_BLOQUE)-(int)(TAMANO_BLOQUE*(1-(PORC_BORDE*2))*TAMANO_BLOQUE*(1-(PORC_BORDE*2)))); //576 = (40x40)-(36x36)
			ArrayList<ColorDetected> detectadosCentro 	= detectar(coloresCentro, (int)(TAMANO_BLOQUE*(1-(PORC_CENTRO*2))*TAMANO_BLOQUE*(1-(PORC_CENTRO*2)))); //30x30 del centro
			ArrayList<ColorDetected> detectadosProducto   = detectarProducto(detectadosBorde,detectadosCentro);
			
			if (createFiles){
				
				//Info de la imagen
				System.out.println(ancho+" - "+alto);
				System.out.println("anchobloque: "+anchoBloque);
				System.out.println("altobloque: "+altoBloque);
				
				//Crea el HTML de ejemplo
				FileHelper.createFile(matriz, matrizColoAprox, matrizColoAproxColor, matrizColoAproxColorNum, colores, detectados);
				
				//Crea la imagen en el disco de ejemplo
				FileHelper.createImgage(matrizColo, TAMANO_BLOQUE, matrizColorNet, result);
			}

			imageInfo.detectados=detectados;
			imageInfo.detectadosBorde=detectadosBorde;
			imageInfo.detectadosCentro=detectadosCentro;
			imageInfo.detectadosProducto=detectadosProducto;
			
			return imageInfo;
		} catch (Exception e){
			throw e;
		}
	}

	private ArrayList<ColorDetected> detectarProducto(
			ArrayList<ColorDetected> detectadosBorde,
			ArrayList<ColorDetected> detectadosCentro) {

		ArrayList<ColorDetected> detectadosProducto = new ArrayList<ColorDetected>(); 
			
		int queda = 100;
		
		//busca el color del centro que sea igual al maximo del borde:
		for (ColorDetected colorDetected : detectadosCentro) {
			if(colorDetected.nombre.equals(detectadosBorde.get(0).nombre)){
				queda = 100 - colorDetected.porcentage;  
				break;
			}
		}
	
		System.out.println("Color Borde: "+detectadosBorde.get(0).nombre+", queda: "+queda);
		
		//el 50% del centro es del mismo color del borde, debe ser un objeto grande, no sacar este color
		if (queda<=60){

			System.out.println("El centro es igual al borde");
			
			//Se copian los colores del centro al del producto
			for (ColorDetected colorDetected : detectadosCentro) {
				detectadosProducto.add(colorDetected);	
			}
			
		} else {

			ColorDetected color;
			int canti;
			for (ColorDetected colorDetected : detectadosCentro) {
				
				//si encuentro el mismo color del borde
				if(colorDetected.nombre.equals(detectadosBorde.get(0).nombre)){
					//Le descuento el total al color que voy a sacar
					color = new ColorDetected(colorDetected.nombre,0,0);
				} else {
					//recalcula el nuevo porcentaje
					canti = (colorDetected.porcentage*100) / queda;							
					color = new ColorDetected(colorDetected.nombre,canti,0);
				}

				detectadosProducto.add(color);
			}
			
		}
		
		Collections.sort(detectadosProducto);
		
		for (ColorDetected colorDetected : detectadosProducto) {
			System.out.println(colorDetected.nombre +" - " +colorDetected.porcentage);
		}
		
		return detectadosProducto;

	}

	private ArrayList<ColorDetected> detectar(Map<String, Integer> colores, int cant) {

		//double tamanio = TAMANO_BLOQUE*TAMANO_BLOQUE;
		double tamanio = (double)cant;
		
		ArrayList<ColorDetected> array = new ArrayList<ColorDetected>();
		
		Iterator<Entry<String, Integer>> it = colores.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();

	        if (pairs.getKey()!=null){

		        double porc = (double)pairs.getValue()/tamanio;
		        //System.out.println(cant+"--> "+pairs.getKey()+" - "+pairs.getValue());		        
	        	porc = porc * 100;
	        	ColorDetected col = new ColorDetected(pairs.getKey(),(int)porc,0);
	        	array.add(col);
	        		        	
	        }
	        
	    }
		
	    Collections.sort(array);
	    int i =1;
	    for (ColorDetected colorDetected : array) {
			colorDetected.orden=i;
			i++;
		}
	    
		return array;
	}

	private HashMap<String, Integer> cuentaColores(String[][] matrizColoAprox) {
		
		HashMap<String, Integer> tabla = new HashMap<String, Integer>();
		
		for (String[] s1 : matrizColoAprox) {
			for (String s2 : s1){
				if (s2!=null){
					if (tabla.containsKey(s2))
						tabla.put(s2, tabla.get(s2)+1);
					else
						tabla.put(s2, 1);
				}
			}
		}

		return tabla;
	}

	public static void main(String[] args) {

		ColorDetector border = new ColorDetector();
		try {
			//border.detectWhiteBorder("c:\\Users\\Fersca\\Pictures\\Colores\\prueba1.jpg",false,false);
			//border.detectWhiteBorder("http://upload.wikimedia.org/wikipedia/commons/thumb/d/d6/GermanShep1_wb.jpg/250px-GermanShep1_wb.jpg",true,false);
			
			
			/*
			blanco
			negro 
			plata
			azul
			gris
			*/
			
			/*
			 blanco 25%
			 negro  22%
			 plata  16%
			 gris   12%
			 rojo   7%
			 azul   7%
			 amarillo / dorado 6%
			 verde  3%
			 
			 */
			
			
			//naranja
			/*
			ImageInfo imgN1 = border.detectColors("http://bimg2.mlstatic.com/miralas-hermosisimas-musculosas-y-remerones-largos_MLA-F-3096619678_092012.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN2 = border.detectColors("http://mla-s2-p.mlstatic.com/mini-cooper-s-cabrionaranja05-unico-en-argentinapermuto-6452-MLA5062730458_092013-F.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN3 = border.detectColors("http://bimg1.mlstatic.com/lamborghini-aventador-lp-700-4-naranja-burago-118_MLA-F-137921457_6126.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN4 = border.detectColors("http://www.historiasdelmotor.com/images/2008/07/seat-ibiza-sportcoupe-naranja-lamborghini-3.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN5 = border.detectColors("http://cdn.inaxiom.net/web/wp-content/uploads/2010/09/Fiat-500-Arancia-00-620x412.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN6 = border.detectColors("http://mpe-s2-p.mlstatic.com/470-MPE3607107206_122012-O.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN7 = border.detectColors("http://4.bp.blogspot.com/-LNHumoig_hw/TiG4ghEKekI/AAAAAAAAAWE/ohNOsOIoUmc/s1600/Audi+S3+Naranja.jpeg",true,true,"1,0,0,0,0,0,0");	
			ImageInfo imgN8 = border.detectColors("http://www.estuimagen.com/imagenes/coches/auto-naranja-19.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN9 = border.detectColors("http://imganuncios.mitula.net/due%C3%B1o_vende_r9_gtl_mod_94_gnc_papeles_al_dia_98901997460477168.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgN10 = border.detectColors("http://cdn1.clasificados.com/ar/pictures/photos/000/149/749/original_fotos_taunus_003.jpg",true,true,"1,0,0,0,0,0,0");
			*/
			
			//rojo
			//desalineada
			//ImageInfo imgR1 = border.detectColors("http://www.todohumor.com/UserFiles/Image/fondos/2010/Mayo/automoderno.jpg",true,true,"0,1,0,0,0,0,0");

			/*
			ImageInfo imgR1 = border.detectColors("http://mla-s2-p.mlstatic.com/renault-18-break-4x4-muy-buena-con-gnc-5493-MLA4964073436_092013-F.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR2 = border.detectColors("http://www.motorexperience.es/images/cars/ferrari458600.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR3 = border.detectColors("http://www.w7swall.com/wp-content/wallpapers/cars/porche-carrera-porsche-510283.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR4 = border.detectColors("http://bimg1.mlstatic.com/fiat-uno-no-147-128-duna-palio-corsa-gol_MLA-F-5052769320_092013.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR5 = border.detectColors("http://mla-s1-p.mlstatic.com/citroen-3cv-viajero-7241-MLA5183925751_102013-F.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR6 = border.detectColors("http://mla-s1-p.mlstatic.com/full-full-motor-14-fin-del-2010-7548-MLA5237539691_102013-F.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR7 = border.detectColors("http://mla-s1-p.mlstatic.com/chevrolet-corsa-3ptas-full-full-2008-7646-MLA5248905635_102013-F.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR8 = border.detectColors("http://mla-s2-p.mlstatic.com/fiat-147-vivace-mod-96-3er-duena-impecable-25000-7625-MLA5252977132_102013-F.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR9 = border.detectColors("http://mla-s2-p.mlstatic.com/renault-19-re-con-gnc-aa-7538-MLA5240474169_102013-F.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR10 = border.detectColors("http://mla-s1-p.mlstatic.com/saveiro-16-cab-ext-130-hp-7150km-impecable-7526-MLA5237171831_102013-F.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR11 = border.detectColors("http://mla-s2-p.mlstatic.com/ambulancia-mercedes-benz-mb180-bomberos-vende-7557-MLA5241821228_102013-F.jpg",true,true,"0,1,0,0,0,0,0");	
			ImageInfo imgR12 = border.detectColors("http://mla-s1-p.mlstatic.com/cupe-taunus-gt-con-aa-y-asientos-de-cuero-5338-MLA4373884433_052013-F.jpg",true,true,"0,1,0,0,0,0,0");
			*/
			ImageInfo imgR13 = border.detectColors("http://www.pitheringabout.com/wp-content/uploads/2009/07/dscf1875.JPG",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR14 = border.detectColors("http://www.ghulmil.com/wp-content/uploads/awesome-red-car-beautiful-hd-wallpapers1.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR15 = border.detectColors("http://es.best-wallpaper.net/wallpaper/1920x1080/1106/Ferrari-red-car_1920x1080.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR16 = border.detectColors("http://upload.wikimedia.org/wikipedia/en/9/91/Award_Winning_Red_Car.jpg",true,true,"0,1,0,0,0,0,0");
			ImageInfo imgR17 = border.detectColors("http://www.pressportal.com.au/tpllib/img.php?im=2008/10/img_327_3536_3568.jpeg&w=750&h=500",true,true,"0,1,0,0,0,0,0");
					
			//gris
			/*
			ImageInfo imgG1 = border.detectColors("http://img1.mlstatic.com/mini-cooper-s-hot-pepper-16-turbo-tomo-cuatri-parrillero_MLA-O-4656120149_072013.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG2 = border.detectColors("http://mla-s1-p.mlstatic.com/volkswagen-vw-vento-20-tdi-4p-2011-el-puente-automotore-5989-MLA5017888927_092013-F.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG3 = border.detectColors("http://mla-s1-p.mlstatic.com/porsche-911-carrera-s-permuto-moto-departamento-m1-135-m3-5134-MLA4222409793_042013-F.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG4 = border.detectColors("http://www.pfautomotores.com.ar/photos/412/imagen1.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG5 = border.detectColors("http://images02.olx.cl/ui/19/71/36/1328104705_309586536_3-VENDO-AUTO-HYUNDAI-I-10-COLOR-GRIS-AnO-2009-2-DUEnO-Lautaro.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG6 = border.detectColors("http://staticcl.lavozdelinterior.com.ar/files/imagecache/ficha_aviso_628_418/avisos/aviso_auto/aviso-auto-chevrolet-corsa-607836.JPG",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG7 = border.detectColors("http://images02.olx.cl/ui/18/06/13/1373899471_512660313_1-Fotos-de--auto-kia-rio4-2013-modelo-deportivo-2500-km-nuevo-color-gris-platinado-semi-full.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG8 = border.detectColors("http://images02.olx.com.py/ui/16/08/44/1360446611_480730444_2-Remato-por-viaje-Nissan-Armada-SE-2008-gris-humo-Muy-buen-estado-Asuncion.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG9 = border.detectColors("http://safe-img01.olx.com.mx/ui/2/30/69/1372508550_522262169_2-Fiesta-hb-2003-color-gris-oscuro-4-cil-16-economico-CLIMA-o-cambio-Guadalupe.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG10 = border.detectColors("http://images04.olx.com.pe/ui/20/45/45/1380681861_551704045_3-Volswagen-gol-2010-gris-metalico-Autos.jpg",true,true,"0,0,1,0,0,0,0");
			*/
			ImageInfo imgG11 = border.detectColors("http://jordanmorningstarblog.files.wordpress.com/2008/11/dsc_0729.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG12 = border.detectColors("http://www.rapidcars.com/images/e92m3.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG13 = border.detectColors("http://s1.cdn.autoevolution.com/images/news/gallery/medium/new-lexus-gs-350-gets-matte-dark-grey-wrap-photo-gallery-medium_11.jpg",true,true,"0,0,1,0,0,0,0");		
			ImageInfo imgG14 = border.detectColors("http://images.nysportscars.com/pictures/64122171.jpg",true,true,"0,0,1,0,0,0,0");
			ImageInfo imgG15 = border.detectColors("http://www.p1woc.co.uk/sky/sky1.jpg	",true,true,"0,0,1,0,0,0,0");
				
				
			//plata
			/*
			ImageInfo imgP1 = border.detectColors("http://bimg2.mlstatic.com/hilux-sr-30-4x4-dc-ano-2008-oportunidad-_MLA-F-5075976690_092013.jpg",true,true,"1,0,0,0,0,0,0");			
			ImageInfo imgP2 = border.detectColors("http://bimg2.mlstatic.com/corsa-classic-16-n-super-vendo-o-permuto-_MLA-F-5053040292_092013.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP3 = border.detectColors("http://mla-s2-p.mlstatic.com/golf-16-impecable-permuto-x-moto-honda-twuister-6689-MLA5097011487_092013-F.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP4 = border.detectColors("http://mla-s1-p.mlstatic.com/vendo-toyota-hilux-sr-4x2-30-ano-2008-6941-MLA5137422547_102013-F.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP5 = border.detectColors("http://mla-s1-p.mlstatic.com/volkswagen-gol-dublin-19-sd-3-puertas-7095-MLA5152985417_102013-F.jpg",true,true,"1,0,0,0,0,0,0");		
			ImageInfo imgP6 = border.detectColors("http://mla-s2-p.mlstatic.com/chevrolet-corsa-08-gl-full-gnc-grande-muy-original-y-cuidado-6099-MLA4584255587_072013-F.jpg",true,true,"1,0,0,0,0,0,0");	
			ImageInfo imgP7 = border.detectColors("http://images01.olx.cl/ui/8/36/06/1282944106_116243006_5-Susuki-Gran-Nomade-2004-Full-4x4-6700000-Vehiculos-1282944106.jpg",true,true,"1,0,0,0,0,0,0");	
			ImageInfo imgP8 = border.detectColors("http://images01.olx.com.ar/ui/7/42/62/1370741848_511414062_4-Toyota-corolla-xei-18-2010-gris-plata-Vehiculos.jpg",true,true,"1,0,0,0,0,0,0");	
			ImageInfo imgP9 = border.detectColors("http://images01.olx.com.ar/ui/20/16/42/1377639057_540231442_1-se-vende-ford-taunus-gxl-color-gris-plata-grand-bourg-callao-y-burmeister.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP10 = border.detectColors("http://media-sa.viva-images.com/vivastreet_ar/clad/b2/3/79559455/large/1.jpg?dt=aac4be09bbc16db5f74dae6536bf7adc",true,true,"1,0,0,0,0,0,0");		
			*/
			ImageInfo imgP11 = border.detectColors("http://www.my-lhd.co.uk/images/voitures/1784a-car-bmw-z4-1.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP12 = border.detectColors("http://images.gtcarlot.com/pictures/25653111.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP13 = border.detectColors("http://imganuncios.mitula.net/mercedes_benz_s350l_96714964496420444.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP14 = border.detectColors("http://www.carandclassic.co.uk/uploads/cars/mercedes/4036211.jpg",true,true,"1,0,0,0,0,0,0");
			ImageInfo imgP15 = border.detectColors("http://favimages.net/wp-content/uploads/2013/09/ferrari-456-gt-silver-grey-car-front-view-headlights.jpg",true,true,"1,0,0,0,0,0,0");
					
			//negro
			/*
			ImageInfo imgNE1 = border.detectColors("http://img1.mlstatic.com/bmw-series-3-335i-m-sport-biturbo-steptronic_MLA-O-5085301356_092013.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE2 = border.detectColors("http://bimg1.mlstatic.com/bmw-330-i-265cv-automatico-2010-impecable-idem-a-okm-permuto_MLA-F-4464781334_062013.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE3 = border.detectColors("http://bimg2.mlstatic.com/bmw-335i-biturbo-sedan_MLA-F-5096800211_092013.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE4 = border.detectColors("http://mla-s2-p.mlstatic.com/peugeot-307-negro-flamante-titular-permuto-linea-nueva-vtv-7651-MLA5248074987_102013-F.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE5 = border.detectColors("http://mla-s1-p.mlstatic.com/volkswagen-amarok-20-180cv-negra--7680-MLA5254309894_102013-F.jpg",true,true,"0,0,0,1,0,0,0");	
			ImageInfo imgNE6 = border.detectColors("http://mla-s2-p.mlstatic.com/suzuki-swift-15-vvt-bluetooth-aux-usb-apoyabrazos-molduras-7587-MLA5241872849_102013-F.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE7 = border.detectColors("http://mla-s1-p.mlstatic.com/ecosport-2010-u-n-i-c-a-caccesorios-74000km-permuto-7664-MLA5247724587_102013-F.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE8 = border.detectColors("http://mla-s2-p.mlstatic.com/chevrolet-corsa-3p-negro-full-full-47000-kms-excelente-7638-MLA5256932199_102013-F.jpg",true,true,"0,0,0,1,0,0,0");	
			ImageInfo imgNE9 = border.detectColors("http://mla-s2-p.mlstatic.com/clio-yahoo-2008-4132-MLA4896612255_082013-F.jpg",true,true,"0,0,0,1,0,0,0");	
			ImageInfo imgNE10 = border.detectColors("http://mla-s1-p.mlstatic.com/206-xs-full-full-vendo-o-permuto-552-MLA4689845583_072013-F.jpg",true,true,"0,0,0,1,0,0,0");	
			*/
			ImageInfo imgNE11 = border.detectColors("http://www.technobuffalo.com/wp-content/uploads/2013/03/black-car-service-uber.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE12 = border.detectColors("http://www.team-bhp.com/forum/iipcache/73996.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE13 = border.detectColors("http://www.12pointsignworks.com/Portals/36109/images/12PSW-Image-045.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE14 = border.detectColors("http://www.z1auto.com/Images/blackWRX.jpg",true,true,"0,0,0,1,0,0,0");
			ImageInfo imgNE15 = border.detectColors("http://fancytuning.com/wp-content/uploads/2010/06/sr-auto-audi-r8-valkyrie.jpg",true,true,"0,0,0,1,0,0,0");
				
			//blanco
			/*
			ImageInfo imgB1 = border.detectColors("http://mla-s1-p.mlstatic.com/fiat-uno-s-272-MLA4675875481_072013-F.jpg",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB2 = border.detectColors("http://mla-s1-p.mlstatic.com/renault-trafic-14-7614-MLA5247308146_102013-F.jpg",true,true,"0,0,0,0,1,0,0");	
			ImageInfo imgB3 = border.detectColors("http://mla-s1-p.mlstatic.com/fiat-500-sport-14-mt-multiair-16v-105cv-7561-MLA5236852956_102013-F.jpg",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB4 = border.detectColors("http://mla-s1-p.mlstatic.com/peugeot-207-gti-156cv-3ptas-5657-MLA4977229087_092013-F.jpg",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB5 = border.detectColors("http://mla-s2-p.mlstatic.com/chevrolet-suzuki-grand-vitara-20-tdi-2001-4x4-ctecho-7680-MLA5255978467_102013-F.jpg",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB6 = border.detectColors("http://mla-s1-p.mlstatic.com/volkswagen-fox-impecable-7620-MLA5257362416_102013-F.jpg",true,true,"0,0,0,0,1,0,0");	
			ImageInfo imgB7 = border.detectColors("http://mla-s2-p.mlstatic.com/renault-clio-authentic-dci-15-2005-primera-mano-7523-MLA5241973931_102013-F.jpg",true,true,"0,0,0,0,1,0,0");	
			ImageInfo imgB8 = border.detectColors("http://mla-s1-p.mlstatic.com/volkswagen-gol-power-16-3ptasespecial-unico-dueno-7684-MLA5247581964_102013-F.jpg",true,true,"0,0,0,0,1,0,0");	
			ImageInfo imgB9 = border.detectColors("http://mla-s1-p.mlstatic.com/peugeot-207-xt-3-ptas-blanco-7693-MLA5248680820_102013-F.jpg",true,true,"0,0,0,0,1,0,0");	
			ImageInfo imgB10 = border.detectColors("http://mla-s1-p.mlstatic.com/bmw-335i-s-paq-m-autom-7ma-doble-embrague-levas-cedin-5292-MLA4288337360_052013-F.jpg",true,true,"0,0,0,0,1,0,0");	
			*/
			ImageInfo imgB11 = border.detectColors("http://www.here.org.uk/wp-content/uploads/2008/02/nissan-gt-r_20082.jpg",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB12 = border.detectColors("http://www.wallm.com/images/2013/03/bmw-white-car-482685.jpg",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB13 = border.detectColors("http://forums.forzamotorsport.net/photos/aaron_pass/images/2252647/original.aspx",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB14 = border.detectColors("http://www.fototime.com/A30E829D11E6B87/orig.jpg",true,true,"0,0,0,0,1,0,0");
			ImageInfo imgB15 = border.detectColors("http://www.sunrisesigns.com/Portals/84214/images/matte%20white%20wrap-resized-600.jpg",true,true,"0,0,0,0,1,0,0");
				
			//azul
			/*
			ImageInfo imgA1 = border.detectColors("http://mla-s2-p.mlstatic.com/peugeot-206-16-excelente-7017-MLA5146735986_102013-F.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA2 = border.detectColors("http://mla-s2-p.mlstatic.com/fiat-uno-2006-5-puertas-muy-buenofinancio-oportunidad-6939-MLA5140422163_102013-F.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA3 = border.detectColors("http://mla-s2-p.mlstatic.com/citron-c5-30i-v6-exclusive-aut-impec-oportunidad-1465-MLA4752549281_072013-F.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA4 = border.detectColors("http://bimg2.mlstatic.com/chevrolet-meriva-gl-plus-2005-azul-gnc_MLA-F-3319211049_102012.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA5 = border.detectColors("http://mla-s2-p.mlstatic.com/renault-sandero-stepway-luxe-16-2011-el-puente-automotores-7523-MLA5235900251_102013-F.jpg",true,true,"0,0,0,0,0,1,0");	
			ImageInfo imgA6 = border.detectColors("http://mla-s2-p.mlstatic.com/fiat-palio-fire-13-16v-aire-y-direccion-oportunidad-7502-MLA5226239736_102013-F.jpg",true,true,"0,0,0,0,0,1,0");	
			ImageInfo imgA7 = border.detectColors("http://mla-s2-p.mlstatic.com/volkswagen-bora-20-trendline-3847-MLA4874430752_082013-F.jpg",true,true,"0,0,0,0,0,1,0");	
			ImageInfo imgA8 = border.detectColors("http://mla-s2-p.mlstatic.com/peugeot-partner-patagonica-19d-primera-mano-al-dia-4858-MLA3933771377_032013-F.jpg",true,true,"0,0,0,0,0,1,0");	
			ImageInfo imgA9 = border.detectColors("http://mla-s1-p.mlstatic.com/nissan-sentra-tekna-ano-2011-con-41000km-automatico-2523-MLA4803937364_082013-F.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA10 = border.detectColors("http://mla-s2-p.mlstatic.com/volkswagen-fox-16-route-3-p-5817-MLA5003088725_092013-F.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA11 = border.detectColors("http://mla-s2-p.mlstatic.com/vw-gol-19-sd-azul-2004-km-180000-39900--7616-MLA5253972907_102013-F.jpg",true,true,"0,0,0,0,0,1,0");
			*/
			ImageInfo imgA12 = border.detectColors("http://3.bp.blogspot.com/-u_4itKBZGBM/USbCqDMWPRI/AAAAAAAAXXs/-dRIiwmDGwk/s640/bollore-bluecar-1.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA13 = border.detectColors("http://www.fiatbarchetta.com/links/images/kit12515.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA14 = border.detectColors("http://www.mcleodsbritishcars.com/code/styled-10/files/triumph-gt6-03.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA15 = border.detectColors("http://image-cache.boostcruising.com/database/journal/files/post-23512-1201399021822695246.jpg",true,true,"0,0,0,0,0,1,0");
			ImageInfo imgA16 = border.detectColors("http://www.governmentauctions.org/uploaded_images/chevycobalt-748584.JPG",true,true,"0,0,0,0,0,1,0");
				
			//verde claro
			ImageInfo imgV1 = border.detectColors("http://mla-s1-p.mlstatic.com/renault-twingo-2001-muy-lindo-doble-airbag-y-levantacristale-5554-MLA4510574583_062013-F.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV2 = border.detectColors("http://mla-s1-p.mlstatic.com/renault-gordini-5p-da-2v-1969-automotores-el-puente-5122-MLA4205628815_042013-F.jpg",true,true,"0,0,0,0,0,0,1");
			
			//puede confundir con pasto
			//ImageInfo imgV3 = border.detectColors("http://www.auto-blog.com.mx/wp-content/uploads/2012/08/Aventador-Verde-Ithaca-580x300.jpg",true,true,"0,0,0,0,0,0,1");
			
			//verde oscuro
			/*
			ImageInfo imgV3 = border.detectColors("http://images04.olx.com.pe/ui/6/92/95/1275924865_98678795_1-VENDO-AUTO-NISSAN-SENTRA-DEL-96-COLOR-VERDE-LA-VICTORIA-BALCONCILLO-1275924865.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV4 = border.detectColors("http://www.hupmobile.com.ar/Siam%20verde%20445.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV5 = border.detectColors("http://www.todoautos.com.pe/attachments/f50/122937d1221072504-toyota-corolla-1998-auto-verde.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV6 = border.detectColors("http://www.todoautos.com.pe/attachments/f39/119693d1220038086-remato-coupe-97-mecanico-auto-verde-039.jpg",true,true,"0,0,0,0,0,0,1");	
			ImageInfo imgV7 = border.detectColors("http://bimg2.mlstatic.com/mitsubishi-lancer-glxi-15-nafta-verde-1997-4-puertas_MLA-F-2932656684_072012.jpg",true,true,"0,0,0,0,0,0,1");	
			ImageInfo imgV8 = border.detectColors("http://img2.mlstatic.com/renault-twingo-97-3p-verde-oportunidad-juan-manuel-auto_MLA-O-4996053906_092013.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV9 = border.detectColors("http://images01.olx.com.ar/ui/11/82/61/1297483935_165968161_2-Fotos-de--VENDO-GOL-5-PUERTAS-COLOR-VERDE-EN-MUY-BUENAS-CONDICIONES.jpg",true,true,"0,0,0,0,0,0,1");	
			ImageInfo imgV10 = border.detectColors("http://imganuncios.mitula.net/vendo_muy_buen_estado_acepto_auto_menos_valor_96868092354435406.jpg",true,true,"0,0,0,0,0,0,1");
			 */
			
			ImageInfo imgV11 = border.detectColors("http://www.carsguide.com.au/images/uploads/imiev-green-w.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV12 = border.detectColors("http://www.whitegadget.com/attachments/car-accessories/18982d1227068174-short-list-green-car-year-auto-show-short-list-green-car-year-auto-show.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV13 = border.detectColors("http://www.examiner.com/images/blog/wysiwyg/image/Mazda_Green(1).jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV14 = border.detectColors("http://www.examiner.com/images/blog/EXID40434/images/porsche_eruf_stormster_green_exotic_car_sports.jpg",true,true,"0,0,0,0,0,0,1");
			ImageInfo imgV15 = border.detectColors("http://img.gawkerassets.com/img/18fzfegxlms5ijpg/original.jpg",true,true,"0,0,0,0,0,0,1");
			
			/*
			ImageInfo img6 = border.detectColors("http://img1.mlstatic.com/bmw-series-3-335i-m-sport-biturbo-steptronic_MLA-O-5085301356_092013.jpg",true,false,"");
			ImageInfo img7 = border.detectColors("http://bimg1.mlstatic.com/bmw-330-i-265cv-automatico-2010-impecable-idem-a-okm-permuto_MLA-F-4464781334_062013.jpg",true,false,"");
			ImageInfo img8 = border.detectColors("http://bimg2.mlstatic.com/bmw-335i-biturbo-sedan_MLA-F-5096800211_092013.jpg",true,false,"");
			 */
			//blanco
			//ImageInfo img12 = border.detectColors("http://bimg2.mlstatic.com/chrysler-pt-cruiser-24-classic_MLA-F-5045159418_092013.jpg",true,false,"");
			
			//para detectar bien el color blanco lo que puedo hacer es, si me da gris, pero hay un porcentaje de blanco, es posible que ese
			//gris sea la sombra del blanco, entonces pasarlo a blanco :)	
			
			/*
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--NARANJA-----------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(imgN1.detectadosBorde.get(0).nombre+" - "+imgN1.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgN1.detectadosProducto.get(0).nombre.equals("NA"));
			
			
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--ROJO--------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(imgR1.detectadosBorde.get(0).nombre+" - "+imgR1.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR1.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR2.detectadosBorde.get(0).nombre+" - "+imgR2.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR2.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR3.detectadosBorde.get(0).nombre+" - "+imgR3.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR3.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR4.detectadosBorde.get(0).nombre+" - "+imgR4.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR4.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR5.detectadosBorde.get(0).nombre+" - "+imgR5.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR5.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR6.detectadosBorde.get(0).nombre+" - "+imgR6.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR6.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR7.detectadosBorde.get(0).nombre+" - "+imgR7.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR7.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR8.detectadosBorde.get(0).nombre+" - "+imgR8.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR8.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR9.detectadosBorde.get(0).nombre+" - "+imgR9.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR9.detectadosProducto.get(0).nombre.equals("RO"));
			System.out.print(imgR10.detectadosBorde.get(0).nombre+" - "+imgR10.detectadosProducto.get(0).nombre);
			System.out.println(" - "+imgR10.detectadosProducto.get(0).nombre.equals("RO"));

			
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--GRIS--------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img5.detectadosBorde.get(0).nombre+" - "+img5.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img5.detectadosProducto.get(0).nombre.equals("GR"));
			System.out.print(img10.detectadosBorde.get(0).nombre+" - "+img10.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img10.detectadosProducto.get(0).nombre.equals("GR"));
			System.out.print(img11.detectadosBorde.get(0).nombre+" - "+img11.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img11.detectadosProducto.get(0).nombre.equals("GR"));			
			
			
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--NEGRO-------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img6.detectadosBorde.get(0).nombre+" - "+img6.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img6.detectadosProducto.get(0).nombre.equals("NE"));
			System.out.print(img7.detectadosBorde.get(0).nombre+" - "+img7.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img7.detectadosProducto.get(0).nombre.equals("NE"));
			System.out.print(img8.detectadosBorde.get(0).nombre+" - "+img8.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img8.detectadosProducto.get(0).nombre.equals("NE"));
			System.out.println("--------------------------------------------------------------------");
			System.out.println("--BLANCO------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------");
			System.out.print(img12.detectadosBorde.get(0).nombre+" - "+img12.detectadosProducto.get(0).nombre);
			System.out.println(" - "+img12.detectadosProducto.get(0).nombre.equals("BL"));
			*/
			
			
			ArrayList<ImageInfo> imagenes = new ArrayList<ImageInfo>();
			
			/*
			imagenes.add(imgN1);
			imagenes.add(imgN2);
			imagenes.add(imgN3);
			imagenes.add(imgN4);
			imagenes.add(imgN5);
			imagenes.add(imgN6);
			imagenes.add(imgN7);
			imagenes.add(imgN8);
			imagenes.add(imgN9);
			imagenes.add(imgN10);
			*/
			
			
			/*
			imagenes.add(imgR1);
			imagenes.add(imgR2);
			imagenes.add(imgR3);
			imagenes.add(imgR4);
			imagenes.add(imgR5);
			imagenes.add(imgR6);
			imagenes.add(imgR7);
			imagenes.add(imgR8);
			imagenes.add(imgR9);
			imagenes.add(imgR10);
			imagenes.add(imgR11);
			imagenes.add(imgR12);
			*/
			imagenes.add(imgR13);
			imagenes.add(imgR14);
			imagenes.add(imgR15);
			imagenes.add(imgR16);
			imagenes.add(imgR17);
			
			/*
			imagenes.add(imgG1);
			imagenes.add(imgG2);
			imagenes.add(imgG3);
			imagenes.add(imgG4);
			imagenes.add(imgG5);
			imagenes.add(imgG6);
			imagenes.add(imgG7);
			imagenes.add(imgG8);
			imagenes.add(imgG9);
			imagenes.add(imgG10);
			*/
			imagenes.add(imgG11);
			imagenes.add(imgG12);
			imagenes.add(imgG13);
			imagenes.add(imgG14);
			imagenes.add(imgG15);
			
			/*
			imagenes.add(imgP1);
			imagenes.add(imgP2);
			imagenes.add(imgP3);
			imagenes.add(imgP4);
			imagenes.add(imgP5);
			imagenes.add(imgP6);
			imagenes.add(imgP7);
			imagenes.add(imgP8);
			imagenes.add(imgP9);
			imagenes.add(imgP10);
			*/
			imagenes.add(imgP11);
			imagenes.add(imgP12);
			imagenes.add(imgP13);
			imagenes.add(imgP14);
			imagenes.add(imgP15);
			
			/*
			imagenes.add(imgNE1);
			imagenes.add(imgNE2);
			imagenes.add(imgNE3);
			imagenes.add(imgNE4);
			imagenes.add(imgNE5);
			imagenes.add(imgNE6);
			imagenes.add(imgNE7);
			imagenes.add(imgNE8);
			imagenes.add(imgNE9);
			imagenes.add(imgNE10);
			*/
			imagenes.add(imgNE11);
			imagenes.add(imgNE12);
			imagenes.add(imgNE13);
			imagenes.add(imgNE14);
			imagenes.add(imgNE15);
			
			/*
			imagenes.add(imgB1);
			imagenes.add(imgB2);
			imagenes.add(imgB3);
			imagenes.add(imgB4);
			imagenes.add(imgB5);
			imagenes.add(imgB6);
			imagenes.add(imgB7);
			imagenes.add(imgB8);
			imagenes.add(imgB9);
			imagenes.add(imgB10);
			*/
			imagenes.add(imgB11);
			imagenes.add(imgB12);
			imagenes.add(imgB13);
			imagenes.add(imgB14);
			imagenes.add(imgB15);
			
			/*
			imagenes.add(imgA1);
			imagenes.add(imgA2);
			imagenes.add(imgA3);
			imagenes.add(imgA4);
			imagenes.add(imgA5);
			imagenes.add(imgA6);
			imagenes.add(imgA7);
			imagenes.add(imgA8);
			imagenes.add(imgA9);
			imagenes.add(imgA10);			
			imagenes.add(imgA11);
			*/
			imagenes.add(imgA12);
			imagenes.add(imgA13);
			imagenes.add(imgA14);
			imagenes.add(imgA15);
			imagenes.add(imgA16);
			
			/*
			imagenes.add(imgV1);
			imagenes.add(imgV2);
			imagenes.add(imgV3);
			imagenes.add(imgV4);
			imagenes.add(imgV5);
			imagenes.add(imgV6);
			imagenes.add(imgV7);
			imagenes.add(imgV8);
			imagenes.add(imgV9);
			imagenes.add(imgV10);			
			*/
			imagenes.add(imgV11);
			imagenes.add(imgV12);
			imagenes.add(imgV13);
			imagenes.add(imgV14);
			imagenes.add(imgV15);
			
			FileHelper.createSummary(imagenes, "/home/fersca/summary.html");

		    File file1 = new File("/home/fersca/coloresAutosCentro50-plata-2.txt");
		    Writer output = new BufferedWriter(new FileWriter(file1));
			output.write(FileHelper.sb.toString());
			output.close();	    
			
			System.out.println("Fin");

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}