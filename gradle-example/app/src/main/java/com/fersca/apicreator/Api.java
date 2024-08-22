package com.fersca.apicreator;

import static com.fersca.apicreator.DynamicJavaRunner.compile;
import static com.fersca.apicreator.DynamicJavaRunner.execute;
import static com.fersca.apicreator.Storage.assureDirectory;
import static com.fersca.apicreator.Storage.createAPIDefinition;
import static com.fersca.apicreator.Storage.deleteFile;
import static com.fersca.apicreator.Storage.readAPIDefinitions;
import static com.fersca.apicreator.Storage.readDomainContent;
import static com.fersca.apicreator.Storage.saveGeneratedCode;
import static com.fersca.apicreator.Storage.saveJsonFile;
import static com.fersca.lib.HttpCli.json;
import static com.fersca.lib.HttpCli.jsonString;
import static com.fersca.lib.Logger.println;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fersca.apicreator.Storage.Directory;
import com.fersca.lib.HttpContext;
import com.fersca.lib.Json;
import com.fersca.lib.Server;
/**
 *
 * @author Fernando.Scasserra
 */
public class Api {
    
    public static void main(String[] args) throws Exception {
                
        Api api = new Api();       
        
        api.startWebserver();        
                     
    }

    private static final Set<String> compiledWorkingClasses = new HashSet<>();
    
    private static Object getCalculatedValue(String api, String field, Map<String, Object> parameters, String description, Json parametersWithTypes) {
        
        //nombre del código
        String className =api + "_"+ field;
        
        /*
        TODO: Ver si se puede detectar al inicio cuáles clases ya están compiladas así no se genera de nuevo el código.
        Creo que no va a quedar otra que ver los archivos dsdsque ya están generados y de alguna manera limpiarlos y listo.
        */
        
        //Si ya está compilado y funciona, cachea el código.
        if (!compiledWorkingClasses.contains(className)){

            if (description==null){
                return "Missing prompt for "+api+"/"+field+" calculated field.";
            }
            
            //genera el codigo fuente
            String sourceCode = generateSourceCode(api, field, parametersWithTypes, description);
            
            //guarda el código generado
            var notOK = saveGeneratedCode(api, field, sourceCode);
            if (notOK!=null) {
            	return notOK;
            }
                       
            // Compilar el código fuente
            boolean isCompiled = compile(sourceCode, className);
            if (!isCompiled) {
                 return "Script compile for "+api+"/"+field+" error.";
            }
            
        }

        String methodName = "getMessage";                        
        //Ejecuta el código ya compilado en la JVM
        Object result = execute(className, methodName, parameters);        
        println("Resultado del método: " + result);
        if (result!=null){
            //compilación y ejecución correcta, cachea la compilación así no se hace de nuevo.
            compiledWorkingClasses.add(className);
            return result;
        } else {
            //Lo saca por si estaba antes ya cacheado, no debería pasar pero quizás por datos ratos en el runtime.
            compiledWorkingClasses.remove(className);
            return "Runtime error for "+api+"/"+field+" error.";
        }
                    
    }

    private static String generateSourceCode(String api, String field, Json parametersWithTypes, String description) {
        
        String sourceCode = 
"""
import java.util.Map;
                            
public class ##CLASS_NAME## {

    public static Object getMessage(Map<String, Object> parametros) {

        ##PARAMETERS##

        Object result;
        //// Begin AI-Code ////
        
        ##AI_CODE##
                                                                                                                                                    
        //// End AI-Code   ////
        return result;
    }

    public static void main(String[] args) {
        System.out.println("");
    }
}                                                    
""";
        
        String parametersCode = generateParametersCode(parametersWithTypes);
        
        String aiCode = generateAiCode(parametersWithTypes, description, api, field);

        sourceCode = sourceCode.replace("##CLASS_NAME##", api+"_"+field);
        sourceCode = sourceCode.replace("##PARAMETERS##", parametersCode);        
        sourceCode = sourceCode.replace("##AI_CODE##", aiCode);                        
                
        return sourceCode;
        
    }

