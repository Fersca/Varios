package com.fersca.apicreator;

import static com.fersca.apicreator.DynamicJavaRunner.compile;
import static com.fersca.apicreator.DynamicJavaRunner.execute;
import static com.fersca.lib.Logger.println;
import static com.fersca.lib.HttpCli.json;
import com.fersca.lib.HttpContext;
import static com.fersca.lib.Logger.println;
import com.fersca.lib.Server;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    private static Object getCalculatedValue(String api, String field) {
        
        //busca el archivo que contiene el script para el campo de la api especificada.

        // Especifica el directorio que deseas leer
        File scriptFile = new File(rootPath+"/apis/"+api+"/"+field+".sc");
        Path filePath = Paths.get(scriptFile.getAbsolutePath());
        
        try {
            
            // Leer el contenido del archivo de texto
            String sourceCode = Files.readString(filePath);
            String className = field;
            String methodName = "getMessage";                        
            
            // Compilar el código fuente
            boolean isCompiled = compile(sourceCode, className);
            if (isCompiled) {
                // Ejecutar la clase compilada y capturar el valor de retorno del método
                Object result = execute(className, methodName);
                System.out.println("Resultado del método: " + result);
                return result;
            } else {
                return "Script compile error.";
            }
                                                                   
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
            return "Script error.";
        }              
        
    }

    private static final String rootPath = "/Users/Fernando.Scasserra/code/Varios/gradle-example/app/";
    
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
    private static final HashMap<Integer, Object> DB = new HashMap<>();
    
    private static void generalController(HttpContext context) {

        
        var arguments = context.getArgs();
        @SuppressWarnings("unchecked")
        var structure = (Map<String, Object>) arguments.get("structure");
        @SuppressWarnings("unchecked")
        var fields = (Map<String, Object>)structure.get("fields");
        String domain = (String)structure.get("domain");
        
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
       
        Integer key= Integer.valueOf(context.getUrlPath(2));
        
        //---> Para el caso del GET:
        
        if ("GET".equals(method)){
            
            //ir a buscar el Json guardado en la base de datos para el ID identificado.
            Object element = DB.get(key);
            
            //si existe el elemento en la base de datos
            if (element!=null){               
                //devuelve el json en el request
                
                var jsonFromDB = json((String)element);
                Map<String, Object> finalJson = new HashMap<>();
                
                //va creando el json final en base a lo que viene en los fields               
                for (String field : fields.keySet()) {
                    
                    //obtiene el tipo del dato del campo
                    String valueType = (String)fields.get(field);
                    String valueFromDB = (String)jsonFromDB.get(field);
                   
                    //Según sea el value type lo pone en el json final y calcula los campos calculados real time
                    switch (valueType) {
                        case "Number" -> finalJson.put(field, Integer.valueOf(valueFromDB));
                        case "String" -> finalJson.put(field, valueFromDB);
                        case "Boolean" -> finalJson.put(field, Boolean.valueOf(valueFromDB));
                        case "Calculated" -> finalJson.put(field, getCalculatedValue(domain, field));
                        default -> println("Tipo inválido: "+field + "("+valueType+")");
                    }                            
                }                    
                
                //Devuelve el json generado en base a los campos y los datos de la DB
                context.write(finalJson);                
            } else {
                //Devuelce not fount
                context.notFound(""+key);
            }
            
            //Verificar si hay algún dato que se calcule en real time.

            //Aplicar el algoritmo de real time para ese campo el cual debería estar guardado dentro de la configuración de los algoritmos del dominio.            
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

    private void startDB() {
        String json = """
                        {
                        "id":"22",
                        "name":"Fernando",
                        "age":"42",
                        "adult":"true",
                        "degrees": "Engineering"
                        }                                            
                      """;
        DB.put(22, json);
                
    }


    
}


