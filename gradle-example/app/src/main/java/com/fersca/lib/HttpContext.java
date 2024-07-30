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
    
    public HttpContext (HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }
    private final HttpServletRequest request;
    private final HttpServletResponse response;

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


}
    