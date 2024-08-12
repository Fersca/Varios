package com.fersca.lib;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import static com.fersca.lib.Logger.println;
import java.io.BufferedReader;
import java.util.stream.Collectors;
import static com.fersca.lib.HttpCli.json;
import java.util.logging.Logger;

/**
 *
 * @author Fernando.Scasserra
 */
public class HttpContext {
    
    public HttpContext (HttpServletRequest request, HttpServletResponse response, Map<String, Object> args){
        this.request = request;
        this.response = response;
        this.args = args;
    }
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private Map<String, Object> args = null;

    public void print(String message){

        try {
            this.response.getWriter().print(message);                

        } catch (IOException ex) {
            println(Level.SEVERE, ex);
        }
    }

    public void write(){            
        this.response.setStatus(200);
        this.response.setContentType("text/html; charset=UTF-8");
        this.response.setCharacterEncoding("UTF-8");                                
    }    

    public void write(String message){

        try {
            this.response.getWriter().print(message);                
            this.write();                

        } catch (IOException ex) {
            println(Level.SEVERE, ex);
        }
    }
    // Crear una instancia de Gson
    private static final Gson gson = new Gson();
    
    public void write(Map<String, Object> json){
        try {

            // Convertir el mapa a un string JSON
            String jsonString = gson.toJson(json);                
            this.response.getWriter().print(jsonString);

            this.response.setStatus(200);
            this.response.setContentType("application/json; utf-8");
            this.response.setCharacterEncoding("UTF-8");


        } catch (IOException ex) {
            println(Level.SEVERE, ex);
        }
    }

    public void created(Map<String, Object> json){
        this.write(json);
        //Overwrite the 200 with 201
        this.response.setStatus(201);                        
    }
       
    public String getParameter(String param){
        return this.request.getParameter(param);
    }
    
    public HttpServletRequest getRequest(){
        return this.request;
    }
    public Map<String, Object> getArgs(){
        return this.args;
    }

    public void notSupported() {
        this.write("Method not supported");
        this.response.setStatus(405);
        this.response.setContentType("text/html; charset=UTF-8");
        this.response.setCharacterEncoding("UTF-8");                                
    }

    public String getUrlPath(int i) {
        // si pongo /users/12 me pone en la pos 0 nada, en la 1 users en la 2 "12"
        String[] split = request.getRequestURI().split("/");
        return split[i];

    }

    private static final String htmlNotFound="""
                       <html>
                       <h2> 404 Element not found: ##ELEMENT## </h2>
                       </html>
                       """;

    public void notFound(String message) {        
        this.write(htmlNotFound.replaceAll("##ELEMENT##", message));
        this.response.setStatus(400);
        this.response.setContentType("text/html; charset=UTF-8");
        this.response.setCharacterEncoding("UTF-8");                                                                   
    }
    
    public Map<String, Object> getJsonBody(){
        return json(getBody());
    }
    
    public String getBody()  {
        BufferedReader reader=null;
        try {
            reader = this.request.getReader();
        } catch (IOException ex) {
            Logger.getLogger(HttpContext.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public void conflict() {
        this.write("The resource already exists.");
        this.response.setStatus(409);
        this.response.setContentType("text/html; charset=UTF-8");
        this.response.setCharacterEncoding("UTF-8");                                                                           
    }

}
    