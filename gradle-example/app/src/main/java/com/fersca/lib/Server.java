package com.fersca.lib;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import static com.fersca.lib.Logger.println;

/**
 *
 * @author Fernando.Scasserra
 */
public class Server {

    //constructor privado para evitar el publico por default en una clase static que se usa como utils
    private Server(){}

    private static org.eclipse.jetty.server.Server jettyServer;
    
    private static final Map<String, Consumer<HttpContext>> urls = new HashMap<>();
    
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
        connector1.setPort(8080);
        jettyServer.addConnector(connector1);

        // Set a simple Handler to handle requests/responses.
        jettyServer.setHandler(new AbstractHandler()
        {
            @Override
            @SuppressWarnings("unchecked")
            public void handle(String target, Request jettyRequest, HttpServletRequest request, HttpServletResponse response) throws IOException
            {
                // Mark the request as handled so that it
                // will not be processed by other handlers.
                jettyRequest.setHandled(true);                
                
                /*
                // Obtener el path de la URL
                String contextPath = request.getContextPath();
                String servletPath = request.getServletPath();
                String pathInfo = request.getPathInfo();
                String requestURI = request.getRequestURI();
                String queryString = request.getQueryString();

                // Imprimir los paths para demostración
                response.setContentType("text/plain");
                response.getWriter().println("Context Path: " + contextPath);
                response.getWriter().println("Servlet Path: " + servletPath);
                response.getWriter().println("Path Info: " + pathInfo);
                response.getWriter().println("Request URI: " + requestURI);
                response.getWriter().println("Query String: " + queryString);
                */
                
                Consumer<HttpContext> metodo = urls.get(request.getRequestURI());
                HttpContext context = new HttpContext(request, response);
                metodo.accept(context);                               

                println("Request Handled");
            }
        });

        // Start the Server so it starts accepting connections from clients.
        jettyServer.start();
        println("Server started");
        
    }
    
    public static String serverStatus(){
        if (jettyServer!=null)
            return jettyServer.getState();
        else
            return "null";
    }
        
    public static void addController(String name, Consumer<HttpContext> controller){
        urls.put(name, controller);
    }
    
    public static void shutdownWebserver(){
        try {
            println("Shutting down server");
            jettyServer.stop();
        } catch (Exception ex) {
            println("Error while closing webserver");
        }
    }

    
}
