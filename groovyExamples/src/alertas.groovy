import groovy.json.JsonSlurper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//departamentos a chequear
def deptos = 
['Addresses',
'Applications',
'Attributes',
'AuthenticatedUsers',
'Bookmarks',
'Categories',
'Emails',
'Feedback',
'Homes',
'Items',
'Locations',
'Myml',
'Orders',
'Payments',
'Preferences',
'Prices',
'PromotionPacks',
'Questions',
'Shipping',
'Sitemaps',
'Sites',
'Users']

def verifica=true

//verifica si hay que chequear instancias o poner tolerancias
if (!args) {
	println "Verificando equipos down y en maintenance"	
} else {
	//verifica si el parámetro es tolerance, sino no ejecuta
	if (args[0]!="tolerance") {
		println "Parametro inválido"
		System.exit(0)
	} else {
		verifica=false
	}
}

//en el caso de setear tolerancia, pide el usuario y password
def username
def password
if (!verifica) {
	username = System.console().readLine 'Nombre de usuario: '
	password = System.console().readLine 'Password: '
}


def json = new JsonSlurper()

//exluir los pooles con estas palabras
def excluir = ['-atl','test','feedback','history_api','frontend','varnish','shipping','payments','node','seo_dashboard','attributes','merka','myml','fend']

//equipos que hay que setear igual
def procesar = ['feedback-wrapperApi-webserver-write-master-vir','feedback-wrapperApi-webserver-read-master-vir']

def url = 'http://api.melicloud.com/compute/pools?dept='
def urlPool = 'http://api.melicloud.com/compute/pools/'

def badIntances = []

deptos.each { depto ->
	
	//obtiene el token para el depto
	def token = null
	
    //obtiene la lista de pooles
    def pooles = json.parseText((url+depto).toURL().getText())
    
    //para cada pool, pide las instancias
    pooles.each { pool ->
		
		println "Verificando pool: "+pool 
        
        //verifica si hay que excluirla
        def excluye = false
        excluir.each {
            if (pool.contains(it)){
                excluye = true
            }
        }

        //si no hay que exluirla
        if (!excluye){

            //verifica si es un webserver, sino lo descarto (por ejemplo para elastic, rabbits, etc)
            if (pool.contains('webserv')){
    
                def instances = json.parseText((urlPool+pool+'/instances').toURL().getText())

				//verifica las instancias o setea tolerancias        
				if (verifica) {
				
					//verifica cada instancia
					instances.each { instance ->
						
						//obtiene la info
						def instMetadata = json.parseText(('http://api.melicloud.com/compute/instances/'+instance).toURL().getText())
				
						println "instance: "+instMetadata.server+" , status: "+instMetadata.state		
						//si no está en on_duty, la voy guardando
						if (instMetadata.state!="on_duty") {
							badIntances << [instMetadata.pool,instMetadata.server, instMetadata.state]	
						}
						
					}
					
				} else {
				
					//expluye los que sean de menos de dos intancias
					if (instances.size()>2){
	
						//obtiene el token para el depto (si no lo tiene)
						if (!token) {
							token = getToken(depto, username, password)
						}
						
						//setea la tolerancia
						procesa(pool, instances.size(), json, token)
					}
				
				}
            }
        } 
    }
}

if (verifica) {
	println "Listado de malas instancias:"
	println "----------------------------"
	badIntances.each { instance ->
		println instance	
	}
}

def getToken(def depto, def username, def pass) {
	
	def comando1 = '{"auth": {"passwordCredentials": {"username": "'+username+'", "password": '
	def password = '"'+pass+'"'
	def comando2 = '''},"tenantName":'''
	
	def commando = comando1 + password + comando2 + '"'+depto+'"}}'
	//println commando
	
	def result = http('http://essexkeystone.melicloud.com:5000/v2.0/tokens', commando, "GET")
	
	return result.access.token.id	
}

