package hello.world;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.util.Collections;

/**
 *
 * @author Fernando.Scasserra
 */
@Controller("/hello")
public class HelloController {

    @Get()  //(produces = MediaType.TEXT_PLAIN) // 
    public HttpResponse index() {
        return HttpResponse.ok(Collections.singletonMap("msg", "OK"));
    }
}