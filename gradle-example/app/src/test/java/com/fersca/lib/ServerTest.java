package com.fersca.lib;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    }
    
    @Before
    public void setUp() {
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
}
