package com.fersca.lib;

import static com.fersca.lib.HttpCli.getFutureJson;
import static com.fersca.lib.HttpCli.getJson;
import static com.fersca.lib.HttpCli.json;
import static com.fersca.lib.HttpCli.postJson;
import static com.fersca.lib.Logger.println;
import static com.fersca.lib.Logger.setLogLevel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fersca.lib.HttpCli.FutureJson;


/**
 *
 * @author Fernando.Scasserra
 */
public class ServerTest {
    
    public ServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        //shutdown http server
        Server.shutdownWebserver();
        String status = Server.serverStatus();
        assertEquals("STOPPED",status);       
    }
    
    @Before
    public void setUp() {
        setLogLevel(Level.INFO);
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void test_json_object_to_json_String() {

        //Test Json Parsing
        String json = """
            {
                "name":"mkyong", 
                "age":33
            }
            """;

        var jsonMap = HttpCli.json(json);

        //Chequea que transforme bien de json string a json
        assertEquals(jsonMap.get("name"),"mkyong");
        assertEquals(jsonMap.get("age"),33.0);
               
    }
    
    @Test
    public void test_get_json_from_url() {
        
        //get the json map from url
        Map jsonMap2 = null;        
        try {
            jsonMap2 = getJson("https://api.mercadolibre.com/users/20");
        } catch (Exception ex) {
            fail("Error getting content from url");
        }
        
        String nickname = jsonMap2.get("nickname").toString();
                
        assertEquals("MPAGO_MLB",nickname);
        
    }
    
    @Test
    public void test_get_json_from_url_async(){

        //get the json map with an async call
        FutureJson fJson = null;                
        try {
            fJson = getFutureJson("https://api.mercadolibre.com/users/10");
        } catch (Exception ex) {
            fail("Error getting content from url in async way");
        }
        
        //wait until json is available
        var jsonMap3 = fJson.getJson();        
        String nickname = jsonMap3.get("nickname").toString();                
        assertEquals("ANGEL_TEST10",nickname);
        
    }
    
    @Test
    public void test_webserver_cretion_and_adding_a_controller(){

        if (Server.serverStatus().equals("STARTED")){
            Server.shutdownWebserver();
        }
        
        try {
            //http server
            Server.createHttpServer();
        } catch (Exception ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Add the request handlers      
        Server.addController("/ping", ServerTest::pong,null);     
        //Add the request handlers for json     
        Server.addController("/pingJson", ServerTest::pongJson,null);     
        
        
        String status = Server.serverStatus();
        assertEquals("STARTED",status);
                
    }

    @Test
    public void test_ping_to_webserver(){

        //get the json map from url
        Map jsonMap2 = null;        
        try {
            jsonMap2 = getJson("http://localhost:8080/ping");
        } catch (Exception ex) {
            fail("Error getting content from url");
        }
        
        String responseMessage = jsonMap2.get("status").toString();
                
        assertEquals("pong",responseMessage);                
    }

    @Test
    public void test_ping_to_webserver_with_post(){

        String body = """
                      {"name":"fer"}
                      """;
        
        //get the json map from url
        Map jsonMap2 = null;        
        try {
            jsonMap2 = postJson("http://localhost:8080/pingJson?nombre=fernando",body);
        } catch (Exception ex) {
            fail("Error getting content from url");
        }
        
        String responseMessage = jsonMap2.get("status").toString();
        String method = jsonMap2.get("method").toString();
        String bodyResp = jsonMap2.get("body").toString();
        
        var bodyInJson = json(bodyResp);
        var name =bodyInJson.get("name");
                
        assertEquals("fernando",responseMessage);                
        assertEquals("POST",method);                
        assertEquals("fer",name);                        
    }

    /*
    TODO: Este test no lo corro ya que en la API cargo una URL, hay que cambiar el 
    server para poder modificar las URL y ver si cuando no hay ninguna devuelve 400
    @Test
    public void test_to_root_url(){

        try {
            var json = get("http://localhost:8080");
            assertEquals("400", String.valueOf(json.statusCode()));
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
                                       
    }
    */
    
    @Test
    public void test_post_to_external_api(){
               
        String body = """
{ 
  "prompt":"quiero que me digas cual es la distancia de la tierra a la luna, la mejor aproximación que puedas solo devuelvemen un número"
}
                      """;
        
        //get the json map from url
        Map<String, Object> jsonMap2 = null;        
        try {
            jsonMap2 = postJson("https://postman-echo.com/post",body);
        } catch (Exception ex) {
            fail("Error getting content from url");
        }
                
        @SuppressWarnings("unchecked")
        var responseMessage = (Map<String, Object>)jsonMap2.get("json");      
        
        String content = responseMessage.get("prompt").toString();
        
        String expected = "quiero que me digas cual es la distancia de la tierra a la luna, la mejor aproximación que puedas solo devuelvemen un número";
        assertEquals(expected,content);                
        
    }   
    
    
    /*
    @Test
    public void test_post_to_local_llm(){
               
        String body = """
{ 
  "prompt":"quiero que me digas cual es la distancia de la tierra a la luna, la mejor aproximación que puedas solo devuelvemen un número"
}
                      """;
        
        //get the json map from url
        Map<String, Object> jsonMap2 = null;        
        try {
            jsonMap2 = postJson("http://localhost:8082/v1/completions",body);
            //jsonMap2 = postJson("http://localhost:8082/fer",body);
        } catch (Exception ex) {
            Logger.getLogger(Api.class.getName()).log(Level.SEVERE, null, ex);            
            fail("Error getting content from url");
        }
                
        @SuppressWarnings("unchecked")
        //var responseMessage = (Map<String, Object>)jsonMap2.get("json");      
        
        //String content = responseMessage.get("prompt").toString();
        String content = jsonMap2.get("message").toString();
        
        //String expected = "quiero que me digas cual es la distancia de la tierra a la luna, la mejor aproximación que puedas solo devuelvemen un número";
        assertEquals("hola",content);                
        
    }   

    */
    
    private static void pong(HttpContext context) {
        String part1 = "{\"status\""; 
        String part2 = ":\"pong\"}"; 
        context.print(part1);
        context.write(part2);
        println(Level.INFO,part1+part2);
    };

    private static void pongJson(HttpContext context) {

        Enumeration<String> lista = context.getRequest().getHeaderNames();        
        while (lista.hasMoreElements()) {
            String header = lista.nextElement();
            println("Header: " + header +" --- "+ context.getRequest().getHeader(header));
        }                                                                    
        
        String nombreParam = context.getParameter("nombre");
        assertEquals(nombreParam, "fernando");
        
        String method = context.getRequest().getMethod();
        
        Map<String, Object> user = new HashMap<>();
        user.put("status", nombreParam);        
        user.put("method", method);
        
        if ("POST".equals(method)){
            user.put("body", context.getBody());
        }
                
        context.write(user);

    };
    
}
