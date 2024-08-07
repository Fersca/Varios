package com.fersca.apicreator;

/**
 *
 * @author Fernando.Scasserra
 */
import java.io.File;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicJavaRunner {

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

    public static Object execute(String className, String methodName) {
        try {
            
            // Directorio hardcodeado donde se espera que estén las clases compiladas
            File hardcodedOutDir = new File("/Users/Fernando.Scasserra/code/Varios/gradle-example/app");
            
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{hardcodedOutDir.toURI().toURL()});
            Class<?> cls = Class.forName(className, true, classLoader);
                                    
            // Encuentra y ejecuta el método especificado
            Method method = cls.getMethod(methodName);
            return method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
