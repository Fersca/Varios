package com.fersca.apicreator;


import com.fersca.lib.Server;
import static com.fersca.lib.HttpCli.get;
import static com.fersca.lib.HttpCli.post;
import static com.fersca.lib.HttpCli.json;
import static com.fersca.apicreator.Api.DB;
import static com.fersca.apicreator.Api.rootPath;
import static com.fersca.apicreator.Api.saveJsonFile;
import static com.fersca.apicreator.Api.assureDirectory;
import static com.fersca.apicreator.Api.createAPIDefinition;
import static com.fersca.apicreator.Api.Directory;
import static com.fersca.lib.HttpCli.HttpResult;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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
    public static void setUpClass() throws IOException {                                
        //Crea los jsons de ejemplo
        String animals = "animals";
        String planes = "planes";
        
        String animal1 = """
                         {
                         "id":1,
                         "name":"Lion",
                         "mamal":true,
                         "age":10
                         }
                         """;
        
        String animal2 = """
                         {
                         "id":2,
                         "name":"Cow",
                         "mamal":true,
                         "age":7
                         }
                         """;        
        
        String animal3 = """
                         {
                         "id":3,
                         "name":"Fish",
                         "mamal":false,
                         "age":3
                         }
                         """;        
        
        String animal4 = """
                         {
                         "id":4,
                         "name":"Snake",
                         "mamal":false,
                         "age":2
                         }
                         """;
        
        String plane1 = """
                         {
                         "id":1,
                         "name":"Boing 747",
                         "seats":200
                         }
                         """;        
        
        
        //grea los diretorio de los dominios
        assureDirectory(animals, Directory.DOMAIN);
        assureDirectory(planes, Directory.DOMAIN);
        
        //graba los jsons de ejemplo;
        saveJsonFile(animals, "1",animal1, Directory.DOMAIN);
        saveJsonFile(animals, "2",animal2, Directory.DOMAIN);
        saveJsonFile(animals, "3",animal3, Directory.DOMAIN);
        saveJsonFile(animals, "4",animal4, Directory.DOMAIN);
        
        saveJsonFile(planes, "1",plane1, Directory.DOMAIN);
        
        //guarda el archivo con la parametrización de la api de animals
        String animalsAPIDefinition = """
                        {
                        "structure":{
                            "domain": "animals",
                            "accept":["GET", "POST", "PUT","DELETE"],
                            "fields":{
                                "id":"Number",
                                "name":"String",
                                "age":"Number",
                                "mamal":"Boolean"
                            }    
                        },
                        "post_validations":{
                            "name":"No puede tener más de 5 caracrteres",
                            "age":"Es un numero de 1 a 100"
                        },
                        "get_online_calculations":{
                            "summary":"obtener el nombre, ponerlo en mayúsculas, luego ponerle un guión, y poner tiene X años, en donde x es la edad"
                        }
                        }                               
                               """;
        //guarda el archivo con la parametrización de la api de aviones
        String planesAPIDefinition = """
                        {
                        "structure":{
                            "domain": "planes",
                            "accept":["GET"],
                            "fields":{
                                "id":"Number",
                                "name":"String",
                                "seats":"Number"
                            }    
                        },
                        "post_validations":{
                            "name":"No puede tener más de 5 caracrteres"
                        },
                        "get_online_calculations":{
                        }
                        }                               
                               """;
        

        //crea la definición de la api
        createAPIDefinition(animals, animalsAPIDefinition);
        createAPIDefinition(planes, planesAPIDefinition);
        
        String[] args = {""};
        try {        
            Api.main(args);
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(ApiTest.class.getName()).log(Level.SEVERE, null, ex);            
            Assert.fail();              
        }        
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {              
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
        ArrayList<Map<String, Object>> files = api.readAPIDefinitionFiles("/Users/Fernando.Scasserra/code/Varios/gradle-example/app/apis/test");
        
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


    //GETS Use cases
    
    //Hacer un GET a un recurso que no existe y que devuelva 404 y no exista el archivo.
    @Test
    public void test_get_to_non_existing_key_should_return_404() {
    
        try {
     
            String domain = "animales";
            String key = "34";
            
            //debería devolver 404
            var result = get("http://localhost:8080/"+domain+"/"+key);                        
            assertEquals("404", result.statusCode().toString());
            
            //No tiene que estar en la DB
            assertNull(DB.get(domain+"_"+key));
                        
            //No tiene que haber un file con ese dato
            assertFalse(fileExists(domain,key));
            
        } catch (Exception ex) {
            Logger.getLogger(ApiTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
                    
    }
 
    private boolean fileExists(String domain, String key){        
        String filePathString = rootPath+domain+"/"+key+".json"; 
        Path filePath = Paths.get(filePathString);
        return Files.exists(filePath);                
    }
    
    
    //Hacer un GET a un recurso que exista en el file, devolve el JSON y chequear que quedó en la DB.
    @Test
    public void test_get_existing_element_should_return_200() throws Exception {
    
   
        String domain = "animals";
        String key = "1";

        //debería devolver 200
        var result = get("http://localhost:8080/"+domain+"/"+key);                        
        assertEquals("200", result.statusCode().toString());

        var animal =json(result.body());

        assertEquals("Lion", animal.get("name"));

        //Tiene que estar en la DB y chequea el valor del json
        @SuppressWarnings("unchecked")
        Object dbElement = DB.get(domain+"_"+key);
        var jsonFromDB = json((String)dbElement);
        assertEquals("Lion", jsonFromDB.get("name"));
                                                       
    }

    @Test
    public void test_invalid_http_method_shoud_return_405() throws Exception {
    
   
        String domain = "planes";
        String key = "1";

        String jsonString = """
                            {
                            "name":"747",
                            "seats":300
                            }
                            """;
        
        //hago el post (cosa que noe esta permitida)
        var result = post("http://localhost:8080/"+domain, jsonString);                        
        
        //debería devolveme 405
        assertEquals("405", result.statusCode().toString());
                                                      
    }
    
    
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
