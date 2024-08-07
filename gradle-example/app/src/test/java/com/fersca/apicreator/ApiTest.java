package com.fersca.apicreator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
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
public class ApiTest {
    
    public ApiTest() {
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
    public void test_read_files_with_json_content() throws IOException {
    
        Api api = new Api();
        ArrayList<Map<String, Object>> files = api.readFiles("/Users/Fernando.Scasserra/code/Varios/gradle-example/app/apis/test");
        
        //debería tener un solo elemento
        assertEquals(files.size(),1);
        
        //obtengo el valor del campo domain
        for (Map<String, Object> apiDescription : files){
            
            @SuppressWarnings("unchecked")
            var apiStructure = (Map<String, Object>) apiDescription.get("structure");
            String domain = apiStructure.get("domain").toString();                        
            assertEquals("users", domain);
        }
    
    
    }
    
}
