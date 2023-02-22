package com.mycompany.javabasic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author Fernando.Scasserra
 */
public class JavaBasic {

    public static void main(String[] args) throws  Exception {

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
                
        //Concurrency Example
        j.concurrency();        
        
        //http server
        j.createHttpServer();
        
        //waits 10 seconds
        System.out.println("Waiting 10 seconds until shutdown");
        Thread.sleep(10000);
        
        //shutdown http server
        j.shutdownWebserver();
        
        //System.exit(0);
    }

    private Server server;
    
    protected void createHttpServer() throws Exception {

        // Create a Server instance.
        server = new Server();

        // The HTTP configuration object.
        HttpConfiguration httpConfig = new HttpConfiguration();
        // Configure the HTTP support, for example:
        httpConfig.setSendServerVersion(false);        
        
        // The ConnectionFactory for HTTP/1.1.
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);        
        
        // Create a ServerConnector instance on port 8080.
        ServerConnector connector1 = new ServerConnector(server, http11);
        connector1.setPort(8080);
        server.addConnector(connector1);

        // Set a simple Handler to handle requests/responses.
        server.setHandler(new AbstractHandler()
        {
            @Override
            public void handle(String target, Request jettyRequest, HttpServletRequest request, HttpServletResponse response) throws IOException
            {
                // Mark the request as handled so that it
                // will not be processed by other handlers.
                jettyRequest.setHandled(true);
                response.setStatus(200);
                response.setContentType("text/html; charset=UTF-8");

                // Write a Hello World response.
                response.getWriter().print("" +
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "  <title>Jetty Hello World Handler</title>" +
                    "</head>" +
                    "<body>" +
                    "  <p>Hello World</p>" +
                    "</body>" +
                    "</html>" +
                    "");                
                
                System.out.println("Request Handled");
            }
        });

        // Start the Server so it starts accepting connections from clients.
        server.start();                
        System.out.println("Server started");
        
    }

    protected void shutdownWebserver(){
        try {
            System.out.println("Shutting down server");
            server.stop();
        } catch (Exception ex) {
            System.out.println("Error while closing webserver");
        }
    }
    
    private class RunableImpl implements Runnable {
        public void run() {
            System.out.println("Asynchronous task 0");
        }
    } 
    
    private class CallableImpl implements Callable {
        @Override
        public String call() throws InterruptedException {
            System.out.println("Asynchronous Callable task 0");
            Thread.sleep(1000);
            return "Callable 0";
        }
    } 

    
    private void concurrency() throws InterruptedException, ExecutionException {
        
        //Create an executor service with 10 threads
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        //One way to create a runnable is throught a class definition.
        var r0 = new RunableImpl();
        executorService.execute(r0);

        //Another way it to creathe the class inline in the parameter call.
        executorService.execute(new Runnable() {
            public void run() {
                System.out.println("Asynchronous task 1");
            }
        });        
        
        //Another way is to create an inline object and assign it to a varieble
        var r2 = new Runnable() {
            public void run() {
                System.out.println("Asynchronous task 2");
            }
        };        
        
        executorService.execute(r2);
        
        //Another way is with lambda expressions
        Runnable r3 = () -> {
            System.out.println("Asynchronous task 3");
        };        
        
        executorService.execute(r3);
        
        //other way is to send the lambda directly in the execution
        executorService.execute(() -> {
            System.out.println("Asynchronous task 4");
        });        
        
        //Send a Callable task (the callable return a value)
        var c1 = new CallableImpl();
        Future f1 = executorService.submit(c1);
        var futureValue = f1.get();
        System.out.println("Valor futuro: "+futureValue);
        
        //Smallest way -->
        Callable c2 = () -> {
            System.out.println("Asynchronous Callable task 2");
            return "Callable task 2";
        };        

        var future2 = executorService.submit(c2);
        System.out.println("Valor futuro: "+future2.get());

        
        //Shutdown the executor
        executorService.shutdown();
                
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
