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
		Integer pagina = 1130
		Integer pedido = 0 //0,1,2
		
		while (pagina < 14000){
			try {
			def categoria
			if (pedido==0){
				categoria="MLA7291" //remeras
			} else {
				if (pedido==1){
					categoria="MLA3111" //zapatos	
				} else {
					categoria="MLA5527" //carteras
 				}
			}
			
			println "Pidiendo pagina: ${pagina}"
			def j = getJson("https://api.mercadolibre.com/sites/MLA/search?category=${categoria}&offset=${pagina}&limit=${cantidad}")
			
			ColorDetector col = new ColorDetector()
			
			j.results.each {
				
				def jitem = getJson("https://api.mercadolibre.com/items/${it.id}")
				def imagen = jitem?.pictures[0]?.url
			
				if (imagen){
					
					println "Item: "+jitem.id+" - "+imagen
					
					def iInfo = col.detectColors(imagen, true, false)
					
					def resultado = iInfo.detectados
					def resultadoBorde = iInfo.detectadosBorde
					def resultadoCentro = iInfo.detectadosCentro
					
					def result = Producto.findByItemId(it.id)
				
					if (!result){
	
						Producto item = new Producto()
						
						item.itemId = it.id
						item.titulo = it.title
						item.precio = it.price
						item.imagen =  imagen
						item.categoria=categoria
						
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
						
						resultadoBorde.each {
							if (it.nombre=="NE")
								item.negroBorde=it.porcentage
							else if (it.nombre=="BL")
								item.blancoBorde=it.porcentage
							else if (it.nombre=="AZ")
								item.azulBorde=it.porcentage
							else if (it.nombre=="RO")
								item.rojoBorde=it.porcentage
							else if (it.nombre=="VE")
								item.verdeBorde=it.porcentage
							else if (it.nombre=="AM")
								item.amarilloBorde=it.porcentage
							else if (it.nombre=="NA")
								item.naranjaBorde=it.porcentage
							else if (it.nombre=="VI")
								item.violetaBorde=it.porcentage
							else if (it.nombre=="MA")
								item.marronBorde=it.porcentage
							else if (it.nombre=="RS")
								item.rosaBorde=it.porcentage
							else if (it.nombre=="GR")
								item.grisBorde=it.porcentage
							else if (it.nombre=="CE")
								item.celesteBorde=it.porcentage
				
						}
						
						resultadoCentro.each {
							if (it.nombre=="NE")
								item.negroCentro=it.porcentage
							else if (it.nombre=="BL")
								item.blancoCentro=it.porcentage
							else if (it.nombre=="AZ")
								item.azulCentro=it.porcentage
							else if (it.nombre=="RO")
								item.rojoCentro=it.porcentage
							else if (it.nombre=="VE")
								item.verdeCentro=it.porcentage
							else if (it.nombre=="AM")
								item.amarilloCentro=it.porcentage
							else if (it.nombre=="NA")
								item.naranjaCentro=it.porcentage
							else if (it.nombre=="VI")
								item.violetaCentro=it.porcentage
							else if (it.nombre=="MA")
								item.marronCentro=it.porcentage
							else if (it.nombre=="RS")
								item.rosaCentro=it.porcentage
							else if (it.nombre=="GR")
								item.grisCentro=it.porcentage
							else if (it.nombre=="CE")
								item.celesteCentro=it.porcentage
				
						}
						
						if (!item.save(flush:true)){
							println item.errors
							println "Error"
						} else {
							println "Guardado OK"
						}
					} 
					else {
					println "Ya existe: "+it.id
					}
				}
				
				println "-------------------------------"
			}
			
			if (pedido==2)
				pedido=0
			else
				pedido=pedido+1
				
			if (pedido==0)	
				pagina = pagina + 10
				
		} catch (e){
			println "error, preba de nuevo..."
		}
		
		}
		println "Fin"
	}
	
}
