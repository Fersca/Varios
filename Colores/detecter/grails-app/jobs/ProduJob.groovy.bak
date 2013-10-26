import colordetecter.Producto

class ProduJob {
	
	def concurrent = false
	
	static triggers = {
		simple name:'simpleTrigger2', startDelay:1, repeatInterval: 0, repeatCount: 0
	}
		
	def execute() {

		println "Inicia calculo"
		def productos = Producto.list()
		
		println "Cantidad: ${productos.size()}"
		
		def cont=0
		
		productos.each { produ -> 
			
			def maxBordeCant = 0
			def maxBorde = ""

			def colores = ["blanco","negro","rojo","azul","marron","celeste","amarillo","verde","naranja","violeta","gris","rosa"]
	
			colores.each {color ->
				if (produ.(color+"Borde")>maxBordeCant){
					maxBordeCant = produ.(color+"Borde")
					maxBorde = color
				}
			}			
			
			def queda = 100 - produ.(maxBorde+"Centro")
			
			//el 90% del centro es del mismo color del borde, debe ser un objeto grande, no sacar este color
			if (queda<=10){

				//Se copian los colores del centro al del producto
				colores.each {color ->
						//Le descuento el total al color que voy a sacar
						produ.(color+"Producto") = produ.(color+"Centro")
				}
				produ.estado='full'					
			} else {

				colores.each {color ->
					if (color == maxBorde){
						//Le descuento el total al color que voy a sacar
						produ.(color+"Producto") = 0
					} else {
						//recalcula el nuevo porcentaje
						produ.(color+"Producto") = (produ.(color+"Centro")*100) / queda
					}
				}

			}
			
			
			println "maxBorde: ${maxBorde} - cant: {maxBordeCant} - queda: ${queda}"

			produ.save(flush:true)
			
			println "Fin ${produ.titulo} - ${cont}"
			cont++
			
		}
		

	}
	
}
