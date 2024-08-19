package com.fersca.apicreator;

import static com.fersca.apicreator.DynamicJavaRunner.compile;
import static com.fersca.apicreator.DynamicJavaRunner.execute;
import static com.fersca.lib.HttpCli.json;
import static com.fersca.lib.HttpCli.jsonString;
import com.fersca.lib.HttpContext;
import static com.fersca.lib.Logger.println;
import com.fersca.lib.Server;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author fersc
 */
public class Api {
    
    public static void main(String[] args) throws IOException, Exception {
                
        Api api = new Api();
        
        api.startWebserver();        
                     
    }

    private static final Set<String> compiledWorkingClasses = new HashSet<>();
    
    private static Object getCalculatedValue(String api, String field, Map<String, Object> parameters, String description, Map<String, Object> parametersWithTypes) {
        
        //nombre del código
        String className =api + "_"+ field;
        
        /*
        TODO: Ver si se puede detectar al inicio cuáles clases ya están compiladas así no se genera de nuevo el código.
        Creo que no va a quedar otra que ver los archivos que ya están generados y de alguna manera limpiarlos y listo.
        */
        
        //Si ya está compilado y funciona, cachea el código.
        if (!compiledWorkingClasses.contains(className)){

            if (description==null){
                return "Missing prompt for "+api+"/"+field+" calculated field.";
            }
            
            //genera el codigo fuente
            String sourceCode = generateSourceCode(api, field, parametersWithTypes, description);
            
            //guarda el código generado
            File scriptFile = new File(rootPath+"/apis_calculated_fields_code/"+api+"/"+field+"_generated.sc");
            Path filePath = Paths.get(scriptFile.getAbsolutePath());
            try {
                Files.writeString(filePath, sourceCode);
            } catch (IOException ex) {
                Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
                return "Error storing code for "+api+"/"+field;
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
        System.out.println("Resultado del método: " + result);
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

    protected static final String rootPath;

    static {
        rootPath = System.getProperty("user.dir")+"/";
    }
    private static String generateSourceCode(String api, String field, Map<String, Object> parametersWithTypes, String description) {
        
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

        sourceCode = sourceCode.replaceAll("##CLASS_NAME##", api+"_"+field);
        sourceCode = sourceCode.replaceAll("##PARAMETERS##", parametersCode);        
        sourceCode = sourceCode.replaceAll("##AI_CODE##", aiCode);                        
                
        return sourceCode;
        
    }

    private static String generateParametersCode(Map<String, Object> parametersWithTypes) {

        String parametersCode = "";
        for (String field : parametersWithTypes.keySet()) {

            //obtiene el tipo del dato del campo
            String valueType = (String)parametersWithTypes.get(field);

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

    private static String generateAiCode(Map<String, Object> parametersWithTypes, String description, String apiName, String apiField) {
        
        String variablesCode = "";
        for (String field : parametersWithTypes.keySet()) {

            //obtiene el tipo del dato del campo
            String valueType = (String)parametersWithTypes.get(field);

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
        prompt = prompt.replaceAll("##VARIABLES##", variablesCode);
        prompt = prompt.replaceAll("##DESCRIPTION##", description);
        
        String code = executePrompt(prompt, apiName, apiField);
        
        return code;
        
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

    private static void deleteFile(String domain, String key) {

        Path filePath = Paths.get(rootPath+"db/"+domain+"/"+key+".json");
        try {
            Files.delete(filePath);
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected static void saveJsonFile(String domain, String key, String jsonString, Directory type) {
        
        Path filePath;
        
        if (type==Directory.DOMAIN){
            filePath = Paths.get(rootPath+"db/"+domain+"/"+key+".json");
        } else {
            filePath = Paths.get(rootPath+"apis/"+domain+".api.def");
        }
        
        try {
            Files.write(filePath, jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
                
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

    private record TuplaFieldValue(String field, String value){}
    
    private static ArrayList<Map<String,Object>> getAllElements(String domain, Map<String, Object> fields, Map<String, Object> onlineCalculations, ArrayList<TuplaFieldValue> filtros) {

        //Lista de elementos a devolver
        ArrayList<Map<String,Object>> elementsArray = new ArrayList<>();
        
        //recorre todos los elementos
        for (String element : DB.keySet()){
            
            //si el elemento es del dominio buscado, lo guarda en el array
            String[] split = element.split("_");
            String elementDomain = split[0];
            String key = split[1];
            
            if (elementDomain.equals(domain)){                
                //busca cada elemento y lo agrega al array
                var calculatedJson = getElement(domain, key, fields, onlineCalculations);
                                
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
                            String valueType = (String)fields.get(tupla.field);

                            //Segun el tipo de dato que sea, hago la conversion y la comparación
                            switch (valueType) {
                                case "Number" -> {
                                    //comparacion de Doubles
                                    Double fieldValue = (Double)calculatedJson.get(tupla.field);
                                    if (!fieldValue.equals(Double.parseDouble(tupla.value))){
                                        jsonCorrecto = false;
                                        break;                           
                                    }
                                }
                                case "String" -> {
                                    //comparacion de String
                                    String fieldValue = (String)calculatedJson.get(tupla.field);
                                    if (!fieldValue.equals(tupla.value)){
                                        jsonCorrecto = false;
                                        break;
                                    }                                    
                                }
                                case "Boolean" -> {
                                    //comparacion de Doubles
                                    Boolean fieldValue = (Boolean)calculatedJson.get(tupla.field);
                                    if (!fieldValue.equals(Boolean.parseBoolean(tupla.value))){
                                        jsonCorrecto = false;
                                        break;                           
                                    }                                    
                                }
                                case "Calculated" -> {
                                    //comparacion de String
                                    String fieldValue = (String)calculatedJson.get(tupla.field);
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
    
    private void startWebserver() throws IOException, Exception {

        println("Inicia el webserever");
        
        //Leer cada archivo de configuración de la ruta donde están las apis y por cada uno de ellos hacer:
        ArrayList<Map<String, Object>> files = readAPIDefinitionFiles(rootPath+"apis");
                
        //Crear el webserver        
        Server.createHttpServer();

        //Crear un método para ese dominio con el nombre del archivo para cada uno de los métodos.
        
        //Obtengo el valor del campo domain
        for (Map<String, Object> apiDescription : files){
            
            @SuppressWarnings("unchecked")
            var apiStructure = (Map<String, Object>) apiDescription.get("structure");
            String domain = apiStructure.get("domain").toString();                        

            //Add the request handlers      
            Server.addController("/"+domain, Api::generalController, apiDescription);     
             
            //carga el contenido de los jsons en memoria
            uploadDBDomain(domain);
            
            //Check if the directory exists in the DB, it not create it.
            assureDirectory(domain,Directory.DOMAIN);
            
            //Check if the directory for compiled calculated files, if not create it.
            assureDirectory(domain,Directory.DEFINITION);
            
        }       
                
    }
    
    private void uploadDBDomain(String domain) {
        
        // Especifica el directorio que deseas leer
        File directory = new File(rootPath+"db/"+domain);
        
        //genera la lista de domains                            
        File[] files = directory.listFiles();
        // Itera sobre los archivos y directorios encontrados
        if (files != null) {
            for (File file : files) {
                String keyName = domain+"_"+file.getName().split("\\.")[0];
                Path filePath = Paths.get(file.getAbsolutePath());
                String content;

                //si hay algun error no lo guarda en la DB.
                try {
                    content = Files.readString(filePath);
                    DB.put(keyName, content);
                } catch (IOException ex) {
                    Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }                                                   
        
    }
    
    
    //Base de datos de mentira de jsons
    protected static final HashMap<String, Object> DB = new HashMap<>();
    
    private static Map<String, Object> getElement(String domain, String key, Map<String, Object> fields, Map<String, Object> onlineCalculations){
        
        //ir a buscar el Json guardado en la base de datos para el ID identificado.
        Object element = DB.get(domain+"_"+key);

        //si existe el elemento en la base de datos
        if (element!=null){               
            //devuelve el json en el request

            var jsonFromDB = json((String)element);
            Map<String, Object> finalJson = new HashMap<>();

            //va creando el json final en base a lo que viene en los fields               
            for (String field : fields.keySet()) {

                //obtiene el tipo del dato del campo
                String valueType = (String)fields.get(field);
                Object valueFromDB = jsonFromDB.get(field);

                //Según sea el value type lo pone en el json final y calcula los campos calculados real time
                switch (valueType) {
                    case "Number" -> finalJson.put(field, (Double)valueFromDB);
                    case "String" -> finalJson.put(field, (String)valueFromDB);
                    case "Boolean" -> finalJson.put(field, (Boolean)valueFromDB);
                    case "Calculated" -> finalJson.put(field, getCalculatedValue(domain, field, jsonFromDB,(String)onlineCalculations.get(field), fields));
                    default -> println("Tipo inválido: "+field + "("+valueType+")");
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
        @SuppressWarnings("unchecked")
        var structure = (Map<String, Object>) arguments.get("structure");
        @SuppressWarnings("unchecked")
        var fields = (Map<String, Object>)structure.get("fields");
        String domain = (String)structure.get("domain");
        
        @SuppressWarnings("unchecked")
        var onlineCalculations = (Map<String, Object>) arguments.get("get_online_calculations");
        
        @SuppressWarnings("unchecked")
        //Obtiene los métodos soportados en esta api                
        ArrayList<String> supportedMethods = (ArrayList<String>) structure.get("accept");
        
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
            Map<String, Object> finalJson=null;
            
            try {
                //Chequea si la key es un número, si es así, busca un elemento
                idKey= Integer.valueOf(key);            
            } catch(NumberFormatException ex){                
                //no es un número                
            }
            
            //si se busca un ID valido, va a buscar el elemento
            if (idKey!=null){
                //busca el elemento en la base
                finalJson = getElement(domain, idKey.toString(), fields, onlineCalculations);                
                //Si no es null es porque lo encontró
                if (finalJson!=null){
                    
                    finalJson.put("fer", "no se. ...?");
                    
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
                        ArrayList<Map<String, Object>> finalJsonArray = getAllElements(domain, fields, onlineCalculations,null);
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
                        ArrayList<Map<String, Object>> finalJsonArray = getAllElements(domain, fields, onlineCalculations, filtros);
                        context.write(finalJsonArray);                
                    }                   
                    default -> {
                        context.badRequest("Command "+key+" not available");
                    } 
                }
            } else { //no vienen con ningun comando ni ID
                
                //Arma una descripción del dominio
                int elements = coutElements(domain);    
                
                Map<String, Object> domainInfo = new HashMap<String, Object>();
                domainInfo.put("name", domain);
                domainInfo.put("elements_count", elements);
                context.write(domainInfo);                
                                
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
                context.write("Element: "+domain+ " "+key.toString()+ " deleted");                
            } else {
                //Si no existe, devuelve un not_fount                
                context.notFound(key.toString());
            }
                    
        } else if ("POST".equals(method) || "PUT".equals(method)){
            
            Integer key=null;
            
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
            var postedJson = (Map<String, Object>)context.getJsonBody();
                                                           
            //Se crea el mapa final que se va a devolver
            Map<String, Object> finalJson = new HashMap<>();

            //va creando el json final en base a lo que viene en los fields               
            for (String field : fields.keySet()) {

                //ignora si viene el campo ID
                if ("id".equals(field))
                    continue;

                //obtiene el tipo del dato del campo
                String valueType = (String)fields.get(field);
                Object valueFromPost = postedJson.get(field);

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
       
    };
    
    
    protected ArrayList<Map<String, Object>> readAPIDefinitionFiles(String path) throws IOException {
        
        // Especifica el directorio que deseas leer
        File directory = new File(path);
        
        ArrayList<Map<String, Object>> fileContents = new ArrayList<>();

        // Verifica que el objeto File es un directorio
        if (directory.isDirectory()) {
            // Obtiene la lista de archivos en el directorio
            File[] files = directory.listFiles();

            // Itera sobre los archivos y directorios encontrados
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) continue;
                    // Lee el archivo y pasa el contenido a Json
                    Path filePath = Paths.get(file.getAbsolutePath());
                    String content = Files.readString(filePath);
                    
                    try {
                        var jsonContent = json(content);
                        // Guarda el nombre del archivo y el contenido en el mapa                    
                        fileContents.add(jsonContent);                        
                    } catch (Exception e){
                        println("Error parsing json: ---->");
                        println(content);
                        e.printStackTrace();
                        continue;
                    }             
                                        
                }
            } else {
                System.out.println("El directorio está vacío o no se pudo leer.");
            }
        } else {
            System.out.println("La ruta especificada no es un directorio.");
        }
        return fileContents;
    }

    protected static enum Directory {
        DOMAIN,
        DEFINITION
    }

    protected static void createAPIDefinition(String domain, String jsonString) throws IOException{
        
        //crea el directorio donde estaran las compilaciones de los campos de la api
        assureDirectory(domain, Directory.DEFINITION);
        
        //guarda el file con la definición de la api
        saveJsonFile(domain, domain, jsonString, Directory.DEFINITION);
    }
    
    protected static void assureDirectory(String name, Directory type) throws IOException {
        
        String path;
        
        if (type==Directory.DOMAIN){
            path = rootPath+"db/"+name;
        } else {
            path = rootPath+"apis_calculated_fields_code/"+name;
        }
        
        // Especifica el directorio que deseas leer
        File directory = new File(path);
        
        // Verifica que el objeto File es un directorio
        if (directory.exists() && directory.isDirectory()) {
            return;            
        }
        
        //Crea el directorio si no existe
        Path filePath = Paths.get(directory.getAbsolutePath());        
        Files.createDirectories(filePath);
        
    }
}
