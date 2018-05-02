package gov.cdc.sdp.cbr;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.camel.test.spring.CamelSpringBootRunner;


@RunWith(CamelSpringBootRunner.class)
@SpringBootTest //(webEnvironment = WebEnvironment.DEFINED_PORT)  // defaults to 8080
//@ContextConfiguration(locations = { "classpath:/spring/camel-context-activemq.xml" })
public class HttpApiTest extends Assert {

    private static final Logger LOG = LoggerFactory.getLogger(HttpApiTest.class);

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate template;
    
    @Test
    public void testGetMessage() throws Exception {
        // use http4 component to get the message
    	// body is null

    	String response = null;
    	int count = 0;
    	do {
            response = template.requestBody("http4://localhost:8282/sdp/cbr/rest/messages/", null, String.class);
            if ((response != null) && (!response.isEmpty())) {
                LOG.info("Response #{}: {}", ++count, response);
            } else {
            	LOG.info("End of Messages. Final count is {}", count);
            }
    	} while ((response != null) && (!response.isEmpty()));

    }
    
    @Test
    public void testPostMessage() throws Exception {
        // use http4 component to post the message
    	LOG.info("Starting testPostMessage");
        String filename = "src/test/resources/phinms_input_multi.csv";
        //InputStream hl7Stream = new FileInputStream(filename);
        String content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
        try {
        	Future<String> reply = template.asyncRequestBody("http4://localhost:8282/sdp/cbr/rest/message", content, String.class);
        	reply.get(60, TimeUnit.SECONDS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
	        LOG.info("An error happened...");
			e.printStackTrace();
		}
        //String response = template.requestBodyAndHeader("http4://localhost:8080/sdp/cbr/message", body, "Accept", "application/json", String.class);
        //LOG.info("Response: {}", response);
        LOG.info("End of testPostMessage");
    }
    
    @Test
    public void testSwagger() throws Exception {
        // use http4 component to get the swagger output
    	// body is null

    	String response = null;

        response = template.requestBody("http4://localhost:8282/sdp/cbr/rest/api-doc", null, String.class);
        if ((response != null) && (!response.isEmpty())) {
            LOG.info("Response {}", response);
        }
    }

//    @Test
//    public void testCreateOrder() throws Exception {
//        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";
//
//        LOG.info("Sending order using json payload: {}", json);
//
//        // use restlet component to send the order
//        Map headers = new HashMap();
//        headers.put("Accept", "application/json");
//        headers.put("Content-Type", "application/json");
//        String id = template.requestBodyAndHeaders("restlet:http://localhost:8080/orders?restletMethod=POST", json, headers, String.class);
//        assertNotNull(id);
//
//        LOG.info("Created new order with id " + id);
//    }

//    @Test
//    public void testCreateAndGetOrder() throws Exception {
//        String json = "{\"partName\":\"motor\",\"amount\":1,\"customerName\":\"honda\"}";
//
//        LOG.info("Sending order using json payload: {}", json);
//
//        // use restlet component to send the order
//        Map headers = new HashMap();
//        headers.put("Accept", "application/json");
//        headers.put("Content-Type", "application/json");
//        String id = template.requestBodyAndHeaders("restlet:http://localhost:8080/orders?restletMethod=POST", json, headers, String.class);
//        assertNotNull(id);
//
//        LOG.info("Created new order with id " + id);
//
//        // use restlet component to get the order
//        String response = template.requestBodyAndHeader("restlet:http://localhost:8080/orders/" + id + "?restletMethod=GET", null, "Accept", "application/json", String.class);
//        LOG.info("Response: {}", response);
//    }

}

