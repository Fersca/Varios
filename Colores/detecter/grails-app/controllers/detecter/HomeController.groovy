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
		
		def colo = color+"Centro"
		
		/*
		def remeras = Producto.findAll("from Producto a where ${colo} > 40 and (blancoBorde > 40 or grisBorde > 40) and categoria='MLA7291' order by ${color} desc",[max:50])
		def carteras = Producto.findAll("from Producto a where ${colo} > 40 and (blancoBorde > 40 or grisBorde > 40) and categoria='MLA5527' order by ${color} desc",[max:50])
		def zapatos = Producto.findAll("from Producto a where ${colo} > 40 and (blancoBorde > 40 or grisBorde > 40) and categoria='MLA3111' order by ${color} desc",[max:50])
		*/

		def remeras
		def carteras
		def zapatos
		
		if (color=="gris"){
			remeras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA7291' order by (blancoBorde-grisBorde+${colo}) desc",[max:50])
			carteras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA5527' order by (blancoBorde-grisBorde+${colo}) desc",[max:50])
			zapatos = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA3111' order by (blancoBorde-grisBorde+${colo}) desc",[max:50])		
		} else if (color=="amarillo"){
			remeras = Producto.findAll("from Producto a where ${colo} > 40 and amarilloBorde <30 and categoria='MLA7291' order by (blancoBorde+grisBorde+${colo}) desc",[max:50])
			carteras = Producto.findAll("from Producto a where ${colo} > 40 and amarilloBorde <30 and categoria='MLA5527' order by (blancoBorde+grisBorde+${colo}) desc",[max:50])
			zapatos = Producto.findAll("from Producto a where ${colo} > 40 and amarilloBorde <30 and categoria='MLA3111' order by (blancoBorde+grisBorde+${colo}) desc",[max:50])
		} else {
			remeras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA7291' order by (blancoBorde+grisBorde+${colo}) desc",[max:50])
			carteras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA5527' order by (blancoBorde+grisBorde+${colo}) desc",[max:50])
			zapatos = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA3111' order by (blancoBorde+grisBorde+${colo}) desc",[max:50])
		}
		
		return [remeras: remeras, carteras:carteras, zapatos:zapatos]
		
	}
	
	def contact = {
		
	}
	
	def analizar = {
		
		ColorDetector col = new ColorDetector()
		def resultado
		try {
			resultado = col.detectColors(params.foto, true, false)
		} catch (e){
			render "Error procesando imagen"
			return
		}
		
		def ancho = resultado.ancho
		def alto = resultado.alto
		
		def primario = cambiarColor(resultado.detectados[0])
		def secundario = cambiarColor(resultado.detectados[1])
		def cantPrimario = resultado.detectados[0].porcentage
		def cantSecundario = resultado.detectados[1].porcentage
		
		def borde = cambiarColor(resultado.detectadosBorde[0])
		def centro = cambiarColor(resultado.detectadosCentro[0])
		def cantBorde = resultado.detectadosBorde[0].porcentage
		def cantCentro = resultado.detectadosCentro[0].porcentage
		
		[cantBorde:cantBorde, cantCentro:cantCentro, borde: borde, centro:centro, ancho:ancho, alto:alto,primario: primario, secundario: secundario, cantPrimario: cantPrimario, cantSecundario: cantSecundario]
	
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
