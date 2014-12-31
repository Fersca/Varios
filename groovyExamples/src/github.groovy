import groovy.json.JsonSlurper
//import java.util.concurrent.atomic.AtomicInteger

//def countTh = new AtomicInteger()

//obtengo los repos desde github
def t = "XXXXXXXXXXXXXXXXXXXXXXXXX"
def token ="&per_page=100&access_token=${t}"
def token1 = "?access_token=${t}"
def github = "https://api.github.com/"
def json = new JsonSlurper()
def repCount = 0
//aca se guarda los repos
def repos = []

def counterRep = 1
//pide todos los repos
println "fetching repos"
def reposJson = getURL(github+"orgs/mercadolibre/repos?page="+counterRep+token, json)
while (reposJson.size()>0){
    reposJson.each {
        repos << it.name
    }
    counterRep++
    println "fetching repos"
    reposJson = getURL(github+"orgs/mercadolibre/repos?page="+counterRep+token, json)
}

//de cada repo, obtiene todos los commits
def rows = []
def committers = [:]

def hoy = new Date()

println "repos: ${repos.size()}"

repos.each { repo ->
	
	//def th = Thread.start {
		
	    def counter = 1
		repCount++
		println "----------------------------------------"
		println "Getting repo ${repCount}/${repos.size()}"
	    //println "feching commits for ${repo} - ${counter}"
	    def commitsJson = getURL(github+"repos/mercadolibre/"+repo+"/commits?page="+counter+token, json)
	    def commit
	    def lineas
	    def name
	    String linea
			
	    while (commitsJson.size()>0){
	        commitsJson.each {
				
				def fechaDeploy = Date.parse("yyyy-MM-dd", it.commit.author.date[0..9])
				
				//calcula la diferencia
				def diff = hoy - fechaDeploy
				
				//obtengo el usuario
				if (diff<366 && it?.author?.login) {
					//println it.author.login
					incrementa(committers, it.author.login)					
				} 
				 			
	        }
	
	        counter++
	        //println "fetching commits for ${repo} - ${counter}"
	        commitsJson = getURL(github+"repos/mercadolibre/"+repo+"/commits?page="+counter+token, json)
	    }
		
		//countTh.incrementAndGet()
	//}
}

/*
while (countTh.get()!=repos.size()) {
	sleep 1000
	println "threads: ${countTh.get()}"
}
*/

println "Imprime result"
println committers.entrySet().sort{ it.value }.reverse()

def synchronized incrementa(def committers, def author) {
	if (committers[author]) {
		committers[author] = committers[author]+1
	} else {
		committers[author] = 1
	}
}

def getURL(def url, def json){
	try {
		sleep 1000
		def texto = new URL(url).getText()
		def respuesta = json.parseText(texto)
		return respuesta 
	} catch (def e) {
		println "Error Obteniendo URL: ${url}, ${e}"
		return []
	}
}