    private static String generateParametersCode(Json parametersWithTypes) {

        String parametersCode = "";
        for (String field : parametersWithTypes.keySet()) {

            //obtiene el tipo del dato del campo
            String valueType = parametersWithTypes.s(field);

            //Según sea el value type lo pone en el json final y calcula los campos calculados real time
            switch (valueType) {
                case "Number" -> parametersCode+="\nDouble "+field+" = (Double) parametros.get(\""+field+"\"); //From code";
                case "String" -> parametersCode+="\nString "+field+" = (String) parametros.get(\""+field+"\"); //From code";
                case "Boolean" -> parametersCode+="\nboolean "+field+" = (Boolean) parametros.get(\""+field+"\"); //From code";
                default -> println("Tipo inválido: "+field + "("+valueType+")");
            }                            
        }                    
        
        return parametersCode;        
        
    }

    private static String generateAiCode(Json parametersWithTypes, String description, String apiName, String apiField) {
        
        String variablesCode = "";
        for (String field : parametersWithTypes.keySet()) {

            //obtiene el tipo del dato del campo
            String valueType = parametersWithTypes.s(field);

            //Según sea el value type lo pone en el json final y calcula los campos calculados real time
            switch (valueType) {
                case "Number" -> variablesCode+="\nDouble "+field+";";
                case "String" -> variablesCode+="\nString "+field+";";
                case "Boolean" -> variablesCode+="\nboolean "+field+";";
                default -> println("Tipo inválido: "+field + "("+valueType+")");
            }                            
        }                    

        String prompt = """
    En java tengo el siguiente código en el metodo de una clase

    public static Object getMessage() {

        ##VARIABLES##
                                                                                                                                                                                                                                                                        
        Object result;

        //##CODE##
                                                                                                                                                                                          
        return result;
                        
    }
    
    Como se puede ver tengo algunas variables (por el momebto no importa su contenido, luego las completaré, las cuales estás disponibles para utilizar.
    También hay disponile un objeto result, el cual se devuelve en el método.
    Tu eres un developers java senior, el cual tiene que devolver el código que pondrías en donde dice ##CODE## para que el siguiente método se comporte como la siguiente descripción:
    "##DESCRIPTION##"                                                                        
    
    Quiero que me des exactamente el código que hay que hacer para que esa descripción sea cierta, no quiero que me des ninguna explicación de como hacerlo, ya que la respuesta que
    me des quierero copiarla y pegarla en el código anterior en reemplazo del fragmento que dice ##CODE## con lo cual tienen que devolverme una cadena de caracteres
    que represente un fragmento de código, el mismo puede tener varias líneas, lo importante es que dejes el cálculo, ya sea un número o un texto, en el objeto result.                             
 
""";
        prompt = prompt.replace("##VARIABLES##", variablesCode);
        prompt = prompt.replace("##DESCRIPTION##", description);
        
        return executePrompt(prompt, apiName, apiField);       
        
    }

    private static String executePrompt(String prompt, String apiName, String apiField) {
        println("-----------PROMPT------------");        
        println(prompt);
        println("-----------END------------");                
        
        String code;
        
        /// *** JUST FOR TESTING PURPOSE *** ///        
        if ("planes".equals(apiName)){            
            switch (apiField){                
                case "test_summary" ->  {
                    //código correcto.
                    code =
                            """
                            result =  name + " is " + age + " years old.";
                            """;
                    return code;
                }
                case "test_nickname" ->  {
                    //variable namee con dos e para que no compile
                    code =
                            """
                            result =  namee + " is " + age + " years old.";
                            """;
                    return code;
                }                
                case "test_color" ->  {
                    //pongo un código que compila pero va a fallar en runtime
                    code =
                            """
                            int num = 3-3;
                            int calculo = 1/num;
                            result =  name + " is " + calculo + " years old.";
                            """;
                    return code;
                }                
                
            }                 
        }                
        /// *** END *** ///
        
        code = 
        """
        result =  name + " is " + age + " years old and very good";
        """;
        
        /*
        String url="http://127.0.0.1:1234/v1/completions";
        
        String jsonString=
                """
                {"prompt":"quiero que me digas cual es la distancia de la tierra a la luna"}                          
                """;
        
        Map<String, Object> jsonResponse;
        
        try {
            jsonResponse = postJson(url, jsonString);
        } catch (Exception ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            println("Error pegándole al llm local");
        }
        */
        
        return code;
                
    }
    
