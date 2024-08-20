package com.fersca.apicreator;


import static com.fersca.lib.HttpCli.get;
import static com.fersca.lib.HttpCli.post;
import static com.fersca.lib.HttpCli.delete;
import static com.fersca.lib.HttpCli.put;
import static com.fersca.lib.HttpCli.json;
import static com.fersca.lib.HttpCli.jsonArray;
import static com.fersca.apicreator.Api.DB;
import static com.fersca.apicreator.Api.saveJsonFile;
import static com.fersca.apicreator.Api.assureDirectory;
import static com.fersca.apicreator.Api.createAPIDefinition;
import static com.fersca.apicreator.Api.Directory;
import static com.fersca.apicreator.Api.rootPath;
import static com.fersca.apicreator.Api.deleteAPIDefinition;
import com.fersca.lib.Server;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static void deleteDirectory(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) { // Si es un directorio
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();       
    }
    
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
                         "seats":200,
                         "age":20,
                         "for_water":false
                         }
                         """;        
        
        //borra el directorio donde se deja el codigo calculado
        File directory = new File(rootPath+"apis_calculated_fields_code"); // Reemplaza con la ruta de tu directorio       
        deleteDirectory(directory);
                
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
                                "seats":"Number",
                                "age":"Number",
                                "for_water":"Boolean",
                                "example_invalid_type":"Invalido",
                                "test_summary":"Calculated",
                                "test_nickname":"Calculated",
                                "test_color":"Calculated",
                                "test_version":"Calculated"
                            }    
                        },
                        "post_validations":{
                            "name":"No puede tener más de 5 caracrteres"
                        },
                        "get_online_calculations":{
                            "test_summary":"obtener el nombre, ponerlo en mayúsculas, luego ponerle un guión, y poner tiene X años, en donde x es la edad",
                            "test_nickname":"obtener el nombre, ponerlo en mayúsculas, luego ponerle un guión, y poner tiene X años, en donde x es la edad",
                            "test_color":"obtener el nombre, ponerlo en mayúsculas, luego ponerle un guión, y poner tiene X años, en donde x es la edad"                                                                          
                        }
                        }                               
                               """;
        

        //crea la definición de la api
        createAPIDefinition(animals, animalsAPIDefinition);
        createAPIDefinition(planes, planesAPIDefinition);
        
        //borra la definición de los planetas que se crea con un test
        deleteAPIDefinition("planets");

        //inicia la app y levanta todos los files de las configuraciones
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
        Server.shutdownWebserver();        
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
        ArrayList<Map<String, Object>> files = api.readAPIDefinitionFiles(rootPath+"apis/test");
        
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
     
            String domain = "animals";
            String key = "34";

            //se asegura que se borre
            delete("http://localhost:8080/"+domain+"/"+key);                        
            
            //debería devolver 404
            var result = get("http://localhost:8080/"+domain+"/"+key);                        
            assertEquals("404", result.statusCode().toString());
            
            //No tiene que estar en la DB
            assertNull(DB.get(domain+"_"+key));
                        
            //No tiene que haber un file con ese dato
            assertFalse(fileExists(domain,key,Directory.DOMAIN));
            
        } catch (Exception ex) {
            Logger.getLogger(ApiTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
                    
    }
 
    private boolean fileExists(String domain, String key, Directory type){        
        String filePathString;
        if (type.equals(Directory.DOMAIN))
            filePathString = rootPath+"/db/"+domain+"/"+key+".json"; 
        else
            filePathString = rootPath+domain+"/"+key+".json"; 
        
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
        
    //Hacer un GET de un recurso que tenga un campo calculado y ver si genera el archivo con el script.
    @Test
    public void test_get_with_calculated_field_with_correct_compile_missing_and_runtime_errors() throws Exception {
        
        String domain = "planes";
        String key = "1";

        //debería devolver 200
        var result = get("http://localhost:8080/"+domain+"/"+key);                        
        assertEquals("200", result.statusCode().toString());

        var plane =json(result.body());

        assertEquals("Boing 747 is 20.0 years old.", plane.get("test_summary"));
        assertEquals("Script compile for planes/test_nickname error.", plane.get("test_nickname"));
        assertEquals("Runtime error for planes/test_color error.", plane.get("test_color"));
        assertEquals("Missing prompt for planes/test_version calculated field.", plane.get("test_version"));        
                        
    }
       
    //Hace un get de todos los elementos de una colección y se fija si devuelve un array
    @Test
    public void test_get_all_elements_from_a_domain() throws Exception {
        
        String domain = "planes";

        //debería devolver 200
        var result = get("http://localhost:8080/"+domain+"/all");                        
        assertEquals("200", result.statusCode().toString());

        var planes =jsonArray(result.body());

        var plane = planes.get(0);
        
        assertEquals("Boing 747 is 20.0 years old.", plane.get("test_summary"));
        assertEquals("Script compile for planes/test_nickname error.", plane.get("test_nickname"));
        assertEquals("Runtime error for planes/test_color error.", plane.get("test_color"));
        assertEquals("Missing prompt for planes/test_version calculated field.", plane.get("test_version"));        
                        
    }

    //Hace un get de todos los elementos de una colección y se fija si devuelve un array
    @Test
    public void test_an_invalid_command() throws Exception {
        
        String domain = "planes";

        //debería devolver 200
        var result = get("http://localhost:8080/"+domain+"/fer");                        
        assertEquals("400", result.statusCode().toString());
                        
    }

    //Hace un get de todos los elementos de una colección y se fija si devuelve un array
    @Test
    public void test_search_for_a_string_field() throws Exception {
        
        //debería devolver 200
        var result = get("http://localhost:8080/animals/search?name=Fish");                        
        assertEquals("200", result.statusCode().toString());
        
        var animals =jsonArray(result.body());
        
        //verifica que haya solo un elemento
        assertEquals(1,animals.size());
        
        var animal = animals.get(0);        
        
        //verifica que sea el nombre correcto
        assertEquals("Fish", animal.get("name"));        
                        
    }

    //Hace un get de todos los elementos de una colección y se fija si devuelve un array
    @Test
    public void test_search_for_non_existing_field_and_non_existing_value_shuld_return_200_with_empty_array() throws Exception {
        
        //debería devolver 200
        var result = get("http://localhost:8080/animals/search?name=Fishaaaa");                        
        assertEquals("200", result.statusCode().toString());
        
        var animals =jsonArray(result.body());
        
        //verifica que haya solo un elemento
        assertEquals(0,animals.size());

        //debería devolver 200
        result = get("http://localhost:8080/animals/search?nameeeeee=Fish");                        
        assertEquals("400", result.statusCode().toString());
                                
    }
        
    //Hace un get de todos los elementos de una colección y se fija si devuelve un array
    @Test
    public void test_search_for_number_and_boolean_fields() throws Exception {
        
        //Filtra por un animal que tiene 10 años (El leon)
        
        //debería devolver 200
        var result = get("http://localhost:8080/animals/search?age=10");                        
        assertEquals("200", result.statusCode().toString());
        
        var animals =jsonArray(result.body());
        
        //verifica que haya solo un elemento
        assertEquals(1,animals.size());
        
        var animal = animals.get(0);        
        
        //verifica que sea el nombre correcto
        assertEquals("Lion", animal.get("name"));        

        //Filtra por los que no son mamiferos (hay 2)
        
        //debería devolver 200
        result = get("http://localhost:8080/animals/search?mamal=false");                        
        assertEquals("200", result.statusCode().toString());
        
        animals =jsonArray(result.body());
        
        //Debería haber 2
        assertEquals(2,animals.size());

        //Buscar un animal que no sea mamifero y tenga 4 años (Shark)
        
        //debería devolver 200
        result = get("http://localhost:8080/animals/search?mamal=false&age=2");                        
        assertEquals("200", result.statusCode().toString());
        
        animals =jsonArray(result.body());
        
        //verifica que haya solo un elemento
        assertEquals(1,animals.size());
        
        animal = animals.get(0);        
        
        //verifica que sea el nombre correcto
        assertEquals("Snake", animal.get("name"));                
        
        
    }
    
    //Hace un get de todos los elementos de una colección y se fija si devuelve un array
    @Test
    public void test_get_specific_domain_info() throws Exception {
        
        //debería devolver 200
        var result = get("http://localhost:8080/planes");                        
        assertEquals("200", result.statusCode().toString());
        
        var animals =json(result.body());
        
        //verifica que haya solo un elemento
        assertEquals("planes",animals.get("name"));
        assertEquals(1.0,animals.get("elements_count"));
                               
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_get_all_domains_info() throws Exception {
        
        //debería devolver 200
        var result = get("http://localhost:8080");                        
        assertEquals("200", result.statusCode().toString());
        
        var domains =json(result.body());
        
        @SuppressWarnings("unchecked")
        var planes =  (Map<String, Object>) domains.get("planes");
        var animals = (Map<String, Object>) domains.get("animals");
                        
        //verifica que haya solo un elemento
        assertEquals("planes",((Map<String, Object>)planes.get("structure")).get("domain"));       
        assertEquals("Es un numero de 1 a 100",((Map<String, Object>)animals.get("post_validations")).get("age"));
                               
    }
    
    //POSTs Use cases    

    @Test
    public void test_creation_of_new_domain() throws Exception {
    
        String planetsAPIDefinition = """
                        {
                        "structure":{
                            "domain": "planets",
                            "accept":["GET"],
                            "fields":{
                                "id":"Number",
                                "name":"String",
                                "moons":"Number",
                                "age":"Number",
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
                  
        //hago el post (cosa que noe esta permitida)
        var result = post("http://localhost:8080/", planetsAPIDefinition);       
        
        //verifica que se haya creado
        assertEquals("201", result.statusCode().toString());       
        
        //verifica que se haya creado el archivo en el disco
        
        //verifica que cuando lo pido lo devueva en la URL (indicio que lo cargó en memoria)
        result = get("http://localhost:8080/planets");                        
        assertEquals("200", result.statusCode().toString());
        
        var planets =json(result.body());
        
        //verifica que haya solo un elemento
        assertEquals("planets",planets.get("name"));
        assertEquals(0.0,planets.get("elements_count"));
        
        @SuppressWarnings("unchecked")
        var apiDefinition = (Map<String, Object>) planets.get("api_definition");
        
        @SuppressWarnings("unchecked")
        var apiStructure = (Map<String, Object>) apiDefinition.get("structure");
        
        @SuppressWarnings("unchecked")
        var apiName = (String) apiStructure.get("domain");
        assertEquals("planets", apiName);
        
        //hago el post (cosa que noe esta permitida)
        result = post("http://localhost:8080/", planetsAPIDefinition);       
        
        //verifica que se haya creado
        assertEquals("400", result.statusCode().toString());       
                        
    }
       
    //TODO: HACER VARIOS TEST DONDE NO PASEN LA VALIDACION DEL POST DEL JSON AL CREAR UN DOMINIO
    
    //Hacer un POST y ver que cuando hago un GET se obtiene y que se haya grabado el archivo, verificar si devuelve el ID en la respuesta.
    @Test
    public void test_creation_of_new_element_in_a_domain() throws Exception {
    
   
        String domain = "animals";

        String jsonString = """
                            {
                            "name":"Tiger",
                            "age":3,
                            "mamal":true
                            }
                            """;
        
        //hago el post (cosa que noe esta permitida)
        var result = post("http://localhost:8080/"+domain, jsonString);                        
        
        //debería devolveme 201 created
        assertEquals("201", result.statusCode().toString());
        
        var tigerJson = json(result.body());
        
        Integer key = ((Double)tigerJson.get("id")).intValue();
        
        //debería devolver 200
        var resultGet = get("http://localhost:8080/"+domain+"/"+key);                        
        assertEquals("200", resultGet.statusCode().toString());

        var animal =json(resultGet.body());

        assertEquals("Tiger", animal.get("name"));

        //Tiene que estar en la DB y chequea el valor del json
        @SuppressWarnings("unchecked")
        Object dbElement = DB.get(domain+"_"+key);
        var jsonFromDB = json((String)dbElement);
        assertEquals("Tiger", jsonFromDB.get("name"));
        
        assertTrue(fileExists(domain, key.toString(),Directory.DOMAIN));
                                                      
    }
   
    //Hacer un POST con un ID en la URl y que no lo deje para no forzar ID
    @Test
    public void test_creation_of_new_element_with_id_in_URL_should_fail() throws Exception {
    
   
        String domain = "animals";

        String jsonString = """
                            {
                            "name":"Tiger",
                            "age":3,
                            "mamal":true
                            }
                            """;
        
        //hago el post (cosa que noe esta permitida)
        var result = post("http://localhost:8080/"+domain+"/7", jsonString);                        
        
        //debería devolveme 400 forbidden
        assertEquals("400", result.statusCode().toString());
                                                              
    }

    //Hacer un POST con un domain que no existe debería dar bad request
    @Test
    public void test_creation_of_new_element_with_a_not_valid_domain() throws Exception {
    
   
        String domain = "houses";

        String jsonString = """
                            {
                            "name":"Chalet"
                            }
                            """;
        
        //hago el post (cosa que noe esta permitida)
        var result = post("http://localhost:8080/"+domain, jsonString);                        
        
        //debería devolveme 400 forbidden
        assertEquals("400", result.statusCode().toString());
                                                              
    }
    
    
    //PUTs Use cases    
    
    //Hacer un PUT a un elemento que exita con un dato nuevo, pedir de nuevo el elemento, ver que viene actualizado y que se haya actualizado el archivo.
    @Test
    public void test_update_element_should_work() throws Exception {
    
   
        String domain = "animals";

        String jsonString = """
                            {
                            "name":"Shark",
                            "age":4,
                            "mamal":false
                            }
                            """;
        
        //hago el post (cosa que noe esta permitida)
        var result = put("http://localhost:8080/"+domain+"/2", jsonString);                        
        
        //debería devolveme 200 updated
        assertEquals("200", result.statusCode().toString());
        
        //verifica que el json de respuesta tenga el nombre correcto
        var sharkJson = json(result.body());        
        String name = ((String)sharkJson.get("name"));
        assertEquals("Shark", name);
       
        //verifica que en el get funcione
        var resultGet = get("http://localhost:8080/"+domain+"/2");                        
        assertEquals("200", resultGet.statusCode().toString());

        var animal =json(resultGet.body());

        assertEquals("Shark", animal.get("name"));

        //Tiene que estar en la DB y chequea el valor del json
        @SuppressWarnings("unchecked")
        Object dbElement = DB.get(domain+"_2");
        var jsonFromDB = json((String)dbElement);
        assertEquals("Shark", jsonFromDB.get("name"));        
                                                      
    }

    //Si se hace un PUT sin ID no tiene que dejarlo    
    @Test
    public void test_update_without_element_id_should_not_work() throws Exception {
       
        String domain = "animals";

        String jsonString = """
                            {
                            "name":"Shark",
                            "age":4,
                            "mamal":false
                            }
                            """;
        
        var result = put("http://localhost:8080/"+domain, jsonString);                        
        
        //debería devolveme 400 updated
        assertEquals("400", result.statusCode().toString());
                                                                      
    }

    //Hacer un PUT a un recurso que no exita y que te diga Not-Found.
    @Test
    public void test_update_to_non_existing_element_should_fail() throws Exception {
       
        String domain = "animals";

        String jsonString = """
                            {
                            "name":"Shark",
                            "age":4,
                            "mamal":false
                            }
                            """;
        
        var result = put("http://localhost:8080/"+domain+"/100", jsonString);                        
        
        //debería devolveme 404 updated
        assertEquals("404", result.statusCode().toString());
                                                                      
    }
    
    //DELETE Use cases        
    
    //Hacer un delete de un elemento que no exsita y ver si devuelve 409.
    @Test
    public void test_delete_non_existing_element_should_not_work() throws Exception {
       
        String domain = "animals";
        
        var result = delete("http://localhost:8080/"+domain+"/100");                        
        
        //debería devolveme 404 updated
        assertEquals("404", result.statusCode().toString());
                                                                      
    }

    //Hacer un delete de un elemento que exista, ver que no haya que dado en el caché ni en el file.
    @Test
    public void test_delete_existing_element_should_work() throws Exception {
    
   
        String domain = "animals";
        
        var result = delete("http://localhost:8080/"+domain+"/4");                        
        
        //debería devolveme 200 created
        assertEquals("200", result.statusCode().toString());
                
        //debería devolver 404
        var resultGet = get("http://localhost:8080/"+domain+"/4");                        
        assertEquals("404", resultGet.statusCode().toString());

        //No tiene que estar en la DB
        @SuppressWarnings("unchecked")
        Object dbElement = DB.get(domain+"_4");
        assertNull(dbElement);
                
        //no debería estar el archivo
        assertFalse(fileExists(domain, "4",Directory.DOMAIN));
                                                      
    }

    
    //Que no se pueda hacer un Delete sin ponerle el ID en la URL
    @Test
    public void test_delete_without_element_id_should_not_work() throws Exception {
       
        String domain = "animals";
        
        var result = delete("http://localhost:8080/"+domain);                        
        
        //debería devolveme 400 updated
        assertEquals("400", result.statusCode().toString());
                                                                      
    }
           
}
