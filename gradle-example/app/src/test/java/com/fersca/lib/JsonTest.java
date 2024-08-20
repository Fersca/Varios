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
public class JsonTest {
    
    public JsonTest() {
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

    @Test
    public void test_json_class() {
        
        String jsonString = """
                        {
                        "structure":{
                            "domain": "planets",
                            "accept":["GET"],
                            "fields":{
                                "id":"Number",
                                "name":"String",
                                "moons":"Number",
                                "type":"String",
                                "habitable":"Boolean",                                      
                                "orbit":"Calculated"
                            }    
                        },
                        "post_validations":{
                            "age":"la edad tiene que ser mayor a 1000"
                        },
                        "get_online_calculations":{
                            "orbit":"La orbita se calcula multiplcando el campo age por 23"
                        }
                        }                               
                               """;
        
        var json = new Json(jsonString);
        
        assertEquals("planets",json.j("structure").s("domain"));
        assertEquals("Number",json.j("structure").j("fields").s("moons"));
        assertEquals("GET",json.j("structure").sa("accept").get(0));
               
    }
}
