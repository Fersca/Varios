package colordetecter

import java.io.Serializable;

class Producto implements Serializable {
	
	String itemId
	String titulo
	Double precio
	String imagen
	
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
    	}

	static mapping = {
		version false
		cache false
	}
	
}
