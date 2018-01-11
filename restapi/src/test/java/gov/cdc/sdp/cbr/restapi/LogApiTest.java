package gov.cdc.sdp.cbr.restapi;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Message;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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

import gov.cdc.sdp.cbr.HL7V2BatchSplitter;
import gov.cdc.sdp.cbr.model.SDPMessage;
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
    
	private static final String MESSAGE_ID = "Test_message";
	private static final String VALID_BATCH_FILE_NAME = "src/test/resources/BatchTest_GenV2_2msgs.txt";
    
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
                
                String jsonContent = result.getResponse().getContentAsString();
                Gson gson = new Gson();
                TraceLog[] logMsg = gson.fromJson(jsonContent, TraceLog[].class);
                assertNotNull(logMsg);
                
                for (TraceLog traceLog : logMsg) {
                    assertEquals(TraceStatus.ERROR, traceLog.getStatus());      
                    assertEquals("This is msg 1", traceLog.getDescription());                         
                	
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
                TraceLog[] readFromJson = gson.fromJson(jsonContent, TraceLog[].class);
                assertNotNull(readFromJson);
                int count = 0;
                for (TraceLog traceLog : readFromJson) {
                    count++;
                    
                    assertEquals(count==1 ? TraceStatus.WARN : TraceStatus.INFO,traceLog.getStatus());      
                    assertEquals("This is msg 1-"+count,traceLog.getDescription());
                }
                assertEquals(4, count); // Should be 4 messages.
            }});
        }
    
    @Test
    public void testRetrieveSingleStatus() throws Exception {
        
        traceService.addTraceMessage("cbrId1", "src", TraceStatus.INFO, "This is msg 1");
        
        mockMvc.perform(get("/cbr/status/cbrId1"))
        
        .andDo(new ResultHandler(){

            @Override
            public void handle(MvcResult result) throws Exception {
                // TODO Auto-generated method stub
                String jsonContent = result.getResponse().getContentAsString();
                Gson gson = new Gson();
                assertNotNull(jsonContent);
                
                assertEquals(gson.toJson(TraceStatus.INFO), jsonContent);      
                
            }});
        }
    
    @Test
    public void testRetrieveBatchStatus() throws Exception {
        
        traceService.addTraceMessage(MESSAGE_ID + "_batch_0", "src", TraceStatus.INFO, "This is batch 0");
        
        mockMvc.perform(get("/cbr/batchstatus/" + MESSAGE_ID + "_batch_0"))
        
        .andDo(new ResultHandler(){

            @Override
            public void handle(MvcResult result) throws Exception {
                // TODO Auto-generated method stub
                String jsonContent = result.getResponse().getContentAsString();
                Gson gson = new Gson();
                HL7V2BatchSplitter splitter = new HL7V2BatchSplitter();
                Message msg = new DefaultMessage();
                SDPMessage sdpMsg = new SDPMessage();
                sdpMsg.setId(MESSAGE_ID);

                String expected_bh = MESSAGE_ID + "_batch_0";
                msg.setHeader(SDPMessage.SDP_MESSAGE_HEADER, new Gson().toJson(sdpMsg));
                msg.setBody(readFile(VALID_BATCH_FILE_NAME));

                List<Message> messages = splitter.splitMessage(msg);
                assertEquals(3, messages.size());
                
                for (Message m : messages) {
                    SDPMessage out = new Gson().fromJson((String) m.getHeader(SDPMessage.SDP_MESSAGE_HEADER), SDPMessage.class);
                    assertEquals(expected_bh, out.getBatchId());
                    
                }
               
                assertNotNull(jsonContent);
                assertEquals(gson.toJson(TraceStatus.INFO), jsonContent);      
                
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
    
    private String readFile(String file) throws IOException {
        return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(file)));
    }
}
