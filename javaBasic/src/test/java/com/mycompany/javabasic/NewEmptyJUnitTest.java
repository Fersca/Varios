package com.mycompany.javabasic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Fernando.Scasserra
 */
public class NewEmptyJUnitTest {
        
    @Test
    @DisplayName("Test simple greetings")
    public void testGreetings() {
    
        var jb = new JavaBasic();
        var m = jb.greetings();
        assertEquals(m,"Hola Fer");
        
    }
    
    @Test
    @DisplayName("Test json conversion to map")
    public void testJsonToMap(){

        var j = new JavaBasic();
        String json = "{\"name\":\"mkyong\", \"age\":33}";
        var jsonMap = j.processJson(json);

        assertEquals(jsonMap.get("name"),"mkyong" );
        
    }

    @Test
    @DisplayName("Test Get API")
    public void testGetAPI() throws Exception {
    
        var jb = new JavaBasic();
        
        //create the webserver
        jb.createHttpServer();

        String response = jb.httpCli("http://localhost:8080");
      
        System.out.println(response);
        
        //check the string response
        assertTrue(response.contains("Hello World"));        
                 
        //shutdown the webserver
        jb.shutdownWebserver();      
        
    }

    
}
