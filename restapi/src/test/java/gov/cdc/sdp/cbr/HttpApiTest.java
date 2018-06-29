package gov.cdc.sdp.cbr;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.test.context.junit4.SpringRunner;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;


@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT) // defaults to 8080
// @SpringBootTest //(webEnvironment = WebEnvironment.DEFINED_PORT)  // defaults to 8080
//@ContextConfiguration(locations = { "classpath:/spring/camel-context-activemq.xml" })
public class HttpApiTest extends Assert {

    private static final Logger LOG = LoggerFactory.getLogger(HttpApiTest.class);

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate template;
    
    @Before
    public void setUp() {
    	LOG.info("@Before setUp");
    }

    @After
    public void tearDown() {
    	LOG.info("@After tearDown");
    }
    
    @BeforeClass
    public static void setUpClass() {
      LOG.info("@BeforeClass setUpClass");
    }

    @AfterClass
    public static void tearDownClass() {
      LOG.info("@AfterClass tearDownClass");
    }
    
	@Test
	public void testGetOneMessage() throws Exception {
	
	    // NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();
	    
		// use http4 component to get the message
		// body is null
		String response = null;

        response = template.requestBody("http4://localhost:8080/sdp/cbr/messages/", null, String.class);
        if ((response != null) && (!response.isEmpty())) {
            LOG.info("Response: {}", response);
        } else {
        	LOG.info("No Messages.");
        }
	
	}
    
    
    @Test
    public void testGetAllMessages() throws Exception {

        // NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();
        
    	// use http4 component to get the message
    	// body is null
    	String response = null;
    	int count = 0;
		do {
		    response = template.requestBody("http4://localhost:8080/sdp/cbr/messages/", null, String.class);
		    if ((response != null) && (!response.isEmpty())) {
		        LOG.info("Response #{}: {}", ++count, response);
		    } else {
		    	LOG.info("End of Messages. Final count is {}", count);
		    }
		} while ((response != null) && (!response.isEmpty()));

    }
    
    @Test
    public void testPostTxtMessage() throws Exception {
        // use http4 component to post the message
    	LOG.info("Starting testPostTxtMessage");
        String filename = "src/test/resources/phinms_input_multi_hl7_no_quotes.txt";
        //InputStream hl7Stream = new FileInputStream(filename);
        File file = new File(filename);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody ("upfile", file, ContentType.DEFAULT_BINARY, filename);
        HttpEntity entity = builder.build();
        try {
        	Future<String> reply = template.asyncRequestBody("http4://localhost:8080/sdp/cbr/messages", entity, String.class);
        	reply.get(60, TimeUnit.SECONDS);
		} catch (Exception e) {
	        LOG.info("An error happened...");
			e.printStackTrace();
		}
        

        LOG.info("End of testPostTxtMessage");
    }
    
    @Test
    public void testPostCsvMessage() throws Exception {
        // use http4 component to post the message
    	LOG.info("Starting testPostCsvMessage");
        String filename = "src/test/resources/phinms_input_multi.csv";
        File file = new File(filename);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();         
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody ("upfile", file, ContentType.DEFAULT_BINARY, filename);
        HttpEntity entity = builder.build();
        try {
        	Future<String> reply = template.asyncRequestBody("http4://localhost:8080/sdp/cbr/messages/csv", entity, String.class);
        	reply.get(60, TimeUnit.SECONDS);
		} catch (Exception e) {
	        LOG.info("An error happened...");
			e.printStackTrace();
		}
        
        LOG.info("End of testPostCsvMessage");
    }
    
    @Test
    public void testSwagger() throws Exception {
        // use http4 component to get the swagger output
    	// body is null

    	String response = null;

        response = template.requestBody("http4://localhost:8080/sdp/cbr/api-doc", null, String.class);
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

