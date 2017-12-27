package gov.cdc.sdp.cbr.restapi;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context.xml" })
@WebAppConfiguration
@TestPropertySource(properties = { "input.post.endpoint:direct:test-target" })

@PropertySource("classpath:application.properties")
public class InputApiTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    protected CamelContext camelContext;

    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mockEndpoint;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testBasic() throws Exception {
        // Check for basic test class setup
        assertNotNull(webApplicationContext);
        assertNotNull(mockMvc);
        
        // Check for invalid states
        mockMvc.perform(get("/cbr/input")).andExpect(status().is(405)); // No get
        mockMvc.perform(delete("/cbr/input")).andExpect(status().is(405)); // No delete
        mockMvc.perform(put("/cbr/input")).andExpect(status().is(405)); // No put
        mockMvc.perform(patch("/cbr/input")).andExpect(status().is(405)); // No patch
        mockMvc.perform(post("/cbr/input")).andExpect(status().is(400)); // Invalid params
    }
    
    @Test
    public void testMultipartSubmit() throws Exception {
  
        mockEndpoint.reset();
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "hello.txt", 
                MediaType.TEXT_PLAIN_VALUE, 
                "Hello, World!".getBytes());  // Fake data.  Should not need valid data for this.
        
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedHeaderReceived("CBR_ID", "CBR_testSrc_test");
        mockEndpoint.expectedHeaderReceived("sourceId", "test");
        mockEndpoint.expectedHeaderReceived("source", "testSrc");
        mockEndpoint.expectedHeaderReceived("METADATA", new HashMap<String,String>());
        mockEndpoint.expectedBodiesReceived(file);

        mockMvc.perform(fileUpload("/cbr/input")
                .file(file)
                .param("id", "test")
                .param("source", "testSrc")
                .param("metadata", "{}")) // JSON representation of a map -- will be translated  with GSON.
                .andExpect(status().isOk())
                .andExpect(content().string("CBR_testSrc_test")); // Returned CBR ID.

        MockEndpoint.assertIsSatisfied(camelContext);
    }
    
    @Test
    public void testMultipartSubmitWithCorruptMetadata() throws Exception {
  
        mockEndpoint.reset();
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "hello.txt", 
                MediaType.TEXT_PLAIN_VALUE, 
                "Hello, World!".getBytes());  // Fake data.  Should not need valid data for this.
        
        mockEndpoint.expectedMessageCount(0); // Metadata processing failed -- should not send message.

        mockMvc.perform(fileUpload("/cbr/input")
                .file(file)
                .param("id", "test")
                .param("source", "testSrc")
                .param("metadata", "I am a chicken bakaw bakaw")) // JSON representation of a map -- will be translated  with GSON.
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        MockEndpoint.assertIsSatisfied(camelContext);
    }
}
