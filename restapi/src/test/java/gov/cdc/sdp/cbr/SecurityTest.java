package gov.cdc.sdp.cbr;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SecurityTest {

//    @Autowired
//    private JwtTokenStore tokenStore;
	
	@Value("${security.oauth2.client.clientId}")
	String clientId;

	@Value("${security.oauth2.client.clientSecret}")
	String clientSecret;
	
	@Value("${security.oauth2.client.accessTokenUri}")
	String accessTokenUri;
	
	@Value("${http.api.swagger.api.doc.uri}")
	String apiDocUri;
	
    @Test
    public void testClientCredentialsFlowWithValidCredentialsAndValidToken() {
 	
        final String tokenValue = obtainAccessToken(clientId, clientSecret);
        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);
        System.out.println("Token: " + tokenValue);
     
        // Get the Swagger yaml document for the CBR HTTP/REST API
        final ValidatableResponse validatableResponse = RestAssured.given()
																		.auth().preemptive().oauth2(tokenValue)
																	  .when()
																	  	.get(apiDocUri)
																	  .then()
																	  	.statusCode(200); 
											        
    }
    
    @Test
    public void testClientCredentialsFlowWithValidCredentialsAndInvalidToken() {
 	
        final String tokenValue = "BAD-TOKEN";
        System.out.println("Client ID: " + clientId);
        System.out.println("Client Secret: " + clientSecret);
        System.out.println("Token: " + tokenValue);
     
        // With an invalid token, we should not be able to retreive 
        // the Swagger yaml document for the CBR HTTP/REST API
        final ValidatableResponse validatableResponse = RestAssured.given()
									        							.auth().preemptive().oauth2(tokenValue)
									        						  .when()
									        						  	.get(apiDocUri)
									        						  .then()
									        						  	.statusCode(500);      
       
    }

    private String obtainAccessToken(String clientId, String clientSecret) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        final Response response = RestAssured.given()
						            			.params(params)
						            		.when()
						            			.post(accessTokenUri);
        return response.jsonPath()
            .getString("access_token");
    }

}