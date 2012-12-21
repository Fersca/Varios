package detecter

import colordetecter.Producto

class HomeController {

    def index = { 
		
		def color =  params.color
		
		def lista = Producto.findAll("from Producto a where ${color} > 40 order by ${color} desc",[max:72])
		
		return [listado: lista]
		
	}
}
