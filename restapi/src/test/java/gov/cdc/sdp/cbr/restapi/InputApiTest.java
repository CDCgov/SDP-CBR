package gov.cdc.sdp.cbr.restapi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.AfterClass;
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

import gov.cdc.sdp.cbr.trace.TraceService;
import gov.cdc.sdp.cbr.trace.model.TraceStatus;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context.xml" })
@WebAppConfiguration
@TestPropertySource(properties = { "input.post.endpoint:direct:test-target" })

@PropertySource("classpath:application.properties")
public class InputApiTest {
    
    private MockMvc mockMvc;
    
    private static DataSource ds;
    
    @Autowired
    protected CamelContext camelContext;
    
    @Autowired
    protected TraceService traceService;

    @EndpointInject(uri = "mock:mock_endpoint")
    protected MockEndpoint mockEndpoint;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        if (InputApiTest.ds == null) {
            InputApiTest.ds = (DataSource) camelContext.getRegistry().lookupByName("traceLogDs");
        }
        String sql = "CREATE TABLE IF NOT EXISTS trace_log_api_test ("
                + "  id           bigserial primary key,"
                + "  cbr_id       varchar(255) NOT NULL, " 
                + "  source       varchar(255) NOT NULL, " 
                + "  status       int, "
                + "  description  varchar(255) NOT NULL, " 
                + "  created_at   timestamp NOT NULL)";
        Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        ps.close();
        conn.close();
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
    	// current size of test trace log messages
    	int traceSize = traceService.getTrace("CBR_testSrc_test").size();
    	
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
                .andExpect(content().string("CBR_testSrc_test/data:{}")); // Returned CBR ID.

        MockEndpoint.assertIsSatisfied(camelContext);
        // size should now be +1
        assertEquals(traceSize + 1, traceService.getTrace("CBR_testSrc_test").size());
    }
    
    @Test
    public void testMultipartSubmitWithCorruptMetadata() throws Exception {
        // Provides invalid JSON map structure for Metadata -- expect 422 failure.
    	int traceSize = traceService.getTrace("Error 422").size();
    	
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
                .param("metadata", "Sample corrupt metadata")) // Invalid JSON map -- will throw error  
                .andExpect(status().is(HttpStatus.UNPROCESSABLE_ENTITY.value()));

        MockEndpoint.assertIsSatisfied(camelContext);
        assertEquals(traceSize + 1, traceService.getTrace("Error 422").size());
    }
    
    @After
    public void tearDown() throws SQLException {
        DataSource ds = (DataSource) camelContext.getRegistry().lookupByName("traceLogDs");
        Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM trace_log_api_test");
        ps.execute();
        ps.close();
        conn.close();
    }
    
    @AfterClass 
    public static void afterClass() throws SQLException {
        Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement("DROP TABLE if exists trace_log_api_test");
        ps.execute();
        ps.close();
        conn.close();
    }
}
