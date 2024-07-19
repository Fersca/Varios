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

/**
 *
 * @author Fernando.Scasserra
 */
public class HttpCli {
 
    private static final Gson gson = new Gson();
    
    @SuppressWarnings("rawtypes")
    public static Map json(String jsonString) {

        Map<String, Object> map = gson.fromJson(jsonString, new TypeToken<Map<String, Object>>() {
        }.getType());

        return map;

    }

    @SuppressWarnings("rawtypes")
    public static Map getJson(String url, boolean async) throws URISyntaxException, IOException, InterruptedException, ExecutionException{
        if (async)
            return json(httpCliAsync(url));
        else
            return json(httpCli(url));
    }

    private static String httpCli(String url) throws URISyntaxException, IOException, InterruptedException{

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

    private static String httpCliAsync(String url) throws URISyntaxException, IOException, InterruptedException, ExecutionException{

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
