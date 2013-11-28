import groovy.json.JsonSlurper

//URL a buscar
def url = "https://api.mercadolibre.com/users/6653005"

//Ejecuta tres threads para pedir URLs
def th1 = Thread.start {
    ejecutarUrl(url,"a")
}

def th2 = Thread.start {
    ejecutarUrl(url,"b")
}

def th3 = Thread.start {
    ejecutarUrl(url,"c")
}

def th4 = Thread.start {
    ejecutarUrl(url,"d")
}

//ejecuta indefinidamente el GET a la URL
private ejecutarUrl(url,base){
    int i = 0;
    for (;;){
        def secondUrl = url+"?num="+base+i
        println secondUrl
        def json = new JsonSlurper().parseText(getString(secondUrl))

        if (json.email){
            println "Error!!!"
            println json
            System.exit(0)
        }
        i++
    }
}

//funcion para obtener el String de una URL
private String getString(url){

    //Abre la conexion
    URL u = new URL(url)
    URLConnection c = u.openConnection()
    //Pide en formato json
    c.setRequestProperty("Accept", "application/json");
    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()))

    //Lee el input
    StringBuilder result = new StringBuilder()
    String inputLine
    while ((inputLine = br.readLine()) != null)
        result.append(inputLine)
    br.close()

    //devuelve el resultado final
    return result.toString()

}
