package com.fersca.lib;

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
import java.util.logging.Logger;

/**
 *
 * @author Fernando.Scasserra
 */
public class HttpCli {
 
    private static final Gson gson = new Gson();
    
    public static Map json(String jsonString) {

        return gson.fromJson(jsonString, new TypeToken<Map<String, Object>>(){}.getType());

    }

    public static Map getJson(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        return json(httpCli(url));
    }

    public static FutureJson getFutureJson(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        
        var response = httpCliAsync(url);
        FutureJson fResponse = new FutureJson(response);        
        return fResponse;
    }
    
    //create an http client
    private static final HttpClient client = HttpClient.newHttpClient();;
    private static HttpClient cliAsync;
    private static ExecutorService executorService;
    
    private static String httpCli(String url) throws URISyntaxException, IOException, InterruptedException{

        //Create the builder
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Accept", "application/json")
            .timeout(Duration.ofSeconds(10))
            .GET();        

        //create the request object
        HttpRequest request = b.build();          
        
        //execute the request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                        

        //return the body content
        return response.body();

    }

    public static class FutureJson  {
        
        private long time1;
        private CompletableFuture<HttpResponse<String>> response;
        
        public FutureJson(CompletableFuture<HttpResponse<String>> response){
           time1 = System.currentTimeMillis();
           this.response = response;
        }
        
        public Map getJson(){
            
            //Wait for the future to be available
            HttpResponse<String> response=null;
            try {
                response = this.response.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(HttpCli.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(HttpCli.class.getName()).log(Level.SEVERE, null, ex);
            }

            long time2 = System.currentTimeMillis();

            long diff = time2-time1;
            System.out.println(diff);           

            return json(response.body());    
            
        }
    }
    
    private static CompletableFuture<HttpResponse<String>> httpCliAsync(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{

        //Create the builder
        HttpRequest.Builder b = HttpRequest.newBuilder()
            .uri(new URI(url))
            .headers("Accept", "application/json")
            .timeout(Duration.ofSeconds(10))
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
