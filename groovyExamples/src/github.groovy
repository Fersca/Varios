import groovy.json.JsonSlurper

//obtengo los repos desde github
def token ="&per_page=100&access_token=XXXXXXXXXXXXXXXXXXXX"
def token1 = "?access_token=XXXXXXXXXXXXXXXXXXXX"
def github = "https://api.github.com/"

def counter = 1

//aca se guarda los repos
def repos = []

//pide todos los repos
println "fetching repos"
def reposJson = getURL(github+"orgs/mercadolibre/repos?page="+counter+token)
while (reposJson.size()>0 && counter<3){
    reposJson.each {
        repos << it.name
    }
    counter++
    println "fetching repos"
    reposJson = getURL(github+"orgs/mercadolibre/repos?page="+counter+token)
}

//de cada repo, obtiene todos los commits
def rows = []

repos[1..10].each { repo ->
    counter = 1
    println "feching commits for ${repo}"
    def commitsJson = getURL(github+"repos/mercadolibre/"+repo+"/commits?page="+counter+token)
    def commit
    def lineas
    def name
    String linea
    while (commitsJson.size()>0 && counter<3){
        commitsJson.each {

            println "get commit ${it.sha}"
            commit = getURL(it.url+token1)
            println " url" 
            lineas = commit.stats.total
            name = commit.author.login
            
            linea = name+";"+repo+";"+lineas
            println linea
            rows << linea

        }

        counter++
        println "fetching commits for ${repo}"
        reposJson = getURL(github+"repos/mercadolibre/"+repo+"/commits?page="+counter+token)
    }
}

println rows.sort //{ a, b -> b.value <=> a.value }

def getURL(def url){
    println url
    return new JsonSlurper().parseText(new URL(url).getText())
}
