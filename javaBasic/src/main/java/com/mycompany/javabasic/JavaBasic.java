package com.mycompany.javabasic;

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

/**
 *
 * @author Fernando.Scasserra
 */
public class JavaBasic {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        var j = new JavaBasic();
        var m = j.greetings();

        System.out.println(m);

        //Test Json Parsing
        String json = "{\"name\":\"mkyong\", \"age\":33}";
        var jsonMap = j.processJson(json);

        System.out.println(m);
        System.out.println(jsonMap.get("name"));
        System.out.println(jsonMap.get("age"));
        
        //get the json map from url
        var jsonMap2 = j.getJson("https://api.mercadolibre.com/users/20",false);        
        System.out.println(jsonMap2.get("nickname"));
        
        var jsonMap3 = j.getJson("https://api.mercadolibre.com/users/10",true);        
        System.out.println(jsonMap3.get("nickname"));        
                
        //System.exit(0);
    }

    protected String greetings() {
        return "Hola Fer";
    }

    protected Map processJson(String jsonString) {

        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(jsonString, new TypeToken<Map<String, Object>>() {
        }.getType());
        return map;

    }
    
    protected Map getJson(String url, boolean async) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        if (async)
            return processJson(httpCliAsync(url));
        else
            return processJson(httpCli(url));
    }
    
    protected String httpCli(String url) throws URISyntaxException, IOException, InterruptedException{

            //Create the builder
            HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .GET();        
            
            //create the request object
            HttpRequest request = b.build();          
            
            //create an http client
            HttpClient client = HttpClient.newHttpClient();
                        
            //execute the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());                        
            
            //return the body content
            return response.body();
       
    }
    
    protected String httpCliAsync(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{

            //Create the builder
            HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(new URI(url))
                .headers("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .GET();        
            
            //create the request object
            HttpRequest request = b.build();          
                   
            //create the executor service
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            
            //create the client with executor
            HttpClient cliAsync = HttpClient.newBuilder()
                    .executor(executorService)
                    .build();
            
            //ejecute the request with the client in async way
            CompletableFuture<HttpResponse<String>> response1 = cliAsync.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            var time1 = System.currentTimeMillis();
            System.out.println("Processing request");

            //Wait for the future to be available
            HttpResponse<String> response = response1.get();
            
            var time2 = System.currentTimeMillis();
                        
            var diff = time2-time1;
            System.out.println(diff);

            String body = response.body();    
            
            //Close the thread pool
            executorService.shutdownNow();

            return body;
       
    }
    

}