    private static Integer nextIDforDomain(String domain) {
        
        Set<String> keys = DB.keySet();
        Integer maxKey = 0;
        
        for (String key : keys){
            String[] splits = key.split("_");
            String keyDomain = splits[0];
            Integer keyNumber = Integer.valueOf(splits[1]);
            if (keyDomain.equals(domain)){
                if (keyNumber>maxKey){
                    maxKey = keyNumber;
                }
            }
        }
                
        return maxKey+1;
    }

    private static int coutElements(String domain) {
        
        int count = 0;
        var elements = DB.keySet();
        
        for (var element : elements) {            
            var split = element.split("_");            
            if (domain.equals(split[0])) 
                count++;
        }
        
        return count;
    }

    private static String validStructure(Json postedJson) {
        
        //Obligatorio tener el campo structure
        var structure = postedJson.j("structure");
        if (structure==null)
            return "Missing structure field";
       
        //El campo structure tiene que tener un campo domain, un accept, un una lista de campos dentro de field solo con los tipos de datos permitidos
        var domain = structure.s("domain");
        if (domain==null)
            return "Missing domain field";

        var methods = structure.sa("accept");
        //El campo accept tiene que ser un array solo con los metodos permitidos de HTTP        
        for (String method : methods){
            if (!(method.equals("GET")||method.equals("POST")||method.equals("PUT")||method.equals("DELETE")))
                return "Invalid http method: "+method;
        }
                
        var fields = structure.j("fields");

        if (fields==null)
            return "Missing fields field";
        
        var campos = fields.keySet();
        for (var campo : campos){
            try {
                String valor = fields.s(campo);                
                if (!(valor.equals("String")||valor.equals("Number")||valor.equals("Boolean")||valor.equals("Calculated")))
                    return "Invalid Data Type: "+valor;
            } catch (Exception e){
                //agarra la exception si el calor del campo no es string
                return "Fields Data Types should be in String format";
            }
        }
               
        //Los campos dentro de post validations tiene que ser todo de tipo string y con el nombre correspondiente a un nombre de campo en fields
        var postValidations = postedJson.j("post_validations");        
        var postValidationsFields = postValidations.keySet();
        for (var fieldName : postValidationsFields){

            var value = fields.s(fieldName);
            //si es nulo está mal, debería haber campo
            if (value==null) return "Missing "+fieldName+" in fields definition";
                
            try {
                //fuerza la conversion a string para ver si el tipo de dato es correcto
                postValidations.s(fieldName);                
            } catch (Exception e){
                //agarra la exception si el calor del campo no es string
                return "Fields Data Types should be in String format";
            }            
        }
               
        //Lo mismo para online_validations
        var onlineValidations = postedJson.j("get_online_calculations");        
        var onlineValidationsFields = onlineValidations.keySet();
        for (String fieldName : onlineValidationsFields){
            var value = fields.s(fieldName);
            //si es nulo está mal, debería haber campo
            if (value==null) return "Missing "+fieldName+" in fields definition";
                
            try {
                //fuerza la conversion a string para ver si el tipo de dato es correcto
                onlineValidations.s(fieldName);                
            } catch (Exception e){
                //agarra la exception si el calor del campo no es string
                return "Fields Data Types should be in String format";
            }            
        }
               
        return null;
        
    }

    private static boolean existsDomain(String domain) {
        return domains.containsKey(domain);        
    }

    private record TuplaFieldValue(String field, String value){}
    
