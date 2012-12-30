package detecter

import colordetecter.Producto
import colores.ColorDetector

class HomeController {

    def index = { 
		
		def color =  params.color
		
		if (!color) {
			color = "rosa"
			params.color = "rosa"
		}
		def lista = Producto.findAll("from Producto a where ${color} > 40 order by ${color} desc",[max:50])
		
		return [listado: lista]
		
	}
	
	def contact = {
		
	}
	
	def analizar = {
		
		ColorDetector col = new ColorDetector()
		def resultado = col.detectColors(params.foto, true, false)
		
		def ancho = resultado[0]
		def alto = resultado[1]
		def primario = cambiarColor(resultado[2])
		def secundario = cambiarColor(resultado[3])
		def cantPrimario = resultado[2].porcentage
		def cantSecundario = resultado[3].porcentage
		
		[ancho:ancho, alto:alto,primario: primario, secundario: secundario, cantPrimario: cantPrimario, cantSecundario: cantSecundario]
	
	}
	
	def cambiarColor(def it){
		
		if (it.nombre=="NE")
			return "black"
		else if (it.nombre=="BL")
			return "white"
		else if (it.nombre=="AZ")
			return "blue"
		else if (it.nombre=="RO")
			return "red"
		else if (it.nombre=="VE")
			return "green"
		else if (it.nombre=="AM")
			return "yellow"
		else if (it.nombre=="NA")
			return "orange"
		else if (it.nombre=="VI")
			return "violet"
		else if (it.nombre=="MA")
			return "brown"
		else if (it.nombre=="RS")
			return "pink"
		else if (it.nombre=="GR")
			return "grey"
		else if (it.nombre=="CE")
			return "skyblue"
			
		}
	
}
