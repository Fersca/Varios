package candidatemanager

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import grails.converters.JSON

class PruebaController {

	private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~/connections:(id,last-name)";
	
	//OAuthService service
	//Token requestToken
	Token accessToken
	
	String apiKey="qb17gxjjdr4b"
	String apiSecret="lqlQjuLB6mnUunc6"
    def index() {
	
		OAuthService service = new ServiceBuilder()
		.provider(LinkedInApi.class)
		.apiKey(apiKey)
		.apiSecret(apiSecret)
		.callback("http://localhost:8080/candidateManager/prueba/process")
		.build()
				
		// Obtain the Request Token
		// Fetching the Request Token
		Token requestToken = service.getRequestToken();
		
		// Now go and authorize Scribe here
		String url1 = service.getAuthorizationUrl(requestToken)
							
		session['REQUEST_TOKEN'] = requestToken
		
		redirect(url: url1)
	}
	
	def process(){
		
		String v = params.oauth_verifier
		String r=  session['REQUEST_TOKEN']
	
		getXmlStream(v,session['REQUEST_TOKEN'])
	
	}

	/*
	def apiUrl = "http://api.linkedin.com/v1/people/~:(" +
	"id," +
	"picture-url," +
	"site-standard-profile-request," +
	"first-name," +
	"date-of-birth," +
	"last-name," +
	"industry," +
	"location," +
	"educations," +
	"positions:(id,title,summary,start-date,end-date,is-current,company)," +
	"skills:(id,skill:(name),proficiency:(level),years:(name))," +
	"connections:(id,industry,first-name,last-name,site-standard-profile-request,headline,location,positions,educations,date-of-birth,picture-url,skills:(id,skill:(name),proficiency:(level),years:(name)))" +
	")"
	*/
	
	def apiUrl = "http://api.linkedin.com/v1/people/~?format=json"
	
	public void getXmlStream(String ver, rt)
	{
		OAuthService service=new ServiceBuilder()
							.provider(LinkedInApi.class)
							.apiKey(apiKey)
							.apiSecret(apiSecret)
							.build();
	
		Verifier v = new Verifier(ver);
	
		Token accessToken = service.getAccessToken(rt, v);
	
		session['ACCESS_TOKEN'] = accessToken
			
		render obtenerURL(apiUrl)

	}
	
	def obtenerURL(def URL){
		OAuthRequest request = new OAuthRequest(Verb.GET, URL);
		
		OAuthService service=new ServiceBuilder()
		.provider(LinkedInApi.class)
		.apiKey(apiKey)
		.apiSecret(apiSecret)
		.build();
		Token accessToken = session['ACCESS_TOKEN']
		service.signRequest(accessToken, request); // the access token from step 4
		Response response = request.send();
		String jsonString = response.getBody();
		def jsonObject = JSON.parse(jsonString)
	}
	
}