    private static ArrayList<Json> getAllElements(String domain, Json fields, Json onlineCalculations, ArrayList<TuplaFieldValue> filtros, List<String> selection) {

        //Lista de elementos a devolver
        ArrayList<Json> elementsArray = new ArrayList<>();
        
        //recorre todos los elementos
        for (String element : DB.keySet()){
            
            //si el elemento es del dominio buscado, lo guarda en el array
            String[] split = element.split("_");
            String elementDomain = split[0];
            String key = split[1];
            
            if (elementDomain.equals(domain)){                
                //busca cada elemento y lo agrega al array
                var calculatedJson = getElement(domain, key, fields, onlineCalculations, selection);
                                
                // Chequea si el json tiene que ser agregado a la lista por tener los campos de filtro.
                // Primero verifico si viene el campo de los filtros con algo dentro.
                // Activo un flag de que el json cumple
                // Recorro la lista de filtros
                // Por cada filtro me fijo si el campo está en el json
                // Si el campo está en el json y tiene el valor del filtro, dejo un flag de bien activo
                // Si no está o no cumple con el valor, pongo el flag en negativo y corto el bucle
                // al finalizar el bucle si el flag está en positivo, lo agrego al array de elementos correctos
                                
                if (filtros==null){
                    //agrego el elemebto al array final
                    elementsArray.add(calculatedJson);                                                    
                } else {
                                                                                                  
                    boolean jsonCorrecto = true;
                                        
                    for (TuplaFieldValue tupla : filtros){                        
                        if (calculatedJson.containsKey(tupla.field)){
                                                                                   
                            //voy a buscar el tipo de dato que debería tener el campo al mapa de filtros
                            String valueType = fields.s(tupla.field);

                            //Segun el tipo de dato que sea, hago la conversion y la comparación
                            switch (valueType) {
                                case "Number" -> {
                                    //comparacion de Doubles
                                    Double fieldValue = calculatedJson.d(tupla.field);
                                    if (!fieldValue.equals(Double.valueOf(tupla.value))){
                                        jsonCorrecto = false;
                                        break;                           
                                    }
                                }
                                case "String" -> {
                                    //comparacion de String
                                    String fieldValue = calculatedJson.s(tupla.field);
                                    if (!fieldValue.equals(tupla.value)){
                                        jsonCorrecto = false;
                                        break;
                                    }                                    
                                }
                                case "Boolean" -> {
                                    //comparacion de Doubles
                                    Boolean fieldValue = calculatedJson.b(tupla.field);
                                    if (!fieldValue.equals(Boolean.valueOf(tupla.value))){
                                        jsonCorrecto = false;
                                        break;                           
                                    }                                    
                                }
                                case "Calculated" -> {
                                    //comparacion de String
                                    String fieldValue = calculatedJson.s(tupla.field);
                                    if (!fieldValue.equals(tupla.value)){
                                        jsonCorrecto = false;
                                        break;
                                    }                                                                        
                                }
                                default -> println("Tipo inválido: "+ tupla.field + "("+valueType+")");
                            }                            
                                                                                    
                        } else {
                            jsonCorrecto = false;
                            break;                            
                        }                        
                    }
                    
                    //el json cumple con los filtros
                    if (jsonCorrecto){
                        elementsArray.add(calculatedJson);                                                                            
                    }                    
                }                                
            }            
        }
                        
        return elementsArray;
        
    }

    //Crear un método para ese dominio con el nombre del archivo para cada uno de los métodos.
    private static final Map<String, Object> domains = new HashMap<>();
    
    private void startWebserver() throws IOException, Exception {

        println("Inicia el webserever");
        
        //Leer cada archivo de configuración de la ruta donde están las apis y por cada uno de ellos hacer:
        ArrayList<Json> definitions = readAPIDefinitions();
                
        //Crear el webserver        
        Server.createHttpServer();
                
        //Obtengo el valor del campo domain
        for (Json apiDescription : definitions){
            
            //crea el dominio en el server
            String domain = loadAPIDescription(apiDescription);
            domains.put(domain, apiDescription.getMap());
        }       
        
        //Agrega el controller general para obtener info de los dominios
        Server.addController("/", Api::domainsController, null);     
                        
    }
    
