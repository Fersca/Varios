package colordetecter

import java.io.Serializable;

class Producto implements Serializable {
	
	String itemId
	String titulo
	Double precio
	String imagen
	String categoria
	
	Integer negro=0
	Integer blanco=0
	Integer rojo=0
	Integer verde=0
	Integer azul=0
	Integer naranja=0
	Integer violeta=0
	Integer amarillo=0
	Integer gris=0
	Integer celeste=0
	Integer rosa=0
	Integer marron=0
	
	Integer negroBorde=0
	Integer blancoBorde=0
	Integer rojoBorde=0
	Integer verdeBorde=0
	Integer azulBorde=0
	Integer naranjaBorde=0
	Integer violetaBorde=0
	Integer amarilloBorde=0
	Integer grisBorde=0
	Integer celesteBorde=0
	Integer rosaBorde=0
	Integer marronBorde=0

	Integer negroCentro=0
	Integer blancoCentro=0
	Integer rojoCentro=0
	Integer verdeCentro=0
	Integer azulCentro=0
	Integer naranjaCentro=0
	Integer violetaCentro=0
	Integer amarilloCentro=0
	Integer grisCentro=0
	Integer celesteCentro=0
	Integer rosaCentro=0
	Integer marronCentro=0


	static constraints = {
		negro(nullable:true)
		blanco(nullable:true)
		rojo(nullable:true)
		verde(nullable:true)
		azul(nullable:true)
		naranja(nullable:true)
		violeta(nullable:true)
		amarillo(nullable:true)
		gris(nullable:true)
		celeste(nullable:true)
		rosa(nullable:true)
		marron(nullable:true)
		negroBorde(nullable:true)
		blancoBorde(nullable:true)
		rojoBorde(nullable:true)
		verdeBorde(nullable:true)
		azulBorde(nullable:true)
		naranjaBorde(nullable:true)
		violetaBorde(nullable:true)
		amarilloBorde(nullable:true)
		grisBorde(nullable:true)
		celesteBorde(nullable:true)
		rosaBorde(nullable:true)
		marronBorde(nullable:true)
		negroCentro(nullable:true)
		blancoCentro(nullable:true)
		rojoCentro(nullable:true)
		verdeCentro(nullable:true)
		azulCentro(nullable:true)
		naranjaCentro(nullable:true)
		violetaCentro(nullable:true)
		amarilloCentro(nullable:true)
		grisCentro(nullable:true)
		celesteCentro(nullable:true)
		rosaCentro(nullable:true)
		marronCentro(nullable:true)
    	}

	static mapping = {
		version false
		cache false
	}
	
}
