package gov.cdc.sdp.cbr;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelTestContextBootstrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@BootstrapWith(CamelTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath:GenericTransformer-context.xml" })
@PropertySource("classpath:application.properties")
public class GenericTransformerTest {

	@Autowired
    protected CamelContext camelContext;

    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mock_endpoint;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;
    
    @Test
    public void testWithInputFromRest() throws InterruptedException, IOException {
        mock_endpoint.reset();
        
        String source_file = "src/test/resources/BatchTest_GenV2_2msgs.txt";
        
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE, 
                java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(source_file)));
        
        Exchange ex = new ExchangeBuilder(camelContext)
                .withBody(file)
                .withHeader("CBR_ID", "CBR_testSrc_test")
                .withHeader("ORIGINATING_CBR_ID", "CBR_testSrc_test")
                .withHeader("sourceId", "testSrc")
                .withHeader("METADATA", new HashMap<String,String>()).build();
        
        mock_endpoint.expectedMessageCount(3);
        mock_endpoint.expectedHeaderValuesReceivedInAnyOrder(
                "CBR_ID", 
                "CBR_testSrc_test_0", 
                "CBR_testSrc_test_1", 
                "CBR_testSrc_test_2");
        
        mock_endpoint.expectedHeaderReceived(
                "ORIGINATING_CBR_ID", 
                "CBR_testSrc_test");
        
        mock_endpoint.expectedHeaderReceived("sourceId", "testSrc");
        template.send(ex);

        MockEndpoint.assertIsSatisfied(camelContext);
    }

    private String readFile(String file) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
    }

}
