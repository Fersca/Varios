//Closures

square = {it * it}
println square(9)

//this is interesting because you can pass this closure to another functions that are wainting
//for a parameter object that were a closure, like the collect method in arrays

numbers = [1,2,3,4,5]
cuadrados = numbers.collect(square)

println cuadrados

//By default closures take only one parameter called "it" if you want you can name the parameters and have more than one
imprimeNombreEnMapa = {clave, valor ->
	println "clave: "+clave+", valor: "+valor
}

mapaDeNombres = ["fer":"fernando","vale":"valeria"]
mapaDeNombres.each(imprimeNombreEnMapa)  //because each supports a closure with two parameters

//More Closure Examples
//closure can access external variables and can be anonymouse

variable = "Hola: "
mapaDeNombres.each {variable=variable+it}
println variable
//its the same as doing this: mapaDeNombres.each({variable=variable+it})

//Dealing with Files
myFile = new File("prueba.txt")

myFile << "Hola"
myFile << "Chau"

myFile2 = new File("prueba.txt")
myFile.eachLine {println "Contenido: "+it}

//New toInteger() method in strings

numero = "3".toInteger()
println numero * 4

//checks the class type
println numero.class == Integer






















