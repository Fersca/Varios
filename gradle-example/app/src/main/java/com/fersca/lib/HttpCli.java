package com.fersca.lib;

import static com.fersca.lib.HttpCli.json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import static com.fersca.lib.Logger.println;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Fernando.Scasserra
 */
public class HttpCli {
     
    //constructor privado para evitar el publico por default en una clase static que se usa como utils
    private HttpCli(){}
    
    private static final Gson gson = new Gson();
    
    public static Map<String, Object> json(String jsonString) {
        try {
            return gson.fromJson(jsonString, new TypeToken<Map<String, Object>>(){}.getType());
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    
    public static ArrayList<Map<String, Object>> jsonArray(String jsonString) {
        return gson.fromJson(jsonString, new TypeToken<ArrayList<Map<String, Object>>>(){}.getType());
    }
    
    public static String jsonString(Map<String, Object> jsonString) {
        return gson.toJson(jsonString);
    }
    public static String jsonString(Json jsonString) {
        return gson.toJson(jsonString.getMap());
    }

    public static Map<String, Object> getJson(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        HttpResult result = httpCli(url);        
        return json(result.body());
    }

    
    public static HttpResult get(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        return httpCli(url);        
    }
    
    public static Map<String, Object> postJson(String url, String jsonStrig) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        HttpResult result = httpPostCli(url,jsonStrig);
        return json(result.body());
    }
    public static HttpResult post(String url, String jsonStrig) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        return httpPostCli(url,jsonStrig);
        
    }
    public static HttpResult put(String url, String jsonStrig) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        return httpPutCli(url,jsonStrig);        
    }
    public static HttpResult delete(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        return httpDeleteCli(url);        
    }
    public static Map<String, Object> deleteJson(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        HttpResult result = httpDeleteCli(url);
        return json(result.body());
    }
    
    public static Map<String, Object> putJson(String url, String jsonStrig) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        HttpResult result = httpPutCli(url,jsonStrig);
        return json(result.body());
    }

    public static FutureJson getFutureJson(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        
        var response = httpCliAsync(url);
        return new FutureJson(response);        
    }
    
    //create an http client
    private static final HttpClient client = HttpClient.newHttpClient();
    private static HttpClient cliAsync;
    private static ExecutorService executorService;
    
    public static record HttpResult(String body, Integer statusCode){};
    
    private static final int TIMEOUT = 120;
    
    private static HttpResult httpCli(String url) throws URISyntaxException, IOException, InterruptedException{

        //Create the builder
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Accept", "application/json")
            .timeout(Duration.ofSeconds(TIMEOUT))
            .GET();        

        //create the request object
        HttpRequest request = b.build();          
        
        //execute the request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                        

        HttpResult result = new HttpResult(response.body(),response.statusCode());
        return result;

    }
    
    private static HttpResult httpPostCli(String url, String jsonBody) throws URISyntaxException, IOException, InterruptedException{

        //Create the builder
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Accept", "application/json", "Content-Type", "application/json")
            .timeout(Duration.ofSeconds(TIMEOUT))
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody));        

        //create the request object
        HttpRequest request = b.build();          

        //execute the request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                        

        HttpResult result = new HttpResult(response.body(),response.statusCode());
        return result;
        
    }

    private static HttpResult httpPutCli(String url, String jsonBody) throws URISyntaxException, IOException, InterruptedException{

        //Create the builder
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Accept", "application/json", "Content-Type", "application/json")
            .timeout(Duration.ofSeconds(TIMEOUT))
            .PUT(HttpRequest.BodyPublishers.ofString(jsonBody));        

        //create the request object
        HttpRequest request = b.build();          

        //execute the request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                        

        HttpResult result = new HttpResult(response.body(),response.statusCode());
        return result;
        
    }

    private static HttpResult httpDeleteCli(String url) throws URISyntaxException, IOException, InterruptedException{

        //Create the builder
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Accept", "application/json", "Content-Type", "application/json")
            .timeout(Duration.ofSeconds(TIMEOUT))
            .DELETE();        

        //create the request object
        HttpRequest request = b.build();          

        //execute the request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                        

        HttpResult result = new HttpResult(response.body(),response.statusCode());
        return result;
        
    }
       
    public static class FutureJson  {
        
        private final long time1;
        private final CompletableFuture<HttpResponse<String>> response;
        
        public FutureJson(CompletableFuture<HttpResponse<String>> response){
           time1 = System.currentTimeMillis();
           this.response = response;
        }
        
        public Map<String, Object> getJson(){
            
            //Wait for the future to be available
            HttpResponse<String> resp=null;
            try {
                resp = this.response.get();
            } catch (InterruptedException ex) {
                println(Level.SEVERE, ex);
                // Hay que manejar la interrupted exception
                Thread.currentThread().interrupt();                
            } catch (ExecutionException ex) {
                println(Level.SEVERE, ex);
            }

            long time2 = System.currentTimeMillis();

            long diff = time2-time1;
            println(String.valueOf(diff));
            
            if (resp!=null)
                return json(resp.body());
            else
                return new HashMap<>();
            
        }
    }
    
    private static CompletableFuture<HttpResponse<String>> httpCliAsync(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{

        //Create the builder
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Accept", "application/json")
            .timeout(Duration.ofSeconds(TIMEOUT))
            .GET();        

        //create the request object
        HttpRequest request = b.build();          

        //create the executor service
        if (executorService==null)
            executorService = Executors.newFixedThreadPool(3);

        //create the client with executor
        if (cliAsync==null)
            cliAsync = HttpClient.newBuilder().executor(executorService).build();

        //ejecute the request with the client in async way
        CompletableFuture<HttpResponse<String>> response1 = cliAsync.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        return response1;

    }
    
}