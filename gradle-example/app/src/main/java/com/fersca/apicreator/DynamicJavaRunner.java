package com.fersca.apicreator;

/**
 *
 * @author Fernando.Scasserra
 */
import java.io.File;
import java.io.FileWriter;
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
        
    public static boolean compile(String sourceCode, String className) {
                
        try {
            // Escribe el código fuente en un archivo .java
            FileWriter writer = new FileWriter(className + ".java");
            writer.write(sourceCode);
            writer.close();
            
            // Obtiene el compilador del sistema
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                throw new IllegalStateException("No se encontró el compilador de Java.");
            }
            
            // Compila el archivo .java
            int result = compiler.run(null, null, null, className + ".java");
            
            return result == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Object execute(String className, String methodName, Map<String, Object> parameters) {
        try {
            
            // Directorio hardcodeado donde se espera que estén las clases compiladas
            File hardcodedOutDir = new File("/Users/Fernando.Scasserra/code/Varios/gradle-example/app");
            
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