    private static String loadAPIDescription(Json apiDescription) throws IOException{
        
        var apiStructure = apiDescription.j("structure");
        String domain = apiStructure.s("domain");                        

        //Add the request handlers      
        Server.addController("/"+domain, Api::generalController, apiDescription);     

        //carga el contenido de los jsons en memoria
        uploadDBDomain(domain);

        //Check if the directory exists in the DB, it not create it.
        assureDirectory(domain,Directory.DOMAIN);

        //Check if the directory for compiled calculated files, if not create it.
        assureDirectory(domain,Directory.DEFINITION);
        
        return domain;
        
    }
    
    private static void domainsController(HttpContext context) {
        
        //Obtiene el método actual
        String method = context.getRequest().getMethod();
                                     
        switch (method) {
            case "GET" -> 
                //devuelve el json con la lista de los dominios.
                context.write(domains);
            case "POST","PUT" -> {
                //obtener el json
                var postedJson = context.getJsonBody();
                                                
                //valider si tiene la estructura correcta
                String message = validStructure(postedJson);
                if (message!=null){
                    context.badRequest("Invalid Json structure: "+message);
                    return;
                }

                //Obtiene el dominio, para ver si existe y si es post no dejarlo 
                //y si es un put, dejarlo.
                //verificar si el dominio no existe ya
                String domain = postedJson.j("structure").s("domain");
                
                if (existsDomain(domain)){
                	if (method.equals("POST")){
    	    			context.badRequest("Domain already exists");
    	                return;                                    			
                	}                	
                } else {
                	if (method.equals("PUT")){
    	    			context.badRequest("Domain do not exists");
    	                return;                                    			
                	}                	                	
                }
                                
                try {
                    //guardar el archivo del dominio                    
                    createAPIDefinition(domain, jsonString(postedJson.getMap()));
                    
                    //ejecutar la inicializacion del dominio
                    loadAPIDescription(postedJson);
                    
                    //lo guarda en la lista de domains
                    domains.put(domain, postedJson.getMap());
                    
                } catch (IOException ex) {
                    context.badRequest("Error creating API definition.");
                    return;
                }
                                             
                if (method.equals("POST"))
                	context.created(postedJson);
                else 
                	context.ok(postedJson);
            }
            
            default -> context.notSupported();
        }          
       
    }
    
    private static void uploadDBDomain(String domain) {        
    	DB.putAll(readDomainContent(domain));
    }
    
    
    //Base de datos de mentira de jsons
    protected static final HashMap<String, Object> DB = new HashMap<>();
    
    private static Json getElement(String domain, String key, Json fields, Json onlineCalculations, List<String> selection){
        
        //ir a buscar el Json guardado en la base de datos para el ID identificado.
        Object element = DB.get(domain+"_"+key);

        //si existe el elemento en la base de datos
        if (element!=null){               
            //devuelve el json en el request

            var jsonFromDB = json((String)element);
            var finalJson = new Json();

            //va creando el json final en base a lo que viene en los fields               
            for (String field : fields.keySet()) {

                //obtiene el tipo del dato del campo
                String valueType = fields.s(field);
                Object valueFromDB = jsonFromDB.get(field);

                //Según sea el value type lo pone en el json final y calcula los campos calculados real time
                switch (valueType) {
                    case "Number" -> finalJson.put(field, (Double)valueFromDB);
                    case "String" -> finalJson.put(field, (String)valueFromDB);
                    case "Boolean" -> finalJson.put(field, (Boolean)valueFromDB);
                    case "Calculated" -> finalJson.put(field, getCalculatedValue(domain, field, jsonFromDB,onlineCalculations.s(field), fields));
                    default -> println("Tipo inválido: "+field + "("+valueType+")");
                }                            
            }                    

            //si viene con selection, filtro los campos que me pidio
            if (selection!=null){
                //obtiene los campos para filtrar
                for (String field : fields.keySet()){
                    //verifica si no están en la lista de selection
                    if (!selection.contains(field)){
                        finalJson.remove(field);
                    }                    
                }                             
            }
            
            //Devuelve el json generado en base a los campos y los datos de la DB
            return finalJson;
        } else {
            return null;
        }
        
    }
    
