import java.util.Map;

public class users_summary {

    public static Object getMessage(Map<String, Object> parametros) {

        
Double id = (Double) parametros.get("id"); //From code
String name = (String) parametros.get("name"); //From code
Double age = (Double) parametros.get("age"); //From code
boolean adult = (Boolean) parametros.get("adult"); //From code
String degrees = (String) parametros.get("degrees"); //From code

        Object result;
        //// Begin AI-Code ////

        result =  name + " is " + age + " years oldss, holds a degree in " + degrees + ", and is " + (adult ? "an adult" : "not an adult") + ".";


        //// End AI-Code   ////
        return result;
    }

    public static void main(String[] args) {
        System.out.println("");
    }
}
