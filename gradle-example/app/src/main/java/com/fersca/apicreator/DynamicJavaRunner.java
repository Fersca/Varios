package com.fersca.apicreator;

import static com.fersca.apicreator.Storage.*;
import static com.fersca.apicreator.Storage.saveCodeFile;

/**
 *
 * @author Fernando.Scasserra
 */
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class DynamicJavaRunner {

    //constructor privado para evitar el publico por default en una clase static que se usa como utils
    private DynamicJavaRunner(){}
    
    public static boolean compile(String api, String sourceCode, String className) {
                
        try {
        	
        	//graba el código en una clase en el disco
            saveCodeFile(api, className, sourceCode);
            
            // Obtiene el compilador del sistema
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                throw new IllegalStateException("No se encontró el compilador de Java.");
            }
            
            // Compila el archivo .java
            int result = compiler.run(null, null, null, getCodeDiretory()+api+"/"+className + ".java");
            
            return result == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object execute(String api, String className, String methodName, Map<String, Object> parameters) {
        try {
            
            // Directorio hardcodeado donde se espera que estén las clases compiladas
            File hardcodedOutDir = new File(getCodeDiretory()+api);
            
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{hardcodedOutDir.toURI().toURL()});
            Class<?> cls = Class.forName(className, true, classLoader);
                        
            // Encuentra y ejecuta el método especificado
            Method method = cls.getMethod(methodName, Map.class);
            
            return method.invoke(null,parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}