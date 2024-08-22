package com.fersca.lib;

import static com.fersca.lib.Logger.println;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Fernando.Scasserra
 */
public class HttpContext {
    
    private static final String UTF_8 = "UTF-8";
	private static final String APPLICATION_JSON_UTF_8 = "application/json; utf-8";

	public HttpContext (HttpServletRequest request, HttpServletResponse response, Json args){
        this.request = request;
        this.response = response;
        this.args = args;
    }
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private Json args = null;

    public void print(String message){

        try {
            this.response.getWriter().print(message);                

        } catch (IOException ex) {
            println(Level.SEVERE, ex);
        }
    }

    public void write(){       
    	setHttpStatus(200);
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
            setHttpStatus(200);

        } catch (IOException ex) {
            println(Level.SEVERE, ex);
        }
    }
    public void write(Json json){
        try {

            // Convertir el mapa a un string JSON
            String jsonString = gson.toJson(json.getMap());                
            this.response.getWriter().print(jsonString);
            setHttpStatus(200);

        } catch (IOException ex) {
            println(Level.SEVERE, ex);
        }
    }

    public void write(List<Json> jsonArray){
        try {

            ArrayList<Map<String, Object>> list = new ArrayList<>();
            for (Json j : jsonArray){
                list.add(j.getMap());
            }
            
            // Convertir el mapa a un string JSON
            String jsonString = gson.toJson(list);                            
            this.response.getWriter().print(jsonString);
            setHttpStatus(200);
        } catch (IOException ex) {
            println(Level.SEVERE, ex);
        }
    }
    
    public void created(Json json){
        this.write(json.getMap());
        setHttpStatus(201);                        
    }
       
    public String getParameter(String param){
        return this.request.getParameter(param);
    }
    public Enumeration<String> getParameterNames(){
        return this.request.getParameterNames();
    }    
    public HttpServletRequest getRequest(){
        return this.request;
    }
    public Json getArgs(){
        return this.args;
    }

    public void notSupported() {
        setErrorMessage("Method not supported");
        setHttpStatus(405);
    }

    public String getUrlPath(int i) {
        // si pongo /users/12 me pone en la pos 0 nada, en la 1 users en la 2 "12"
        String[] split = request.getRequestURI().split("/");        
        if (split.length>=(i+1))
            return split[i];
        else
            return null;

    }

    public void notFound(String message) {        
    	setErrorMessage(message);
    	setHttpStatus(404);
    }
    
    public Json getJsonBody(){       
        return new Json(getBody());
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
    
    public void badRequest(String message) {
        setErrorMessage(message);
        setHttpStatus(400);                                                                           
    }

	private void setHttpStatus(int statusCode) {
		this.response.setStatus(statusCode);
        this.response.setContentType(APPLICATION_JSON_UTF_8);
        this.response.setCharacterEncoding(UTF_8);
	}

	private void setErrorMessage(String message) {
		String m = """
{"message":"##MESSAGE##"}""";
        this.write(m.replaceAll("##MESSAGE##", message));
	}

    public void ok(Json json) {
        this.write(json.getMap());
        setHttpStatus(200);                        
    }

}
    