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
		
		def colo = color+"Producto"
		
		/*
		def remeras = Producto.findAll("from Producto a where ${colo} > 40 and (blancoBorde > 40 or grisBorde > 40) and categoria='MLA7291' order by ${color} desc",[max:50])
		def carteras = Producto.findAll("from Producto a where ${colo} > 40 and (blancoBorde > 40 or grisBorde > 40) and categoria='MLA5527' order by ${color} desc",[max:50])
		def zapatos = Producto.findAll("from Producto a where ${colo} > 40 and (blancoBorde > 40 or grisBorde > 40) and categoria='MLA3111' order by ${color} desc",[max:50])
		*/

		def remeras
		def carteras
		def zapatos
		
		/*
		if (color=="blanco"){
			def cant = 25
			def cant2 = 10
			def cant3 = 20
			remeras = Producto.findAll("from Producto a where blancoCentro > ${cant3} and blancoBorde < ${cant2} and grisBorde < ${cant2} and categoria='MLA7291' and (negroCentro<${cant} and rojoCentro<${cant} and azulCentro<${cant} and verdeCentro <${cant} and amarilloCentro<${cant} and grisCentro<${cant} and violetaCentro<${cant} and rosaCentro<${cant} and marronCentro<${cant} and naranjaCentro<${cant} and celesteCentro<${cant}) order by (blancoCentro) desc",[max:70])
			carteras = Producto.findAll("from Producto a where blancoCentro > ${cant3} and blancoBorde < ${cant2} and grisBorde < ${cant2} and categoria='MLA5527' and (negroCentro<${cant} and rojoCentro<${cant} and azulCentro<${cant} and verdeCentro <${cant} and amarilloCentro<${cant} and grisCentro<${cant} and violetaCentro<${cant} and rosaCentro<${cant} and marronCentro<${cant} and naranjaCentro<${cant} and celesteCentro<${cant}) order by (blancoCentro) desc",[max:70])
			zapatos = Producto.findAll("from Producto a where blancoCentro > ${cant3} and blancoBorde < ${cant2} and grisBorde < ${cant2} and categoria='MLA3111' and (negroCentro<${cant} and rojoCentro<${cant} and azulCentro<${cant} and verdeCentro <${cant} and amarilloCentro<${cant} and grisCentro<${cant} and violetaCentro<${cant} and rosaCentro<${cant} and marronCentro<${cant} and naranjaCentro<${cant} and celesteCentro<${cant}) order by (blancoCentro) desc",[max:70])				
		} else   */
		
		/*
		 if (color=="gris"){
			remeras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA7291' order by (blancoBorde-grisBorde+${colo}) desc",[max:70])
			carteras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA5527' order by (blancoBorde-grisBorde+${colo}) desc",[max:70])
			zapatos = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA3111' order by (blancoBorde-grisBorde+${colo}) desc",[max:70])		
		} else if (color=="amarillo"){
			remeras = Producto.findAll("from Producto a where ${colo} > 40 and amarilloBorde <30 and categoria='MLA7291' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
			carteras = Producto.findAll("from Producto a where ${colo} > 40 and amarilloBorde <30 and categoria='MLA5527' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
			zapatos = Producto.findAll("from Producto a where ${colo} > 40 and amarilloBorde <30 and categoria='MLA3111' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
		} else {
			remeras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA7291' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
			carteras = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA5527' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
			zapatos = Producto.findAll("from Producto a where ${colo} > 30  and categoria='MLA3111' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
		}
*/
	
		if (color=="blanco"){
			remeras = Producto.findAll("from Producto a where ${colo} > 50  and categoria='MLA7291' order by (negroBorde+grisBorde+${colo}) desc",[max:70])
			carteras = Producto.findAll("from Producto a where ${colo} > 50  and categoria='MLA5527' order by (negroBorde+grisBorde+${colo}) desc",[max:70])
			zapatos = Producto.findAll("from Producto a where ${colo} > 50  and categoria='MLA3111' order by (negroBorde+grisBorde+${colo}) desc",[max:70])
		} else {			
			remeras = Producto.findAll("from Producto a where ${colo} > 50  and categoria='MLA7291' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
			carteras = Producto.findAll("from Producto a where ${colo} > 50  and categoria='MLA5527' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
			zapatos = Producto.findAll("from Producto a where ${colo} > 50  and categoria='MLA3111' order by (blancoBorde+grisBorde+${colo}) desc",[max:70])
		}
				
		def rem
		def remOtras
		if (remeras.size()>7){
			rem = remeras[0..6]
			remOtras = remeras[7..remeras.size()-1]
		} else {
			rem = remeras
			remOtras = []
		}

		def car
		def carOtras
		if (carteras.size()>7){
			car = carteras[0..6]
			carOtras = carteras[7..carteras.size()-1]
		} else {
			car = carteras
			carOtras = []
		}

		def zap
		def zapOtras
		if (zapatos.size()>7){
			zap = zapatos[0..6]
			zapOtras = zapatos[7..zapatos.size()-1]
		} else {
			zap = zapatos
			zapOtras = []
		}
				
		return [remeras: rem,otrasRemeras: remOtras, carteras:car, otrasCarteras: carOtras, zapatos:zap, otrosZapatos:zapOtras]
		
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

		def borde1 = cambiarColor(resultado.detectadosBorde[1])
		def centro1 = cambiarColor(resultado.detectadosCentro[1])
		def cantBorde1 = resultado.detectadosBorde[1]?.porcentage
		def cantCentro1 = resultado.detectadosCentro[1]?.porcentage

		def cambia='No'
		if (borde==centro && cantBorde>cantCentro){
			cambia=centro1
		}
				
		def produ = cambiarColor(resultado.detectadosProducto[0])
		def cantProdu = resultado.detectadosProducto[0]?.porcentage
		
		if (cantBorde<=35){
			borde = "multicolor";
		}
		
		if (cantCentro<=35){
			produ="multicolor";
		}
		 
		[produ: produ, cantProdu: cantProdu, cambia: cambia, cantBorde1:cantBorde1, cantCentro1:cantCentro1, borde1: borde1, centro1:centro1,cantBorde:cantBorde, cantCentro:cantCentro, borde: borde, centro:centro, ancho:ancho, alto:alto,primario: primario, secundario: secundario, cantPrimario: cantPrimario, cantSecundario: cantSecundario]
	
	}
	
	def cambiarColor(def it){
		
		if (!it) return "Nan"
		
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