def procesa(def pool, def tamanio, def json, def token){

    //obtiene la metadata
    def metadata = json.parseText(('http://api.melicloud.com/compute/pools/'+pool).toURL().getText())

    //verifica si tiene NOC=1
    if (metadata.noc=="0"){
        println "NO TIENE SETEADO EL NOC: "+pool 
    }
    
    //verifica si tienen la tolerancia
	println metadata.tolerance
    //if (metadata.tolerance==0){
	
		/*
		println "Seteando pool: ${pool} "  
		
		def url = "http://api.melicloud.com/compute/pools/${pool}"
		def content = '{"tolerance":'+porcSugerido(tamanio)+'}'
		def result = http(url, content, "PUT", token)
		println result
		*/
    //}

}

def porcSugerido(def tamanio){

	if (tamanio==2	) return	0
	if (tamanio==3	) return	33
	if (tamanio==4	) return	25
	if (tamanio==5	) return	40
	if (tamanio==6	) return	33
	if (tamanio==7	) return	29
	if (tamanio==8	) return	25
	if (tamanio==9	) return	33
	if (tamanio==10	) return	30
	if (tamanio==11	) return	27
	if (tamanio==12	) return	33
	if (tamanio==13	) return	31
	if (tamanio==14	) return	29
	if (tamanio==15	) return	27
	if (tamanio==16	) return	25
	if (tamanio==17	) return	24
	if (tamanio==18	) return	22
	if (tamanio==19	) return	21
	if (tamanio==20	) return	20
	if (tamanio==21	) return	19
	if (tamanio==22	) return	18
	if (tamanio==23	) return	17
	if (tamanio==24	) return	17
	if (tamanio==25	) return	16
	if (tamanio==26	) return	19
	if (tamanio==27	) return	19
	if (tamanio==28	) return	18
	if (tamanio==29	) return	17
	if (tamanio==30	) return	17
	if (tamanio==31	) return	16
	if (tamanio==32	) return	16
	if (tamanio==33	) return	15
	if (tamanio==34	) return	15
	if (tamanio==35	) return	14
	if (tamanio==36	) return	14
	if (tamanio==37	) return	14
	if (tamanio==38	) return	13
	if (tamanio==39	) return	13
	if (tamanio==40	) return	13
	if (tamanio==41	) return	12
	if (tamanio==42	) return	12
	if (tamanio==43	) return	12
	if (tamanio==44	) return	11
	if (tamanio==45	) return	11
	if (tamanio==46	) return	11
	if (tamanio==47	) return	11
	if (tamanio==48	) return	10
	if (tamanio==49	) return	10
	if (tamanio>=50	) return	10
	
}

def http(def urlToFetch, def content, def method, def token = null) throws Exception {
				
		//get URL content
		URL url = new URL(urlToFetch)
		HttpURLConnection conn = (HttpURLConnection) url.openConnection()

		conn.setDoOutput(true)
		conn.setDoInput(true)
		conn.setRequestMethod(method)
		conn.setRequestProperty("Content-type","application/json")
		conn.setRequestProperty("Content-Length", String.valueOf(content.size()));
		
		//si se pasa el token, se agrega en el header
		if (token) {
			conn.setRequestProperty("x-auth-token",token)
		}
		
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())
		out.write(content)
		out.flush()
		out.close()

		def code = conn.getResponseCode()
		
		def stream
		if (code>=400) {
			stream = conn.getErrorStream()
			println "NO PUEDE OBTENER TOKEN PARA: "+content
			System.exit(0);
		} else {
			stream = conn.getInputStream()
		}
		
		// open the stream and put it into BufferedReader
		
		BufferedReader br = new BufferedReader(new InputStreamReader(stream))

		String inputLine;

		//Guarda el contenido
		StringBuilder contenido= new StringBuilder()

		//genera todo el contenido junto
		while ((inputLine = br.readLine()) != null) {
			contenido.append(inputLine)
		}

		//Devuelve el contendio
		def slurper = new JsonSlurper()
		def result = slurper.parseText(contenido.toString())
		return result
}
