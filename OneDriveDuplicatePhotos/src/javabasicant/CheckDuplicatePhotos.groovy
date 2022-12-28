package javabasicant

import groovy.json.JsonSlurper

println "Welcome to photo checker"

//Crear el mapa donde se van a contener las fotos
HashMap photos = new HashMap<String, Photo>()

//buscar en todo el directorio actual el nombre de los archivos
//obtener el nombre, la ubicación y el tamaño
File dh = new File(args[0])
println "Analyzing Path = " + dh.getAbsolutePath()


dh.eachFileRecurse { File file ->                                            
    
    //chequea si es un archivo
    if (file.isFile()){        
        //crea el objeto foto
        Photo p = new Photo()
        p.name = file.getName()        
        p.path = file.getAbsolutePath()
        p.size = file.size()   
        
        //chequea si la foto está en el hashmap
        if (!photos.get(p.name)) {
            //Agrega al foto en el hashmap
            //println "Agrega la foto: $p.name"
            photos.put(p.name, p)
        } else {

            //println "Estaba ya en el hashmap: $p.name"
            
            //obtengo el objeto en el hasmap
            Photo pAnterior = photos.get(p.name)
            //si no tiene creado el objeto Arraylist lo crea (por ser la primera vez)
            if (!pAnterior.repetidos){
               pAnterior.repetidos = new ArrayList<Photo>()
            } 

            //agrega el elemento a la lista de repetidos
            pAnterior.repetidos.add(p)

        }
                     
    } else {
        //println "No es File: $file.name"
    }
   
}

//guarda el contador de bytes repetidos
Long bytesRepetidos = 0L
Long totalBytes = 0L
Long biggest = 0L
String biggestImg = ""
Integer cantidad = 0

println "Lista de Fotos repetidas:"
println "----------------------------------"


//listar los repetidos
photos.each { key, val ->
    
    
    totalBytes = totalBytes + val.size
    
    //chequea si las fotos tienen un repetido  
    if (val.repetidos){
        
        //imprime los repetidos
        val.repetidos.each{ 
            
            if (it.size == val.size){
                println "Path = $it.path | Size: $it.size"             
                bytesRepetidos = bytesRepetidos + it.size                
                //imprime la original
                println "Path = $val.path | Size: $val.size"                     

                cantidad++
                
                if (biggest<val.size){
                    biggest = val.size
                    biggestImg = val.name
                }
                
                
            } 
            
        }
                
    }
}

println "----------------------------------"


Integer mbTotales = totalBytes/1024/1024/1024
Integer mbLibres = bytesRepetidos/1024/1024/1024

println "Cantidad de repetidas: $cantidad"
println "GB totales: $mbTotales"
println "GB a Liberar: $mbLibres"
Integer repe = (mbLibres/mbTotales)*100
println "% Repetidos: $repe%"

def biggestGB = biggest/1024/1024/1024
println "Biggest: $biggestImg | size: $biggestGB GB"

//se podría crear un archivo html para ver las diferencias.

class Photo {   
    Long size
    String name
    String path
    ArrayList repetidos
}


//................................Funcitons................................

def getJson(String apiString){

    //Object to process the Json
    def json = new JsonSlurper()

    //Get the Json from an API
    URL url = new URL(apiString)
    def userJson = json.parse(url)
    assert userJson instanceof Map
    
    //Print the json in the screen
    //println(userJson)
    
    return userJson
    
}



