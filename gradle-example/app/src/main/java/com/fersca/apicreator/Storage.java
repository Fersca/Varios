package com.fersca.apicreator;

import static com.fersca.lib.HttpCli.json;
import static com.fersca.lib.Logger.println;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fersca.lib.Json;

public class Storage {

	//Directories 
    private static final String APIS_CALCULATED_FIELDS_CODE = "apis_calculated_fields_code/";
    private static final String APIS_COMPILED_FIELDS_CODE = "apis_compiled_fields_code/";
    private static final String APIS_DIR = "apis/";
    protected static final String DB_DIR = "db/";
		
    protected static final String ROOTPATH;

    static {
        ROOTPATH = System.getProperty("user.dir")+"/";
        try {
			assureDirectory(ROOTPATH+APIS_DIR, Directory.API_DIR);
		} catch (IOException e) {
		}
    }
	
	
	private Storage(){}
		
    protected static Map<String, String> readDomainContent(String domain) {
        
    	HashMap<String, String> contentMap = new HashMap<>();
    	
        // Especifica el directorio que deseas leer
        File directory = new File(ROOTPATH+DB_DIR+domain);
        
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
                    contentMap.put(keyName, content);
                } catch (IOException ex) {
                    Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return contentMap;
        
    }
	
	
    protected static void deleteFile(String domain, String key) {

        Path filePath = Paths.get(ROOTPATH+DB_DIR+domain+"/"+key+".json");
        try {
            Files.delete(filePath);
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    protected static void saveJsonFile(String domain, String key, String jsonString, Directory type) {
        
        Path filePath;
        
        if (type==Directory.DOMAIN){
            filePath = Paths.get(ROOTPATH+DB_DIR+domain+"/"+key+".json");
        } else {
            filePath = Paths.get(ROOTPATH+APIS_DIR+domain+".api.def");
        }
        
        try {
            Files.write(filePath, jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
	
    protected enum Directory {
        DOMAIN,
        DEFINITION,
        COMPILE,
        API_DIR
    }

    protected static void createAPIDefinition(String domain, String jsonString) throws IOException{
        
        //crea el directorio donde estaran las compilaciones de los campos de la api
        assureDirectory(domain, Directory.DEFINITION);
        
        //guarda el file con la definición de la api
        saveJsonFile(domain, domain, jsonString, Directory.DEFINITION);
    }

    protected static void deleteAPIDefinition(String domain) throws IOException{                
        try {
            String path = ROOTPATH+APIS_DIR+domain+".api.def";
            File file = new File(path);
            Path filePath = Paths.get(file.getAbsolutePath());        
            Files.delete(filePath);
        } catch (NoSuchFileException e){
            //no pasa nada si no encuntra el file.
        }
    }
    
    protected static void assureDirectory(String name, Directory type) throws IOException {
        
        String path;
        
        if (type==Directory.DOMAIN){
            path = ROOTPATH+DB_DIR+name;
        } else if (type==Directory.DEFINITION){
            path = ROOTPATH+APIS_CALCULATED_FIELDS_CODE+name;
        } else if (type==Directory.COMPILE){
        	path = ROOTPATH+APIS_COMPILED_FIELDS_CODE+name;
        } else {
        	path = ROOTPATH+APIS_DIR;
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
    
    protected static ArrayList<Json> readAPIDefinitions() throws IOException {
        
        // Especifica el directorio que deseas leer
        File directory = new File(ROOTPATH+APIS_DIR);
        
        ArrayList<Json> fileContents = new ArrayList<>();

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
                        var jsonContent = new Json(json(content));
                        // Guarda el nombre del archivo y el contenido en el mapa                    
                        fileContents.add(jsonContent);                        
                    } catch (Exception e){
                        println("Error parsing json: ---->");
                        println("Path: "+file.getAbsolutePath());
                        println(content);
                        e.printStackTrace();
                    }             
                                        
                }
            } else {
                println("El directorio está vacío o no se pudo leer.");
            }
        } else {
            println("La ruta especificada no es un directorio.");
        }
        return fileContents;
    }
    
	protected static String saveGeneratedCode(String api, String field, String sourceCode) {
		//guarda el código generado
		File scriptFile = new File(ROOTPATH+APIS_CALCULATED_FIELDS_CODE+api+"/"+field+"_generated.sc");
		Path filePath = Paths.get(scriptFile.getAbsolutePath());
		try {
		    Files.writeString(filePath, sourceCode);
		} catch (IOException ex) {
		    Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);
		    return "Error storing code for "+api+"/"+field;
		}
		return null;
	}
	
    protected static void saveCodeFile(String api, String fileName, String sourceCode) throws IOException {
        // Escribe el código fuente en un archivo .java
        FileWriter writer = new FileWriter(ROOTPATH+APIS_COMPILED_FIELDS_CODE+api+"/"+fileName + ".java");
        writer.write(sourceCode);
        writer.close();    	
    }
    
    protected static String getCodeDiretory() {
    	return ROOTPATH+APIS_COMPILED_FIELDS_CODE;
    }
	
}
