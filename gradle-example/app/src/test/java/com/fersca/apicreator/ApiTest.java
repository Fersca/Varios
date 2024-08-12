package com.fersca.apicreator;


import com.fersca.lib.Server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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

    @Test
    public void test_launch_api_builder() {
    
        String[] args = {""};
        try {        
            Api.main(args);
        } catch (Exception ex) {
            Assert.fail();              
            Logger.getLogger(ApiTest.class.getName()).log(Level.SEVERE, null, ex);
        }        
            
    }

    //GETS Use cases
    
        //Hacer un GET a un recurso que no existe y que devuelva 404 y no exista el archivo.

        //Hacer un GET a un recurso que exista en el file, devolve el JSON y chequear que quedó en la DB.

        //Verificar de alguna manera otra vez ese GET y que sea devuelto desde el Caché (ver si viene con un header de cache para identificarlo o un flag).

        //Hacer un GET de un recurso que tenga un campo calculado y ver si genera el archivo con el script.
    
        //Hacer un GET de un recurso que tiene un campo calculado y ver si se compila correctamente y se ejecuta el script.            

    //POSTs Use cases    
    
        //Hacer un POST y ver que cuando hago un GET se obtiene y que se haya grabado el archivo, verificar si devuelve el ID en la respuesta.

        //Hacer un POST de un elemento para un domain que no tenga el directorio y ver si lo crea.
    
        //Hacer un POST con un ID en la URl y que no lo deje para no forzar ID

    //PUTs Use cases    
    
        //Hacer un PUT a un elemento que exita con un dato nuevo, pedir de nuevo el elemento, ver que viene actualizado y que se haya actualizado el archivo.
    
        //Si se hace un PUT sin ID no tiene que dejarlo
    
        //Hacer un PUT a un recurso que no exita y que te diga Not-Found.

    //DELETE Use cases        
    
        //Hacer un delete de un elemento que no exsita y ver si devuelve 409.

        //Hacer un delete de un elemento que exista, ver que no haya que dado en el caché ni en el file.
    
        //Que no se pueda hacer un Delete sin ponerle el ID en la URL
       
    
}
