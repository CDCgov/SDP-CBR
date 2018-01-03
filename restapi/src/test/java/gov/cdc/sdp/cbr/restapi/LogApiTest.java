package gov.cdc.sdp.cbr.restapi;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gov.cdc.sdp.cbr.trace.TraceLogTest;
import gov.cdc.sdp.cbr.trace.TraceService;
import gov.cdc.sdp.cbr.trace.model.TraceLog;
import gov.cdc.sdp.cbr.trace.model.TraceStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:context.xml" })
@WebAppConfiguration
@TestPropertySource(properties = { "input.post.endpoint:direct:test-target" })

@PropertySource("classpath:application.properties")
public class LogApiTest {
    
    private static DataSource ds;

    private MockMvc mockMvc;
    
    @Autowired
    protected CamelContext camelContext;
    
    @Autowired
    private TraceService traceService;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        if (LogApiTest.ds == null) {
            LogApiTest.ds = (DataSource) camelContext.getRegistry().lookupByName("traceLogDs");
        }
    }

    @Test
    public void testBasic() throws Exception {
        // Check for basic test class setup
        assertNotNull(webApplicationContext);
        assertNotNull(mockMvc);
        
        // Check for invalid states
        mockMvc.perform(post("/cbr/log/1234")).andExpect(status().is(405)); // No post
        mockMvc.perform(delete("/cbr/log/1234")).andExpect(status().is(405)); // No delete
        mockMvc.perform(put("/cbr/log/1234")).andExpect(status().is(405)); // No put
        mockMvc.perform(patch("/cbr/log/1234")).andExpect(status().is(405)); // No patch
        mockMvc.perform(get("/cbr/log/1234")).andExpect(status().isOk()); // Invalid params
    }
    
    @Test
    public void testRetrieveSingleMsg() throws Exception {
        
        traceService.addTraceMessage("cbrId1", "src", TraceStatus.ERROR, "This is msg 1");
        
        mockMvc.perform(get("/cbr/log/cbrId1"))
        
        .andDo(new ResultHandler(){

            @Override
            public void handle(MvcResult result) throws Exception {
                // TODO Auto-generated method stub
                String jsonContent = result.getResponse().getContentAsString();
                Gson gson = new Gson();
                List readFromJson = gson.fromJson(jsonContent, List.class);
                assertNotNull(readFromJson);
                
                for (Object o : readFromJson) {
                    TraceLog logMsg = gson.fromJson((String)o, TraceLog.class);
                    assertEquals(TraceStatus.ERROR,logMsg.getStatus());      
                    assertEquals("This is msg 1",logMsg.getDescription());                         
                }
            }});
        }
    
    @Test
    public void testRetrieveMultipleMessagesAndIds() throws Exception {
        
        traceService.addTraceMessage("cbrId1", "src", TraceStatus.WARN, "This is msg 1-1");
        traceService.addTraceMessage("cbrId1", "src", TraceStatus.INFO, "This is msg 1-2");
        traceService.addTraceMessage("cbrId1", "src", TraceStatus.INFO, "This is msg 1-3");
        traceService.addTraceMessage("cbrId1", "src", TraceStatus.INFO, "This is msg 1-4");
        traceService.addTraceMessage("cbrId2", "src", TraceStatus.INFO, "This is msg 2-1");
        traceService.addTraceMessage("cbrId2", "src", TraceStatus.INFO, "This is msg 2-2");
        
        mockMvc.perform(get("/cbr/log/cbrId1"))
        
        .andDo(new ResultHandler(){

            @Override
            public void handle(MvcResult result) throws Exception {
                // TODO Auto-generated method stub
                String jsonContent = result.getResponse().getContentAsString();
                Gson gson = new Gson();
                List readFromJson = gson.fromJson(jsonContent, List.class);
                assertNotNull(readFromJson);
                int count = 0;
                for (Object o : readFromJson) {
                    count++;
                    TraceLog logMsg = gson.fromJson((String)o, TraceLog.class);
                    assertEquals(count==1 ? TraceStatus.WARN : TraceStatus.INFO,logMsg.getStatus());      
                    assertEquals("This is msg 1-"+count,logMsg.getDescription());
                }
                assertEquals(4, count); // Should be 4 messages.
            }});
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
