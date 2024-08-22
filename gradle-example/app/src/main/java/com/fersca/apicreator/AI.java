package com.fersca.apicreator;

import static com.fersca.lib.Logger.println;

import com.fersca.lib.Json;

public class AI {

	private AI(){}
	
    protected static String generateSourceCode(String api, String field, Json parametersWithTypes, String description) {
        
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
	
	
}
