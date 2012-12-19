import grails.converters.JSON
import colores.ColorDetector
import colordetecter.Producto

class DescargarJob {
	
	def concurrent = false
	
	static triggers = {
		simple name:'simpleTrigger2', startDelay:1, repeatInterval: 0, repeatCount: 0
	}
	
	def getJson(def url1){
		
		URL url = new URL(url1);
		URLConnection yc = url.openConnection();
		yc.addRequestProperty("Accept", "application/json");
		BufferedReader inp = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		String total="";
		while ((inputLine = inp.readLine()) != null){
		   total = total +inputLine
		}
		inp.close();
		
		def json = JSON.parse(total)
	
		return json
	}
	
	def execute() {

		Integer cantidad = 10
		Integer pagina = 0
		
		while (pagina < 40000){
		println "Pidiendo pagina: ${pagina}"
		def j = getJson("https://api.mercadolibre.com/sites/MLA/search?q=remeras&offset=${pagina}&limit=${cantidad}")
		
		ColorDetector col = new ColorDetector()
		
		j.results.each {
			
			def jitem = getJson("https://api.mercadolibre.com/items/${it.id}")
			def imagen = jitem.pictures[0].url
		
			if (imagen){
				
				println "Item: "+jitem.id+" - "+imagen
				def resultado = col.detectColors(imagen, true, false)
				
				def result = Producto.findByItemId(it.id)
			
				if (!result){

				Producto item = new Producto()
				
				item.itemId = it.id
				item.titulo = it.title
				item.precio = it.price
				item.imagen =  imagen
				
				resultado.each {
					if (it.nombre=="NE")
						item.negro=it.porcentage
					else if (it.nombre=="BL")
						item.blanco=it.porcentage
					else if (it.nombre=="AZ")
						item.azul=it.porcentage
					else if (it.nombre=="RO")
						item.rojo=it.porcentage
					else if (it.nombre=="VE")
						item.verde=it.porcentage
					else if (it.nombre=="AM")
						item.amarillo=it.porcentage
					else if (it.nombre=="NA")
						item.naranja=it.porcentage
					else if (it.nombre=="VI")
						item.violeta=it.porcentage
					else if (it.nombre=="MA")
						item.marron=it.porcentage
					else if (it.nombre=="RS")
						item.rosa=it.porcentage
					else if (it.nombre=="GR")
						item.gris=it.porcentage
					else if (it.nombre=="CE")
						item.celeste=it.porcentage
		
				}
				
				if (!item.save(flush:true)){
					println item.errors
					println "Error"
				} else {
					println "Guardado OK"
				}
				} else {
				println "Ya existe: "+it.id
				}
			}
			
			println "-------------------------------"
		}
		pagina = pagina + 10
		}
		println "Fin"
	}
	
}
