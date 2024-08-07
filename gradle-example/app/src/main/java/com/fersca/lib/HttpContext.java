package com.fersca.lib;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import static com.fersca.lib.Logger.println;

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


}
    