    private static void generalController(HttpContext context) {

        
        var arguments = context.getArgs();
        var structure = arguments.j("structure");
        var fields = structure.j("fields");
        
        String domain = structure.s("domain");
        
        var onlineCalculations =  arguments.j("get_online_calculations");
        
        //Obtiene los métodos soportados en esta api                
        var supportedMethods = structure.sa("accept");
        
        //Obtiene el método actual
        String method = context.getRequest().getMethod();
               
        //valida que el método esté soportado por la definición, sino devuelve un 405 (not supported)
        if (!supportedMethods.contains(method)){
            context.notSupported();
            return;
        }
              
        //---> Para el caso del GET:
        
        if ("GET".equals(method)){

            //obtiene el id del
            String key = context.getUrlPath(2);
            Integer idKey=null;
            Json finalJson;
            
            //Verifica si viene con selection
            String selection = context.getParameter("attributes");
            List<String> selectionFields=null;
            if (selection!=null && !selection.equals("")){
                String[] split = selection.split(",");
                selectionFields = Arrays.asList(split);               
            }             
                        
            try {
                //Chequea si la key es un número, si es así, busca un elemento
                idKey= Integer.valueOf(key);            
            } catch(NumberFormatException ex){                
                //no es un número                
            }
            
            //si se busca un ID valido, va a buscar el elemento
            if (idKey!=null){
                //busca el elemento en la base
                finalJson = getElement(domain, idKey.toString(), fields, onlineCalculations, selectionFields);                
                //Si no es null es porque lo encontró
                if (finalJson!=null){                                       
                    context.write(finalJson);                
                } else {
                    //Devuelce not found
                    context.notFound(""+key);
                }                               
            } else if (idKey==null && key!=null && key.length()>0){ //No viene una key numerica, pero hay un comando
                //Verifica si es un comando
                switch (key){
                    case "all" -> {
                        //arma un json con todos los elementos
                        var finalJsonArray = getAllElements(domain, fields, onlineCalculations,null,selectionFields);
                        context.write(finalJsonArray);                
                    }
                    case "search" -> {
                        
                        //crea un array con los filtros que se han pasado para bucar
                        ArrayList<TuplaFieldValue> filtros = new ArrayList<>();           
                                                                        
                        Enumeration<String> enumeration = context.getParameterNames();
                        
                        var fieldNames = fields.keySet();
                        
                        // Recorrer la enumeración y crea el array de tuplas (podría pasarse el mapa entero, pero ya hice esto...)
                        while (enumeration.hasMoreElements()) {
                            String parameter = enumeration.nextElement();
                            
                            //Verifica si los parametros de los filtros son campos validos, sino devuelve directamente un bad request
                            if (!fieldNames.contains(parameter)){
                                context.badRequest("Invalid filter field "+parameter+", it is not present in the json structure");
                                return;
                            }
                            
                            String parameterValue = context.getParameter(parameter);
                            TuplaFieldValue tupla = new TuplaFieldValue(parameter, parameterValue);
                            filtros.add(tupla);
                        }                        
                      
                        //arma un json con todos los elementos que corresponden a los filtros
                        var finalJsonArray = getAllElements(domain, fields, onlineCalculations, filtros,selectionFields);
                        context.write(finalJsonArray);                
                    }                   
                    default -> context.badRequest("Command "+key+" not available");
                     
                }
            } else { //no vienen con ningun comando ni ID
                
                //Verifica si se están pidiendo por ID
                String ids = context.getParameter("ids");
                if (ids!=null && !ids.equals("")){
                    String[] split = ids.split(",");
                    var jsonids = new ArrayList<Json>();
                    
                    //recorro los ids y busco los jsons
                    for (String id : split) {
                        var jsonID = getElement(domain, id, fields, onlineCalculations,selectionFields);
                        if (jsonID!=null)
                            jsonids.add(jsonID);
                    }
                    
                    context.write(jsonids);
                    
                } else {
                    
                    //Se está pidiendo un dominio solo sin nada
                    
                    //Arma una descripción del dominio
                    int elements = coutElements(domain);    

                    Json domainInfo = new Json();
                    domainInfo.put("name", domain);
                    domainInfo.put("elements_count", elements);
                    domainInfo.put("api_definition", domains.get(domain));
                    context.write(domainInfo);                
                    
                }                                                
            }                                  
        } else if ("DELETE".equals(method)){

            Integer key;
            try {
                key = Integer.valueOf(context.getUrlPath(2));
            } catch (NumberFormatException e){
                context.badRequest("Delete should have a numeric ID in the URL");
                return;                                        
            }
                        
            //Buscar el elemento en la DB
            Object element = DB.get(domain+"_"+key);
                        
            //si existe lo elimina                
            if (element!=null){                               
                //Lo elimina del cache
                DB.remove(domain+"_"+key);
                
                //Lo elimina del disco.
                deleteFile(domain, key.toString());

                //Devuelve el json generado en base a los campos y los datos de la DB
                Json j = new Json();
                j.put("message", "Element: "+domain+ " "+key.toString()+ " deleted");
                context.write(j);                
            } else {
                //Si no existe, devuelve un not_fount                
                context.notFound(key.toString());
            }
                    
        } else if ("POST".equals(method) || "PUT".equals(method)){
            
            Integer key;
            
            //Si es un post genera un nuevo ID para ese dominio
            if (method.equals("POST")){

                //Si es un post, no tiene que venir con el ID ni nada luego del dominio, 
                //con lo cual algo en la posicion 2 deberia ser siempre null
                String element = context.getUrlPath(2);                                
                if (element!=null){
                    context.badRequest("Post should not have ID in the URL");
                    return;
                }
                key = nextIDforDomain(domain);                            
            } else { 

                try {
                    key = Integer.valueOf(context.getUrlPath(2));
                } catch (NumberFormatException e){
                    context.badRequest("Put should have a numeric ID in the URL");
                    return;                                        
                }
                                                
                //Verifica que exista ese elemento
                Object element = DB.get(domain+"_"+key);

                //Si no existe, devuelve not found              
                if (element==null){
                    //Devuelce not found
                    context.notFound(""+key);
                    return;
                }
            }
                        
            //Obtiene el contenido del body, lo paso a json.
            var postedJson = context.getJsonBody();
                                                           
            //Se crea el mapa final que se va a devolver
            Json finalJson = new Json();

            //va creando el json final en base a lo que viene en los fields               
            for (String field : fields.keySet()) {

                //ignora si viene el campo ID
                if ("id".equals(field))
                    continue;

                //obtiene el tipo del dato del campo
                String valueType = fields.s(field);
                Object valueFromPost = postedJson.getObject(field);

                //Según sea el value type lo pone en el json final y calcula los campos calculados real time
                switch (valueType) {
                    case "Number" -> finalJson.put(field, (Double)valueFromPost);
                    case "String" -> finalJson.put(field, (String)valueFromPost);
                    case "Boolean" -> finalJson.put(field, (Boolean)valueFromPost);
                    default -> println("Tipo inválido: "+field + "("+valueType+")");
                }                            
            }        
                        
            //guarda la key en el campo "id"
            finalJson.put("id", key);                
            
            //pasa el json a string para guardarlo
            String jsonInString = jsonString(finalJson);
            
            //Guardo el json en el disco.
            saveJsonFile(domain, key.toString(), jsonInString, Directory.DOMAIN);

            //lo guarda en el cache
            DB.put(domain+"_"+key, jsonInString);
            
            //Devuelve el json generado en base a los campos y los datos de la DB
            if (method.equals("POST")){
                context.created(finalJson);  
            } else {
                context.ok(finalJson);  
            }
            
            /*
            TODO: Pensar si tiene sentido hacer un get para que se ejecuten las compilaciones.
            */
                                                    
        }
                
        //---> Para el caso del POST:
        
            //Obtener solo los campos que están en la estructura, se ignoran los otros.

            //Verificar si hay validaciones de esos campos, en esos casos, aplicar los algoritmos de validaciones para esos campos.

            //Si los algoritmos se ejecutaron directamente, guardar el JSON en la base de datos (por ahora un hashmap)
       
    }        
}
