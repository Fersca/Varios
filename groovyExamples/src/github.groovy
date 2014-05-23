import groovy.json.JsonSlurper

//obtengo los repos desde github
def token ="&per_page=100&access_token=957bb65bea4c680a9da72c5889dba619f8a120fe"
def github = "https://api.github.com/"

def counter = 1

//aca se guarda los repos
def repos = []

//pide todos los depos
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
def users = [:]
repos[1..10].each { repo ->
    counter = 1
    println "feching commits for ${repo}"
    def commitsJson = getURL(github+"repos/mercadolibre/"+repo+"/commits?page="+counter+token)
    while (commitsJson.size()>0 && counter<3){
        commitsJson.each {
            def name = it.commit.author.name
            if (users.get(name)){
                users.put(name,users.get(name)+1)
            } else {
                users.put(name,1)
            }
        }
        counter++
        println "fetching commits for ${repo}"
        reposJson = getURL(github+"repos/mercadolibre/"+repo+"/commits?page="+counter+token)
    }
}

println users.sort { a, b -> b.value <=> a.value }

def getURL(def url){
    return new JsonSlurper().parseText(new URL(url).getText())
}
