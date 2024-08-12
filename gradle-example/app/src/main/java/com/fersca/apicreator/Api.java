package com.fersca.apicreator;

import static com.fersca.apicreator.DynamicJavaRunner.compile;
import static com.fersca.apicreator.DynamicJavaRunner.execute;
import static com.fersca.lib.Logger.println;
import static com.fersca.lib.HttpCli.json;
import static com.fersca.lib.HttpCli.jsonString;
import static com.fersca.lib.HttpCli.postJson;
import com.fersca.lib.HttpContext;
import static com.fersca.lib.Logger.println;
import com.fersca.lib.Server;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author fersc
 */
public class Api {
    
    public static void main(String[] args) throws IOException, Exception {
        
        /* --> Para hostear la API previamente generada:
        
        Leer el archivo de configuración.
        Cada archivo representa una api distinta. La cual tiene especificada la estructura, las acciones y filtros.
        Luego de leer la configuración, debería prepararse una api genérica que esté preparada a recibir los métodos que estén
        en esa configuración, con lo cual la configuración en realidad lo que hace es preparar una URL de los dominios
        y algoritmos a utilizar cuando se ejecuta esa URL
        
        */
        
        Api api = new Api();
        
        api.startDB();
        api.startWebserver();        
             
        //Generar una API -->
        //---------------------
        
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

            //genera el codigo fuente
            String sourceCode = generateSourceCode(api, field, parametersWithTypes, description);
            
            //guarda el código generado
            File scriptFile = new File(rootPath+"/apis/"+api+"/"+field+"_generated.sc");
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

    private static final String rootPath = "/Users/Fernando.Scasserra/code/Varios/gradle-example/app/";

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
        
        String aiCode = generateAiCode(parametersWithTypes, description);

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

    private static String generateAiCode(Map<String, Object> parametersWithTypes, String description) {
        
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
        
        String code = executePrompt(prompt);
        
        return code;
        
    }

    private static String executePrompt(String prompt) {
        println("-----------PROMPT------------");        
        println(prompt);
        println("-----------END------------");                
        
        String code = 
        """
        result =  name + " is " + age + " years oldss, holds a degree in " + degrees + ", and is " + (adult ? "an adult" : "not an adult") + ".";
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

        Path filePath = Paths.get(rootPath+"/db/"+domain+"/"+key+".json");
        try {
            Files.delete(filePath);
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private static void saveJsonFile(String domain, String key, String jsonString) {

        Path filePath = Paths.get(rootPath+"/db/"+domain+"/"+key+".json");
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
    
    private void startWebserver() throws IOException, Exception {

        println("Inicia el webserever");
        
        //Leer cada archivo de configuración de la ruta donde están las apis y por cada uno de ellos hacer:
        ArrayList<Map<String, Object>> files = readFiles(rootPath+"apis");
                
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
            
        }       
                
    }
    
    //Base de datos de mentira de jsons
    private static final HashMap<String, Object> DB = new HashMap<>();
    
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
            println("Method: "+method+" not supported");
            context.notSupported();
            return;
        }
              
        //---> Para el caso del GET:
        
        if ("GET".equals(method)){

            Integer key= Integer.valueOf(context.getUrlPath(2));            
            
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
                context.write(finalJson);                
            } else {
                //Devuelce not fount
                context.notFound(""+key);
            }
            
        } else if ("DELETE".equals(method)){
            
            Integer key= Integer.valueOf(context.getUrlPath(2));            
            
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
            
            Integer key=null;
            
            //Si es un post genera un nuevo ID
            if (method.equals("POST")){
                key = nextIDforDomain(domain);                            
            } else { 
                //Si es un put, utiliza el ID que viene en el body
                key= Integer.valueOf(context.getUrlPath(2));
            }
            
            //guarda la key en el campo "id"
            finalJson.put("id", key);                
            
            //pasa el json a string para guardarlo
            String jsonInString = jsonString(finalJson);
            
            //Guardo el json en el disco.
            saveJsonFile(domain, key.toString(), jsonInString);

            //lo guarda en el cache
            DB.put(domain+"_"+key, jsonInString);
            
            //Devuelve el json generado en base a los campos y los datos de la DB
            context.created(finalJson);                
            
            /*
            TODO: Pensar si tiene sentido hacer un get para que se ejecuten las compilaciones.
            */
                                                    
        }
        
        
        //---> Para el caso del POST:
        
            //Obtener solo los campos que están en la estructura, se ignoran los otros.

            //Verificar si hay validaciones de esos campos, en esos casos, aplicar los algoritmos de validaciones para esos campos.

            //Si los algoritmos se ejecutaron directamente, guardar el JSON en la base de datos (por ahora un hashmap)
       

    };
    
    
    protected ArrayList<Map<String, Object>> readFiles(String path) throws IOException {
        
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
                    var jsonContent = json(content);
                                        
                    // Guarda el nombre del archivo y el contenido en el mapa                    
                    fileContents.add(jsonContent);
                                        
                }
            } else {
                System.out.println("El directorio está vacío o no se pudo leer.");
            }
        } else {
            System.out.println("La ruta especificada no es un directorio.");
        }
        return fileContents;
    }

    /**
     * Lee el contenido del directorio donde se guardan los json y los deja en memoria
     */
    private void startDB() {
                        
        // Especifica el directorio que deseas leer
        File directory = new File(rootPath+"db");
        
        ArrayList<File> domains = new ArrayList<>();

        //genera la lista de domains                            
        File[] directories = directory.listFiles();
        // Itera sobre los archivos y directorios encontrados
        if (directories != null) {
            for (File dir : directories) {
                if (dir.isDirectory()){
                    domains.add(dir);
                }                                        
            }
        } 
        
        //Por cada domain, busca los archivos y los guarda en la DB
        for (File domain : domains){

            //genera la lista de domains                            
            File[] files = domain.listFiles();
            // Itera sobre los archivos y directorios encontrados
            if (files != null) {
                for (File file : files) {
                    String keyName = domain.getName()+"_"+file.getName().split("\\.")[0];
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
    }
}
