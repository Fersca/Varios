package detecter

import colordetecter.Producto

class HomeController {

    def index = { 
		
		def color =  params.color
		
		if (!color) {
			color = "rosa"
			params.color = "rosa"
		}
		def lista = Producto.findAll("from Producto a where ${color} > 40 order by ${color} desc",[max:100])
		
		return [listado: lista]
		
	}
	
	def contact = {
		
	}
}
