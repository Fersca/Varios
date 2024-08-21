package com.fersca.lib;

import static com.fersca.lib.Logger.println;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Fernando.Scasserra
 */
public class Server {

    //constructor privado para evitar el publico por default en una clase static que se usa como utils
    private Server(){}

    private static org.eclipse.jetty.server.Server jettyServer;
    
    private static final Map<String, Controller> urls = new HashMap<>();
    
    public static void createHttpServer() throws Exception {

        /* No funciona si no le pongo --enable-preview en el momento del build pero no me sale en netbeans
        // Crear un QueuedThreadPool utilizando virtual threads
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setThreadFactory(Thread.ofVirtual().factory());
        */
        
        // Create a Server instance.
        jettyServer = new org.eclipse.jetty.server.Server();

        // The HTTP configuration object.
        HttpConfiguration httpConfig = new HttpConfiguration();
        // Configure the HTTP support, for example:
        httpConfig.setSendServerVersion(false);        
        
        // The ConnectionFactory for HTTP/1.1.
        HttpConnectionFactory http11 = new HttpConnectionFactory(httpConfig);        
        
        // Create a ServerConnector instance on port 8080.
        ServerConnector connector1 = new ServerConnector(jettyServer, http11);
        int port = 8080;
        connector1.setPort(port);
        jettyServer.addConnector(connector1);

        // Set a simple Handler to handle requests/responses.
        jettyServer.setHandler(new AbstractHandler()
        {
            @Override
            public void handle(String target, Request jettyRequest, HttpServletRequest request, HttpServletResponse response) throws IOException
            {
                // Mark the request as handled so that it
                // will not be processed by other handlers.
                jettyRequest.setHandled(true);                
                    
                String domain = "/";
                if (!request.getRequestURI().equals("/")){                    
                    // si pongo /users/12 me pone en la pos 0 nada, en la 1 users en la 2 "12"
                    String[] split = request.getRequestURI().split("/");
                    domain = "/"+split[1];
                }
                                
                Controller controlador = urls.get(domain);
                if (controlador==null){
                    String html="""
                                   <html>
                                   <h2> 400 Domain not found: ##DOMAIN## </h2>
                                   <h3> Available APIs </h3>
                                   <br>
                                   <table>
                                   ##APIS##
                                   </table>
                                   </html>
                                   """;
                    
                    String apiMessage="";
                    for (String url : urls.keySet()) {
                        apiMessage = apiMessage + "<tr><td>"+url+"</tr></td>";
                    }                    
                    html = html.replace("##DOMAIN##", request.getRequestURI());
                    html = html.replace("##APIS##", apiMessage);
                    
                    response.getWriter().print(html);                
                    response.setStatus(400);
                    response.setContentType("text/html; charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");                                                    
                } else {
                    Consumer<HttpContext> metodo = controlador.controller;
                    HttpContext context = new HttpContext(request, response,controlador.args);
                    metodo.accept(context);                                                   
                }
                                
            }
        });

        // Start the Server so it starts accepting connections from clients.
        jettyServer.start();
        println("Server started, port: "+port);
        
    }
    
    public static String serverStatus(){
        if (jettyServer!=null)
            return jettyServer.getState();
        else
            return "null";
    }
        
    public static void addController(String name, Consumer<HttpContext> controller, Json args){
        urls.put(name, new Controller(controller, args));
    }
    
    public static void shutdownWebserver(){
        try {
            println("Shutting down server");
            jettyServer.stop();
        } catch (Exception ex) {
            println("Error while closing webserver");
        }
    }
    
    private record Controller(Consumer<HttpContext> controller, Json args){}

    
}
