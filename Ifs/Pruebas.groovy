calcular1(14);
calcular2(14);
calcular3(14);
calcular4(14);
calcular5(14);
calcular6(14);
calcular7(14);
calcular8(14);
calcular9(14);
calcular10(14);

def calcular1(edad){
	
	if (edad<6){
		println "Jardin"
	} else {
		if (edad<13) {
			println "Primaria"
		} else {
			println "Secundaria"
		}
	}
	
}

def calcular2(edad){
	
	if (edad<6){
		println "Jardin"
	}
	
	if (edad>=6 && edad<13) {
		println "Primaria"
	}
	
	if (edad>=13){
		println "Secundaria"
	}
	
}


def calcular3(edad){
	
	if (edad<6){
		println "Jardin"
		return
	}
	
	if (edad<13) {
		println "Primaria"
		return
	}
			
	println "Secundaria"
	
}

def calcular4(edad){
	
	jardin1(edad);
	primaria1(edad);
	secundaria1(edad);
	
}

def jardin1(edad) {
	if (edad<6){
		println "Jardin"
	}
}

def primaria1(edad) {
	if (edad>=6 && edad<13) {
		println "Primaria"
	}
}

def secundaria1(edad) {
	if (edad>=13){
		println "Secundaria"
	}
}

def calcular5(edad){
	if (jardin(edad) || primaria(edad) || secundaria()){}
}

def jardin(edad) {
	
	if (edad<6){
		println("Jardin")
		return true
	}
	return false
}

def primaria(edad) {
	
	return edad<13?println("Primaria"):Boolean.FALSE;

}

def secundaria() {
	println "Secundaria"
	return true
}

def calcular6(edad){
	
	def e1 = new Jardin();
	def e2 = new Primaria();
	def e3 = new Secundaria();
	
	def estrategias = []
	estrategias << e1
	estrategias << e2
	estrategias << e3
	
	for (def e:estrategias){
		if (e.condicionOK(edad)) 
			e.ejecutar()
	}
	
}

def calcular7(edad){
	
	for (def e:cargarEstrategias()){
		if (e.condicionOK(edad)) {
			e.ejecutar() 
			return
		}
	}
	
}

def calcular8(edad){
	
	for (def e:cargarEstrategias()) 
		e.run(edad)

}

def calcular9(edad){
	
	cargarEstrategias().each {it.run(edad)}

}

def calcular10(edad){
	
	cargarEstrategias().find {it.condicionOK(edad)}.ejecutar()

}


def cargarEstrategias(){
	def e1 = new Jardin();
	def e2 = new Primaria();
	def e3 = new Secundaria();
	
	def estrategias = []
	estrategias << e1
	estrategias << e2
	estrategias << e3
	return estrategias
}

abstract class Estrategia {
	def run(edad){
		return condicionOK(edad)?ejecutar():Boolean.FALSE;
	}
}

class Jardin {
	def condicionOK(edad){
		return edad<6
	}
	def ejecutar(){
		println "Jardin"
	}
}

class Primaria extends Estrategia{
	def condicionOK(edad){
		return edad<13
	}
	def ejecutar(){
		println "Secundaria"
	}
}

class Secundaria extends Estrategia {
	def condicionOK(edad){
		return edad>=13
	}
	def ejecutar(){
		println "Secundaria"
	}
}
