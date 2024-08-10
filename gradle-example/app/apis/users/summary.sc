import java.util.Map;

public class users_summary {

    public static Object getMessage(Map<String, Object> parametros) {

        Double id = (Double) parametros.get("id");
        String name = (String) parametros.get("name");
        Double age = (Double) parametros.get("age");
        boolean adult = (Boolean) parametros.get("adult");
        String degrees = (String) parametros.get("degrees");
        
        //// Begin AI-Code ////

        // return 0;
        return name + " is " + age + " years old, holds a degree in " + degrees + ", and is " + (adult ? "an adult" : "not an adult") + ".";

        //// End AI-Code   ////
    }

    public static void main(String[] args) {
        System.out.println("");
    }
}
