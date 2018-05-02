package gov.cdc.sdp.cbr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;


@RunWith(CamelSpringRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:activemq-camel-context.xml" })
@PropertySource("classpath:application.properties")
public class HttpApiTest2 extends Assert {

    private static final Logger LOG = LoggerFactory.getLogger(HttpApiTest2.class);

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate template;
    
    @Before
    public void setup() {

    }
    
    @Test
    public void testGetMessage() throws Exception {
        // use http4 component to get the message
    	// body is null

        context.start();
        
        String response = template.requestBodyAndHeader("http4://localhost:8080/sdp/cbr/message/1", null, "Accept", "application/json", String.class);
        LOG.info("Response: {}", response);
    }
    
    @Test
    public void testPostMessage() throws Exception {
        // use http4 component to post the message
    	LOG.info("Starting testPostMessage");
        String filename = "src/test/resources/phinms_input_single.csv";
        InputStream hl7Stream = new FileInputStream(filename);
        try {
			template.requestBody("http4://localhost:8080/sdp/cbr/message", hl7Stream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
	        LOG.info("An error happened...");
			e.printStackTrace();
		}
        //String response = template.requestBodyAndHeader("http4://localhost:8080/sdp/cbr/message", body, "Accept", "application/json", String.class);
        //LOG.info("Response: {}", response);
        LOG.info("End of testPostMessage");
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

